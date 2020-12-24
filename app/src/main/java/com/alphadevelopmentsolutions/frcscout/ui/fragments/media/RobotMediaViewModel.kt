package com.alphadevelopmentsolutions.frcscout.ui.fragments.media

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.Camera
import android.hardware.display.DisplayManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ImageButton
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.net.toFile
import androidx.lifecycle.*
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.data.models.PhotoFile
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.exceptions.NudityException
import com.alphadevelopmentsolutions.frcscout.extensions.runOnUiThread
import com.alphadevelopmentsolutions.frcscout.factories.PhotoFileFactory
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.interfaces.Constant
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.nipunru.nsfwdetector.NSFWDetector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileNotFoundException
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.collections.ArrayList
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class RobotMediaViewModel(
    val context: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    view: View?,
    var media: RobotMedia?,
    val team: Team?
) : AndroidViewModel(context.application) {

    val photoUri = MutableLiveData<String?>()
    private var photoFile: File? = null

    var mediaSet = false
    var mediaProvided = false

    init {
        if (media != null) {
            mediaSet = true
            mediaProvided = true

            photoUri.value = media?.localFileUri
        }

        else if (team != null) {
            createMedia(team)
            mediaSet = false
        }
    }

    private fun createMedia(team: Team) {
        val keyStore = KeyStore.getInstance(context)
        val event = keyStore.selectedEvent
        val account = Account.getInstance(context)
        val tempPhotoFile = PhotoFileFactory.createFile(Constant.UUID_GENERATOR.generate().toString(), context)
        photoFile = tempPhotoFile

        if (event != null && account != null && tempPhotoFile != null) {
            media =
                RobotMedia(
                    ByteArray(0), // TODO: Update to use team account id
                    event.id,
                    team.id,
                    account.id,
                    null,
                    tempPhotoFile.name,
                    tempPhotoFile.absolutePath
                )
        }
        else
            AppLog.e(
                Exception(
                    "Error creating robot media photo. Event null? ${event == null} Account null? ${account == null} Photo null? ${tempPhotoFile == null}"
                )
            )

    }

    fun captureMedia(cameraLayout: ConstraintLayout, mediaLayout: ConstraintLayout) {
        imageCapture?.let { imageCapture ->
            photoFile?.let { photoFile ->
                val metadata =
                    ImageCapture.Metadata().apply {
                        // Mirror image when using the front camera
                        isReversedHorizontal = lensFacing == CameraSelector.LENS_FACING_FRONT
                    }

                // Create output options object which contains file + metadata
                val outputOptions =
                    ImageCapture.OutputFileOptions.Builder(photoFile)
                        .setMetadata(metadata)
                        .build()

                // Setup image capture listener which is triggered after photo has been taken
                imageCapture.takePicture(
                    outputOptions,
                    cameraExecutor,
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onError(e: ImageCaptureException) {
                            AppLog.e(e)
                        }

                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            media?.let {
                                runOnUiThread {

                                    val uri = (output.savedUri ?: Uri.fromFile(photoFile)).path

                                    NSFWDetector.isNSFW(
                                        BitmapFactory.decodeFile(uri)
                                    ) { isNSFW, confidence, image ->
                                        if (!isNSFW) {
                                            photoUri.value = uri

                                            mediaLayout.visibility = View.VISIBLE
                                            cameraLayout.visibility = View.GONE
                                        }
                                        else {
                                            AppLog.e(NudityException(confidence, Account.getInstance(context)?.id ?: ByteArray(0)))

                                            cancelCapture(cameraLayout, mediaLayout)
                                        }
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    }

    override fun onCleared() {
        if (!mediaProvided) {
            cameraExecutor.shutdown()

            displayManager.unregisterDisplayListener(displayListener)
        }

        super.onCleared()
    }

    fun cancelCapture(cameraLayout: ConstraintLayout, mediaLayout: ConstraintLayout) {
        photoFile?.delete()

        team?.let { team ->
            createMedia(team)

            cameraLayout.visibility = View.VISIBLE
            mediaLayout.visibility = View.GONE

        }
    }

    fun saveImage() {
        media?.let {
            lifecycleOwner.lifecycle.coroutineScope.launch(Dispatchers.IO) {
                RepositoryProvider.getInstance(context).robotMediaRepository.insert(it)

                runOnUiThread {
                    context.removeCurrentFragment()
                }
            }
        }
    }


    private var displayId: Int = -1
    private var lensFacing: Int = CameraSelector.LENS_FACING_BACK
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: androidx.camera.core.Camera? = null
    private var cameraProvider: ProcessCameraProvider? = null

    private lateinit var outputDirectory: File
    lateinit var broadcastManager: LocalBroadcastManager

    private val displayManager by lazy {
        context.getSystemService(Context.DISPLAY_SERVICE) as DisplayManager
    }

    lateinit var cameraExecutor: ExecutorService

    companion object {
        private const val RATIO_4_3_VALUE = 4.0 / 3.0
        private const val RATIO_16_9_VALUE = 16.0 / 9.0
    }

    private fun setupCamera(cameraPreviewView: PreviewView, cameraSwitchButton: ImageButton) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        cameraProviderFuture.addListener({
            cameraProvider = cameraProviderFuture.get()

            lensFacing = when {
                hasBackCamera() -> CameraSelector.LENS_FACING_BACK
                hasFrontCamera() -> CameraSelector.LENS_FACING_FRONT
                else -> throw IllegalStateException("Back and front camera are unavailable")
            }

            updateCameraSwitchButton(cameraSwitchButton)
            bindCameraUseCases(cameraPreviewView)
        }, ContextCompat.getMainExecutor(context))
    }

    private fun aspectRatio(width: Int, height: Int): Int {
        val previewRatio = max(width, height).toDouble() / min(width, height)

        if (abs(previewRatio - RATIO_4_3_VALUE) <= abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3
        }

        return AspectRatio.RATIO_16_9
    }

    fun switchCameraView(cameraPreviewView: PreviewView) {
        lensFacing = 
            if (CameraSelector.LENS_FACING_FRONT == lensFacing)
                CameraSelector.LENS_FACING_BACK
            else
                CameraSelector.LENS_FACING_FRONT
        
        // Re-bind use cases to update selected camera
        bindCameraUseCases(cameraPreviewView)
    }

    private fun bindCameraUseCases(cameraPreviewView: PreviewView) {

        // Get screen metrics used to setup camera for full screen resolution
        val metrics = DisplayMetrics().also { cameraPreviewView.display.getRealMetrics(it) }
        val screenAspectRatio = aspectRatio(metrics.widthPixels, metrics.heightPixels)
        val rotation = cameraPreviewView.display.rotation
        val cameraProvider = cameraProvider ?: throw IllegalStateException("Camera initialization failed.")
        val cameraSelector = CameraSelector.Builder().requireLensFacing(lensFacing).build()

        preview =
            Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

        imageCapture =
            ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

        imageAnalyzer =
            ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setTargetRotation(rotation)
                .build()

        cameraProvider.unbindAll()

        try {
            camera =
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture,
                    imageAnalyzer
                )

            preview?.setSurfaceProvider(cameraPreviewView.surfaceProvider)
        } catch (e: Exception) {
            AppLog.e(e)
        }
    }

    /** Enabled or disabled a button to switch cameras depending on the available cameras */
    private fun updateCameraSwitchButton(cameraSwitchButton: ImageButton) {
        try {
            cameraSwitchButton.isEnabled = hasBackCamera() && hasFrontCamera()
        } catch (exception: CameraInfoUnavailableException) {
            cameraSwitchButton.isEnabled = false
        }
    }

    /** Returns true if the device has an available back camera. False otherwise */
    private fun hasBackCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA) ?: false
    }

    /** Returns true if the device has an available front camera. False otherwise */
    private fun hasFrontCamera(): Boolean {
        return cameraProvider?.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA) ?: false
    }

    fun onConfigurationChanged(cameraSwitchButton: ImageButton) {
        if (!mediaSet) {
            // Enable or disable switching between cameras
            updateCameraSwitchButton(cameraSwitchButton)
        }
    }

    private val displayListener = object : DisplayManager.DisplayListener {
        override fun onDisplayAdded(displayId: Int) = Unit
        override fun onDisplayRemoved(displayId: Int) = Unit
        override fun onDisplayChanged(displayId: Int) =
            view?.let { view ->
                if (displayId == this@RobotMediaViewModel.displayId) {

                    imageCapture?.targetRotation = view.display.rotation
                    imageAnalyzer?.targetRotation = view.display.rotation
                }
            } ?: Unit
    }

    fun onViewCreated(cameraPreviewView: PreviewView, cameraSwitchButton: ImageButton) {
        if (!mediaSet) {
            // Initialize our background executor
            cameraExecutor = Executors.newSingleThreadExecutor()

            broadcastManager = LocalBroadcastManager.getInstance(context)

            // Every time the orientation of device changes, update rotation for use cases
            displayManager.registerDisplayListener(displayListener, null)

            // Wait for the views to be properly laid out
            cameraPreviewView.post {

                // Keep track of the display in which this view is attached
                displayId = cameraPreviewView.display.displayId

                // Build UI controls
//            updateCameraUi()

                // Set up the camera and its use cases
                setupCamera(cameraPreviewView, cameraSwitchButton)
            }
        }
    }
}
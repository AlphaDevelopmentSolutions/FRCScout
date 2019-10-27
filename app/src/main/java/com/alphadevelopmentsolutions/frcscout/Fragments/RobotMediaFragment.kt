package com.alphadevelopmentsolutions.frcscout.Fragments

import android.graphics.Matrix
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Rational
import android.view.*
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.camera.core.*
import androidx.lifecycle.LifecycleOwner
import com.alphadevelopmentsolutions.frcscout.Classes.AutoFitPreviewBuilder
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_robot_media.view.*
import java.io.File
import java.lang.Exception
import java.util.*
import java.util.concurrent.Executors


class RobotMediaFragment : MasterFragment(), LifecycleOwner
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    private var robotMediaJson: String? = null
    
    private lateinit var robotMedia: RobotMedia

    private lateinit var robotMediaImageView: ImageView

    private lateinit var mediaFilePath: String

    private val executor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        if (arguments != null)
            robotMediaJson = arguments!!.getString(ARG_ROBOT_MEDIA)

        if (robotMediaJson != null && robotMediaJson != "")
            robotMedia = Gson().fromJson(robotMediaJson, RobotMedia::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_media, container, false)
        context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })

        robotMediaImageView = view.RobotMediaImageView

        //robot media loaded, load image into the imageview
        if (::robotMedia.isInitialized)
        {
            view.CameraControls.visibility = View.GONE

            robotMediaImageView.setImageBitmap(robotMedia.imageBitmap)
        }
        else
        {



            startCamera(
                    CameraX.LensFacing.BACK,
                    view.ViewFinder,
                    view.SwitchCameraImageView,
                    view.TakePictureImageView,
                    view.CancelImageView,
                    view.SaveImageView,
                    view.CameraControls,
                    view.ImageControls
            )
        }

        loadingThread.join()

        context.setToolbarTitle("${team!!.id} Robot Media")

        return view
    }

    private fun startCamera(
                            cameraLens: CameraX.LensFacing,
                            viewFinder: TextureView,
                            switchCameraImageView: ImageView,
                            takePictureImageView: ImageView,
                            cancelImageView: ImageView,
                            saveImageView: ImageView,
                            cameraControls: LinearLayout,
                            imageControls: LinearLayout)
    {
        viewFinder.post {
            val viewFinderConfig = PreviewConfig.Builder().apply {
                setTargetAspectRatio(
                        with(DisplayMetrics().also { viewFinder.display.getRealMetrics(it) })
                        {
                            Rational(widthPixels, heightPixels)
                        }
                )
                setTargetRotation(viewFinder.display.rotation)
                setLensFacing(cameraLens)
            }.build()

            val preview = AutoFitPreviewBuilder.build(viewFinderConfig, viewFinder).apply {
                setOnPreviewOutputUpdateListener {

                    // To update the SurfaceTexture, we have to remove it and re-add it
                    val parent = viewFinder.parent as ViewGroup
                    parent.removeView(viewFinder)
                    parent.addView(viewFinder, 0)

                    viewFinder.surfaceTexture = it.surfaceTexture
                }
            }


            val imageCapture = ImageCapture(ImageCaptureConfig.Builder().apply {
                setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
            }.build())

            takePictureImageView.setOnClickListener {
                imageCapture.takePicture(
                        File.createTempFile(
                                UUID.randomUUID().toString(),
                                ".jpeg",
                                context.getExternalFilesDir(Constants.ROBOT_MEDIA_DIRECTORY)).apply {
                            mediaFilePath = absolutePath
                        },
                        object : ImageCapture.OnImageSavedListener {
                            override fun onError(
                                    imageCaptureError: ImageCapture.ImageCaptureError,
                                    message: String,
                                    exc: Throwable?
                            ) {
                                AppLog.error(Exception(exc))
                            }

                            override fun onImageSaved(file: File)
                            {
                                cameraControls.visibility = View.GONE
                                imageControls.visibility = View.VISIBLE

                                cancelImageView.setOnClickListener {

                                    imageControls.visibility = View.GONE
                                    cameraControls.visibility = View.VISIBLE

                                    if(file.exists())
                                        file.delete()

                                    startCamera(
                                            cameraLens,
                                            viewFinder,
                                            switchCameraImageView,
                                            takePictureImageView,
                                            cancelImageView,
                                            saveImageView,
                                            cameraControls,
                                            imageControls
                                    )
                                }

                                saveImageView.setOnClickListener {
                                    robotMedia = RobotMedia(
                                            -1,
                                            year!!.serverId,
                                            event!!.blueAllianceId,
                                            team!!.id,
                                            file.absolutePath,
                                            true).apply { save(context.database) }

                                    context.supportFragmentManager.popBackStackImmediate()
                                }

                                CameraX.unbindAll()
                            }
                        })
            }

            switchCameraImageView.setOnClickListener {

                CameraX.unbindAll()

                startCamera(
                        if(cameraLens == CameraX.LensFacing.FRONT) CameraX.LensFacing.BACK else CameraX.LensFacing.FRONT,
                        viewFinder,
                        switchCameraImageView,
                        takePictureImageView,
                        cancelImageView,
                        saveImageView,
                        cameraControls,
                        imageControls
                )
            }


            CameraX.bindToLifecycle(this, preview, imageCapture)
        }
    }

    override fun onDetach()
    {
        //robot media never saved, delete image
        if ((!::robotMedia.isInitialized && ::mediaFilePath.isInitialized) || (::robotMedia.isInitialized && robotMedia.id == -1))
            File(mediaFilePath).apply {
                if(exists())
                    delete()
            }

        super.onDetach()
    }

    companion object
    {
        private const val ARG_ROBOT_MEDIA = "robot_media_json"

        /**
         * Creates a new instance
         * @param robotMedia to show on the fragment
         * @param team to get robot media from
         * @return A new instance of fragment [RobotMediaFragment].
         */
        fun newInstance(robotMedia: RobotMedia?, team: Team): RobotMediaFragment
        {
            val fragment = RobotMediaFragment()
            val args = Bundle()
            args.putString(ARG_ROBOT_MEDIA, toJson(robotMedia))
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

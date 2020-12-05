package com.alphadevelopmentsolutions.frcscout.api

import android.app.Application
import android.graphics.Bitmap
import android.security.keystore.UserNotAuthenticatedException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.callbacks.OnAnimationCompleteCallback
import com.alphadevelopmentsolutions.frcscout.callbacks.OnProgressCallback
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.classes.PhotoFileChunk
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.exceptions.ApiException
import com.alphadevelopmentsolutions.frcscout.exceptions.PreconditionFailedException
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.interfaces.HttpResponseCode
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.dialogs.LoadingDialogFragment
import com.google.android.material.snackbar.Snackbar
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.net.UnknownHostException
import java.util.*

open class ApiViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private var INSTANCE: ApiViewModel? = null

        fun getInstance(owner: MainActivity) =
            INSTANCE ?: synchronized(this) {
                val tempInstance =
                    ViewModelProvider(owner).get(ApiViewModel::class.java)

                INSTANCE = tempInstance

                tempInstance
            }
    }

    private val repositoryProvider: RepositoryProvider by lazy {
        RepositoryProvider.getInstance(application)
    }

    /**
     * Submits data to the API
     */
    suspend fun submitData(context: MainActivity, onProgressCallback: OnProgressCallback? = null): Boolean {

        val appData =
            AppData.create().apply {
                checklistItemResultList = repositoryProvider.checklistItemResultRepository.getAllRaw(true)
                robotInfoList = repositoryProvider.robotInfoRepository.getAllRaw(true)
                robotMediaList = repositoryProvider.robotMediaRepository.getAllRaw(true)
                scoutCardInfoList = repositoryProvider.scoutCardInfoRepository.getAllRaw(true)

            }

        onProgressCallback?.onProgressChange(
            String.format(
                context.getString(
                    R.string.ellipses_dynamic
                ),
                context.getString(
                    R.string.uploading_updates
                )
            )
        )

        val response =
            Api.getInstance(context)
                .setData(appData)
                .execute()

        if (response.isSuccessful) {
            response.body()?.let { responseBody ->

                repositoryProvider.logDeleteRepository.disable()

                onProgressCallback?.onProgressChange(String.format(context.getString(R.string.ellipses_dynamic), context.getString(R.string.upload_complete)))

                val fullSuccess = responseBody.fullSuccess() && submitPhotos(context, onProgressCallback)

                if (fullSuccess) {
//                    RDatabase.getInstance(context).clearDatabase(
//                        logDelete = false,
//                        includePhotoFiles = false
//                    )
                } else {
                    responseBody.getErrors().forEach { error ->
                        AppLog.e(ApiException(error.error, error.message))
                    }
                }

                return fullSuccess
            }
        } else
            catchError(context, response.errorBody())

        return false
    }

    /**
     * Submits images to the API
     */
    suspend fun submitPhotos(context: MainActivity, onProgressCallback: OnProgressCallback? = null): Boolean {

        if (BuildConfig.DEBUG)
            Api.setInterceptorLevel(HttpLoggingInterceptor.Level.HEADERS)

        // Max upload size to the server in MB
        val maxUpload = 30.0 * (4.0 / 5.0)
        val maxFiles = 20

        // Calculate if all items that were submitted were successful
        var fullSuccess = true

        val photoChunks = mutableListOf<PhotoFileChunk>()

        repositoryProvider.photoFileRepository.getAllRaw().let {

            it.forEachIndexed { _, photoRecord ->

                onProgressCallback?.onProgressChange(String.format(context.getString(R.string.ellipses_dynamic), context.getString(R.string.preparing_photos)))

                if (File(photoRecord.fileUri).exists()) {
                    photoRecord.imageBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100,
                        photoRecord.byteArrayOutputStream
                    )

                    val fileSize: Double = ((photoRecord.byteArrayOutputStream.size() / 1024.0) / 1024.0)

                    var recordInserted = false

                    photoChunks.forEach chunks@{ photoChunk ->

                        if (photoChunk.chunkSize + fileSize < maxUpload && photoChunk.photoFiles.size < maxFiles) {

                            photoChunk.chunkSize = photoChunk.chunkSize + fileSize
                            photoChunk.photoFiles.add(photoRecord)
                            recordInserted = true
                        }
                    }

                    if (!recordInserted)
                        photoChunks.add(
                            PhotoFileChunk(
                                fileSize,
                                mutableListOf(photoRecord)
                            )
                        )
                }
            }
        }

        photoChunks.forEachIndexed { index, photoChunk ->
            val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
            val multipartBodyType = "image/jpeg".toMediaTypeOrNull()

            // Create multipart to hold all images that are going to be submitted
            multipartBodyType?.let { mediaType ->
                photoChunk.photoFiles.forEach { photoFile ->

                    builder
                        .addFormDataPart(
                            "photos[]",
                            photoFile.fileName,
                            photoFile.byteArrayOutputStream.toByteArray().toRequestBody(mediaType)
                        )
                }

                onProgressCallback?.onProgressChange(
                    String.format(
                        context.getString(R.string.ellipses_dynamic),
                        String.format(
                            context.getString(R.string.uploading_photo_bundle_dynamic),
                            index + 1,
                            photoChunks.size
                        )
                    )
                )

                // Query the API to submit the images
                val response =
                    Api
                        .getInstance(context)
                        .setPhotos(builder.build().parts)
                        .execute()

                // Validate the response was successful
                if (response.isSuccessful) {

                    // Validate a response came back
                    response.body()?.let {

                        it.response.forEach { successErrorCode ->

                            when {
                                // Delete the photo file if it was submitted successfully
                                successErrorCode.success && successErrorCode.id != null -> repositoryProvider.photoFileRepository.delete(successErrorCode.id)
                                else -> fullSuccess = false
                            }
                        }
                    }
                } else
                    catchError(context, response.errorBody())
            }
        }

        if (BuildConfig.DEBUG)
            Api.setInterceptorLevel(HttpLoggingInterceptor.Level.BODY)

        return fullSuccess
    }

    /**
     * Fetches data from the web API
     */
    suspend fun fetchData(context: MainActivity, onProgressCallback: OnProgressCallback? = null): Boolean {

        onProgressCallback?.onProgressChange(String.format(context.getString(R.string.ellipses_dynamic), context.getString(R.string.downloading_updates)))

        val lastUpdated = Date()

        val response =
            Api
                .getInstance(context)
                .getData()
                .execute()

        if (response.isSuccessful) {
            response.body()?.let { appData ->

                KeyStore.getInstance(context).lastUpdated = lastUpdated

                repositoryProvider.apply {
                    logDraftRepository.disable()

                    repositoryProvider.checklistItemRepository.insertAll(appData.checklistItemList)
                    repositoryProvider.checklistItemResultRepository.insertAll(appData.checklistItemResultList)
                    repositoryProvider.dataTypeRepository.insertAll(appData.dataTypeList)
                    repositoryProvider.eventRepository.insertAll(appData.eventList)
                    repositoryProvider.eventTeamListRepository.insertAll(appData.eventTeamListList)
                    repositoryProvider.matchRepository.insertAll(appData.matchList)
                    repositoryProvider.matchTypeRepository.insertAll(appData.matchTypeList)
                    repositoryProvider.robotInfoRepository.insertAll(appData.robotInfoList)
                    repositoryProvider.robotInfoKeRepository.insertAll(appData.robotInfoKeyList)
                    repositoryProvider.robotMediaRepository.insertAll(appData.robotMediaList)
                    repositoryProvider.roleRepository.insertAll(appData.roleList)
                    repositoryProvider.scoutCardInfoRepository.insertAll(appData.scoutCardInfoList)
                    repositoryProvider.scoutCardInfoKeyRepository.insertAll(appData.scoutCardInfoKeyList)
                    repositoryProvider.scoutCardInfoKeyStateRepository.insertAll(appData.scoutCardInfoKeyStateList)
                    repositoryProvider.teamRepository.insertAll(appData.teamList)
                    repositoryProvider.teamAccountRepository.insertAll(appData.teamAccountList)
                    repositoryProvider.userRepository.insertAll(appData.userList)
                    repositoryProvider.userRoleRepository.insertAll(appData.userRoleList)
                    repositoryProvider.userTeamAccountRepository.insertAll(appData.userTeamAccountList)
                    repositoryProvider.yearRepository.insertAll(appData.yearList)

                    Account.getInstance(context).let { user ->
                        KeyStore.getInstance(context).account = user
                    }

                    robotMediaRepository.insertAll(
                        robotMediaRepository.downloadPhotos(
                            appData.robotMediaList.toMutableList(),
                            context,
                            onProgressCallback
                        )
                    )

                    logDraftRepository.enable()
                    logDeleteRepository.enable()
                }

                onProgressCallback?.onProgressChange(String.format(context.getString(R.string.ellipses_dynamic), context.getString(R.string.download_complete)))

                return true
            }
        } else
            catchError(context, response.errorBody())

        return false
    }

    suspend fun login(context: MainActivity, email: String, password: String, recaptchaTokenResult: String?): Boolean {

        val response =
            Api.getInstance(context)
                .login(
                    email,
                    password
                ).execute()

        if (response.isSuccessful) {
            response.body()?.let {

                KeyStore.getInstance(context).authToken = it.account.authToken
                KeyStore.getInstance(context).account = it.account

                it.account.initialize(context)

                return true
            }
        } else
            catchError(context, response.errorBody())

        return false
    }

    private fun catchError(context: MainActivity, errorBody: ResponseBody?) {
        errorBody?.let { error ->

            val errorString: String = error.string()

            try {

                val errorJson = JSONObject(errorString)

                when (val errorCode = errorJson.getInt("error")) {
                    HttpResponseCode.FORBIDDEN -> {
                        context.showSnackbar(context.getString(R.string.error_403), Snackbar.LENGTH_LONG)
                        AppLog.e(UserNotAuthenticatedException())
                    }

                    HttpResponseCode.PRECONDITION_FAILED -> {
                        context.showSnackbar(context.getString(R.string.error_412), Snackbar.LENGTH_LONG)
                        AppLog.e(PreconditionFailedException(Api.API_VERSION))
                    }

                    else ->
                    {
                        context.showSnackbar(context.getString(R.string.error_general), Snackbar.LENGTH_LONG)
                        AppLog.e(Exception("HTTP error code: $errorCode"))
                    }
                }
            } catch (exception: JSONException) {
                context.showSnackbar(context.getString(R.string.error_general), Snackbar.LENGTH_LONG)
                AppLog.e(Exception(errorString))
            }
        } ?: let {
            context.showSnackbar(context.getString(R.string.error_general), Snackbar.LENGTH_LONG)
            AppLog.e(Exception("ResponseBody is null!"))
        }
    }

    suspend fun sync(context: MainActivity, onAnimationCompleteCallback: OnAnimationCompleteCallback? = null): Boolean {
        val dialog =
            LoadingDialogFragment.newInstance(
                null,
                onAnimationCompleteCallback
            )

        context.runOnUiThread {
            dialog.show(context)
        }

        var fullSuccess = false

        try {
            val submitSuccess = true //submitData(context, dialog.onProgressCallback)
            val fetchSuccess = if (submitSuccess) fetchData(context, dialog.onProgressCallback) else false

            fullSuccess = submitSuccess && fetchSuccess
        } catch (e: UnknownHostException) {
            context.showSnackbar(context.getString(R.string.no_internet))
        }

        context.runOnUiThread {
            when {
                fullSuccess -> dialog.success()
                else -> dialog.failure()
            }
        }

        return fullSuccess
    }
}
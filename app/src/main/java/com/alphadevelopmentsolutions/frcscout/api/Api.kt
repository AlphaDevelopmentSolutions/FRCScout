package com.alphadevelopmentsolutions.frcscout.api

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.callbacks.OnProgressCallback
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.classes.DynamicResponse
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.extensions.toJson
import com.alphadevelopmentsolutions.frcscout.factories.PhotoFileFactory
import com.alphadevelopmentsolutions.frcscout.interfaces.Constant
import com.alphadevelopmentsolutions.frcscout.serializers.BooleanSerializer
import com.alphadevelopmentsolutions.frcscout.serializers.ByteArraySerializer
import com.alphadevelopmentsolutions.frcscout.serializers.DateSerializer
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.google.firebase.perf.FirebasePerformance
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import org.apache.commons.compress.archivers.tar.TarArchiveEntry
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream
import org.apache.commons.compress.utils.IOUtils
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

interface Api {

    @POST("api/get")
    fun getData(): Call<AppData>

    @Headers("Accept: application/json")
    @POST("api/set")
    fun setData(@Body appData: AppData): Call<ApiResponse.Set.Base>

    @Multipart
    @Headers("Accept: application/json")
    @POST("api/set/media")
    fun setPhotos(@Part multiparts: List<MultipartBody.Part>): Call<List<DynamicResponse>>

    @Headers("Accept: application/json")
    @POST("api/get/photos")
    @Streaming
    fun getPhotos(@Body photoFiles: List<RobotMedia>): Call<ResponseBody>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("api/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Call<Account>


    companion object {
        const val API_VERSION = 1
        private var retrofitInstance: Retrofit? = null
        private var okHttpInstance: OkHttpClient? = null
        private var instance: Api? = null
        private var apiUrl: String? = null
        private val loggingInterceptor by lazy {
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.HEADERS }
        }
        private val firebaseInstance by lazy {
            FirebasePerformance.getInstance()
        }

        /**
         * Creates an new [Retrofit] instance and stores it into [retrofitInstance]
         * @param context [Context] current app context
         */
        private fun getRetrofitInstance(context: Context): Retrofit {
            return retrofitInstance ?: synchronized(this) {

//                "${Constant.API_PROTOCOL}://${Constant.API_DNS}/".let { tempApiUrl ->
                "http://192.168.50.227:8080/".let { tempApiUrl ->
                    apiUrl = tempApiUrl

                    val tempInstance = Retrofit.Builder()
                        .baseUrl(tempApiUrl)
                        .addConverterFactory(
                            GsonConverterFactory.create(
                                GsonBuilder()
                                    .registerTypeAdapter(Date::class.java, DateSerializer())
                                    .registerTypeAdapter(Boolean::class.java, BooleanSerializer())
                                    .registerTypeAdapter(Boolean::class.javaObjectType, BooleanSerializer())
                                    .registerTypeAdapter(ByteArray::class.java, ByteArraySerializer())
                                    .create()
                            )
                        )
                        .client(getOkHttpInstance(context))
                        .build()

                    retrofitInstance = tempInstance
                    tempInstance
                }
            }
        }

        /**
         * Creates a new [OkHttpClient] and stores it into [okHttpInstance]
         * @param context [Context] current app context
         */
        private fun getOkHttpInstance(context: Context): OkHttpClient {
            return okHttpInstance ?: synchronized(this) {
                val tempOkHttpClient =
                    OkHttpClient.Builder().apply {
                        addInterceptor(ApiCookieInterceptor(context))
                        addInterceptor(UserAgentInterceptor(context))

                        if (!BuildConfig.isDebugMode)
                            addInterceptor(FirebasePerformanceInterceptor())

                        if (BuildConfig.isDebugMode)
                            addInterceptor(loggingInterceptor)

                        connectTimeout(60, TimeUnit.SECONDS)
                        readTimeout(3, TimeUnit.MINUTES)
                        writeTimeout(2, TimeUnit.MINUTES)
                    }.build()

                okHttpInstance = tempOkHttpClient
                tempOkHttpClient
            }
        }

        /**
         * Creates an new [Api] instance and stores it into [instance]
         * @param context [Context] current app context
         */
        fun getInstance(context: Context): Api {
            return instance ?: synchronized(this) {
                val tempInstance = getRetrofitInstance(context).create(Api::class.java)
                instance = tempInstance
                tempInstance
            }
        }

        /**
         * Destroys all instances
         */
        fun destroyInstance() {
            instance = null
            retrofitInstance = null
            okHttpInstance = null
            apiUrl = null
        }

        /**
         * Intercepts traffic and sends an API key on each call
         * @param context [Context] current app context
         */
        private class ApiCookieInterceptor(private val context: Context) : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response = chain.run {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader(
                            "Cookie",
                            StringBuilder()
                                .append("${Constant.AUTH_TOKEN}=b6c94a91-36a9-11eb-86bc-5c80b67a2786")
                                .append(";")
                                .append("${Constant.API_VERSION}=$API_VERSION")
                                .append(";")
                                .append("${Constant.LAST_UPDATED}=${KeyStore.getInstance(context).lastUpdated.toJson()}")
                                .toString()
                        )
                        .build()
                )
            }
        }

        /**
         * Intercepts traffic and adds firebase performance monitoring
         */
        private class FirebasePerformanceInterceptor : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request = chain.request()
                val trace = firebaseInstance.newHttpMetric(request.url.toUrl(), request.method)
                trace.setRequestPayloadSize(request.body?.contentLength() ?: 0L)

                trace.start()

                val response = chain.proceed(request)

                trace.setHttpResponseCode(response.code)
                trace.setResponsePayloadSize(response.body?.contentLength() ?: 0L)
                trace.stop()

                return response
            }
        }

        /**
         * Intercepts traffic and sends a custom user agent on each call
         * @param context [Context] current app context
         */
        private class UserAgentInterceptor(private val context: Context) : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response = chain.run {
                proceed(
                    request()
                        .newBuilder()
                        .addHeader(
                            "User-Agent",
                            StringBuilder()
                                .append("Android/${Build.VERSION.SDK_INT}/${Build.VERSION.RELEASE}")
                                .append(" ")
                                .append("Device/${Build.BRAND}/${Build.DEVICE}/${Build.MODEL}")
                                .append(" ")
                                .append("App/${context.packageName}/${BuildConfig.VERSION_CODE}/${BuildConfig.VERSION_NAME}/${BuildConfig.BUILD_TYPE}")
                                .toString())
                        .build()
                )
            }
        }

        /**
         * Sets the [loggingInterceptor] [HttpLoggingInterceptor.Level] per [Api] call
         * @param level [HttpLoggingInterceptor.Level] level of logging to apply to the [Api]
         */
        fun setInterceptorLevel(level: HttpLoggingInterceptor.Level) {
//            loggingInterceptor.level = level
            loggingInterceptor.level = HttpLoggingInterceptor.Level.HEADERS
        }

        /**
         * Downloads a list of images
         * @param context [AppCompatActivity] used to get the [okHttpInstance] and the default app filesdir
         * @param photos [List] of [RobotMedia] objects to download from the web
         * @param onProgressCallback [OnProgressCallback] progress callback to update
         */
        @Suppress("RedundantSuspendModifier")
        suspend fun getPhotos(context: AppCompatActivity, photos: MutableList<RobotMedia>, onProgressCallback: OnProgressCallback? = null): List<RobotMedia> {

            if (BuildConfig.DEBUG)
                setInterceptorLevel(HttpLoggingInterceptor.Level.HEADERS)

            onProgressCallback?.onProgressChange("Cleaning Up Old Photo Files...")

            // Remove all files that are not going to be downloaded
            PhotoFileFactory.files(context).forEach currentForeach@{ file ->

                var shouldFileStay = false

                photos.forEach { photo ->
                    if (photo.uri == file.name) {
                        shouldFileStay = true
                        return@currentForeach
                    }
                }

                if (!shouldFileStay)
                    file.delete()
            }

            val imagesPerChunk = 10

            val photosToDownloadChunks = mutableListOf<MutableList<RobotMedia>>()

            photos.forEachIndexed { index, photo ->

                // Attempt to retrieve the local image file
                PhotoFileFactory.getFile(
                    photo.uri,
                    context
                ).let { localImageFile ->

                    // Prev image file exists, update local file uri for that photo
                    if (localImageFile != null) {
                        photos[index] = photo.apply {
                            localFileUri = localImageFile.absolutePath
                        }
                    }

                    // Local image file does not exist, download new image
                    else {
                        var recordInserted = false

                        photosToDownloadChunks.forEach { photosToDownload ->

                            if (photosToDownload.size < imagesPerChunk) {
                                photosToDownload.add(photo)
                                recordInserted = true
                            }
                        }

                        if (!recordInserted)
                            photosToDownloadChunks.add(mutableListOf(photo))
                    }
                }
            }

            photosToDownloadChunks.forEachIndexed { index, photosToDownload ->

                onProgressCallback?.onProgressChange(
                    String.format(
                        context.getString(R.string.ellipses_dynamic),
                        String.format(
                            context.getString(R.string.downloading_photo_bundle_dynamic),
                            index + 1,
                            photosToDownloadChunks.size
                        )
                    )
                )

                getInstance(context)
                    .getPhotos(photosToDownload.toList())
                    .execute()
                    .let { responseBody ->

                        // Decompress the tar archive that was sent back
                        responseBody.body()?.let { body ->
                            try {
                                val tarArchiveInputStream = TarArchiveInputStream(body.byteStream())
                                var tarArchiveEntry: TarArchiveEntry?

                                do {
                                    tarArchiveEntry = tarArchiveInputStream.nextTarEntry

                                    tarArchiveEntry?.let { entry ->

                                        if (!entry.isDirectory) {
                                            val currentFile = File(PhotoFileFactory.getFileDir(context), entry.name)
                                            val parent = currentFile.parentFile

                                            if (parent?.exists() == false)
                                                parent.mkdirs()

                                            if (IOUtils.copy(tarArchiveInputStream, FileOutputStream(currentFile), 1024 * 1024) > 0) {
                                                photos.forEach { photo ->
                                                    if (photo.uri == entry.name)
                                                        photo.localFileUri = currentFile.absolutePath
                                                }
                                            }
                                        }
                                    }
                                } while (tarArchiveEntry != null)
                            } catch (e: IOException) {}
                        }
                    }
            }

            if (BuildConfig.DEBUG)
                setInterceptorLevel(HttpLoggingInterceptor.Level.BODY)

            return photos.toList()
        }
    }
}
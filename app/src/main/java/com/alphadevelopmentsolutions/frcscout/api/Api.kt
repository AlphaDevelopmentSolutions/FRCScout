package com.alphadevelopmentsolutions.frcscout.api

import android.content.Context
import android.os.Build
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.serializers.BooleanSerializer
import com.alphadevelopmentsolutions.frcscout.serializers.ByteArraySerializer
import com.alphadevelopmentsolutions.frcscout.serializers.DateSerializer
import com.google.firebase.perf.FirebasePerformance
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

interface Api {

    companion object {
        const val API_VERSION = 1
        private var retrofitInstance: Retrofit? = null
        private var okHttpInstance: OkHttpClient? = null
        private var instance: Api? = null
        private var apiUrl: String? = null
        private val loggingInterceptor by lazy {
            HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
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

                "${Constants.API_PROTOCOL}://${Constants.API_DNS}/".let { tempApiUrl ->
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
                                .append("${Constants.AUTH_TOKEN}=TOKEN")
                                .append(";")
                                .append("${Constants.API_VERSION}=$API_VERSION")
                                .append(";")
                                .append("${Constants.LAST_UPDATED}=LASTUPDATED")
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
            loggingInterceptor.level = level
        }
    }
}
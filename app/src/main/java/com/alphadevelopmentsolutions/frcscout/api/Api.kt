package com.alphadevelopmentsolutions.frcscout.api

import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST

interface Api {

    @POST("get")
    fun getData(): Call<AppData>

    @POST("hello")
    fun connect(): Call<ApiResponse.Connect>

    companion object {
        private var retrofitInstance: Retrofit? = null
        private var instance: Api? = null

        /**
         * Creates an new [Retrofit] instance and stores it into [retrofitInstance]
         */
        private fun getRetrofitInstance(): Retrofit {
            return retrofitInstance ?: synchronized(this) {
                val tempInstance =  Retrofit.Builder()
                        .baseUrl(Constants.API_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()

                retrofitInstance = tempInstance
                tempInstance
            }
        }

        /**
         * Creates an new [Api] instance and stores it into [instance]
         */
        fun getInstance(): Api {
            return instance ?: synchronized(this) {
                val tempInstance =  getRetrofitInstance().create(Api::class.java)
                instance = tempInstance
                tempInstance
            }
        }
    }

}
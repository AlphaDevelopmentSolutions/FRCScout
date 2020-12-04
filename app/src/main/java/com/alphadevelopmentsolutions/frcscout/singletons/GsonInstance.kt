package com.alphadevelopmentsolutions.frcscout.singletons

import com.alphadevelopmentsolutions.frcscout.serializers.ByteArraySerializer
import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonInstance {
    private var INSTANCE: Gson? = null

    fun getInstance(): Gson =
        INSTANCE ?: synchronized(this) {
            val tempInstance =
                GsonBuilder()
                    .registerTypeAdapter(ByteArray::class.java, ByteArraySerializer())
                    .serializeNulls()
                    .disableHtmlEscaping()
                    .create()

            INSTANCE = tempInstance
            tempInstance
        }
}
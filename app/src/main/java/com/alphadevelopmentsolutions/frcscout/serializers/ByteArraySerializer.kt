package com.alphadevelopmentsolutions.frcscout.serializers

import android.util.Base64
import com.google.gson.*
import java.lang.reflect.Type

class ByteArraySerializer : JsonDeserializer<ByteArray>, JsonSerializer<ByteArray> {

    /**
     * Serializes data for usage on the api
     */
    override fun serialize(
        src: ByteArray,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(Base64.encodeToString(src, Base64.NO_WRAP))
    }

    /**
     * Deserializes data from api
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): ByteArray {

        val byteString = json.asString

        return Base64.decode(byteString, Base64.DEFAULT)
    }
}
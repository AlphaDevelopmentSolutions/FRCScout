package com.alphadevelopmentsolutions.frcscout.serializers

import com.google.gson.*
import java.lang.reflect.Type

class BooleanSerializer : JsonDeserializer<Boolean>, JsonSerializer<Boolean> {

    /**
     * Serializes data for usage on the api
     */
    override fun serialize(
        src: Boolean,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(if (src) 1 else 0)
    }

    /**
     * Deserializes data from api
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Boolean {

        return try {
            json.asInt == 1
        } catch (e: NumberFormatException) {
            json.asBoolean
        }
    }
}

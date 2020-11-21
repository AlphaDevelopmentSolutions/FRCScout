package com.alphadevelopmentsolutions.frcscout.serializers

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateSerializer : JsonDeserializer<Date>, JsonSerializer<Date> {

    /**
     * Serializes data for usage on the api
     */
    override fun serialize(
        src: Date,
        typeOfSrc: Type,
        context: JsonSerializationContext
    ): JsonElement {
        return context.serialize(src.toJson())
    }

    private val dateFormatter = SimpleDateFormat()
    private val dateFormats = arrayOf(
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd H:mm:ss",
        "yyyy-MM-dd",
        "yyyy-MM-dd'T'HH:mm:ssZ",
        "yyyy-MM-dd'T'HH:mm:ss",
        "yyyy-MM-dd",
        "EEE MMM dd HH:mm:ss z yyyy",
        "HH:mm:ss",
        "MM/dd/yyyy HH:mm:ss aaa",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS",
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS'Z'",
        "MMM d',' yyyy H:mm:ss a"
    )

    /**
     * Deserializes data from api
     */
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Date {
        for (dateFormat in dateFormats) {
            try {

                dateFormatter.applyPattern(dateFormat)
                dateFormatter.timeZone = TimeZone.getTimeZone("UTC")

                dateFormatter.parse(json.asString)?.let { utcDate ->
                    dateFormatter.timeZone = TimeZone.getDefault()
                    val dateString = dateFormatter.format(utcDate)

                    dateFormatter.parse(dateString)?.let {
                        return it
                    }
                }

                throw ParseException("", 0)
            } catch (e: ParseException) { }
        }

        throw JsonParseException("Cannot parse date: ${json.asString}")
    }
}

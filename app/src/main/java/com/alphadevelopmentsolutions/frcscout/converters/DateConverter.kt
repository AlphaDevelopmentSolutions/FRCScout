package com.alphadevelopmentsolutions.frcscout.converters

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    /**
     * Converts a [Date] to the [Long] version
     * @param date [Date] to be converted
     * @return [Long] version of the supplied [Date], or null if null is supplied
     */
    @TypeConverter
    fun toLong(date: Date?): Long? = date?.time

    /**
     * Converts a [Long] to the [Date] version
     * @param date [Long] to be converted
     * @return [Date] version of the supplied [Long], or null if null is supplied
     */
    @TypeConverter
    fun fromLong(date: Long?): Date? = if (date != null) Date(date) else null
}
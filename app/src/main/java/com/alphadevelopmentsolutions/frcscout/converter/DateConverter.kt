package com.alphadevelopmentsolutions.frcscout.converter

import androidx.room.TypeConverter
import java.util.*

class DateConverter {

    @TypeConverter
    fun dateToLong(date: Date) = date.time

    @TypeConverter
    fun dateFromLong(date: Long) = Date(date)
}
package com.alphadevelopmentsolutions.frcscout.converter

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.classes.FormattableDate
import java.util.*

class DateConverter {

    @TypeConverter
    fun dateToLong(date: FormattableDate) = date.time

    @TypeConverter
    fun dateFromLong(date: Long) = FormattableDate(date)
}
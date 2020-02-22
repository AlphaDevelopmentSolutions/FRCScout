package com.alphadevelopmentsolutions.frcscout.converter

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.classes.table.ScoutCardInfoKey

class DataTypeConverter {

    @TypeConverter
    fun dataTyoeToString(dataType: ScoutCardInfoKey.DataType) = dataType.toString()

    @TypeConverter
    fun dataTyoeFromString(dataType: String) = ScoutCardInfoKey.DataType.fromString(dataType)
}
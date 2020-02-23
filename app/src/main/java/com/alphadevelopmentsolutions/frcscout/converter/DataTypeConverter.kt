package com.alphadevelopmentsolutions.frcscout.converter

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.enums.DataType

class DataTypeConverter {

    @TypeConverter
    fun dataTyoeToString(dataType: DataType) = dataType.toString()

    @TypeConverter
    fun dataTyoeFromString(dataType: String) = DataType.fromString(dataType)
}
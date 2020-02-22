package com.alphadevelopmentsolutions.frcscout.converter

import androidx.room.TypeConverter
import java.util.*

class UUIDConverter {

    @TypeConverter
    fun uuidToString(uuid: UUID) = uuid.toString()

    @TypeConverter
    fun uuidFromString(uuid: String) = UUID.fromString(uuid)
}
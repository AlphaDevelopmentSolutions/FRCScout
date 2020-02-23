package com.alphadevelopmentsolutions.frcscout.converter

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.enums.MatchType
import com.alphadevelopmentsolutions.frcscout.enums.Status

class StatusConverter {

    @TypeConverter
    fun statusToString(status: Status) = status.toString()

    @TypeConverter
    fun statusFromString(status: String) = Status.fromString(status)
}
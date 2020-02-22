package com.alphadevelopmentsolutions.frcscout.converter

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.classes.table.Match

class MatchTypeConverter {

    @TypeConverter
    fun matchTypeToString(matchType: Match.Type) = matchType.toString()

    @TypeConverter
    fun matchTypeFromString(matchType: String) = Match.Type.fromString(matchType)
}
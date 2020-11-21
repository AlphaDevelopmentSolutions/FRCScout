package com.alphadevelopmentsolutions.frcscout.converters

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount
import com.alphadevelopmentsolutions.frcscout.enums.ChecklistStatus
import java.util.*

class ChecklistStatusConverter {

    /**
     * Converts a [ChecklistStatus] to the [String] version
     * @param status [ChecklistStatus] to be converted
     * @return [String] version of the supplied [ChecklistStatus]
     */
    @TypeConverter
    fun toString(status: ChecklistStatus?): String? = status?.name

    /**
     * Converts a [String] to the [ChecklistStatus] version
     * @param status [String] to be converted
     * @return [ChecklistStatus] version of the supplied [String]
     */
    @TypeConverter
    fun fromString(status: String?): ChecklistStatus? = ChecklistStatus.fromString(status)
}
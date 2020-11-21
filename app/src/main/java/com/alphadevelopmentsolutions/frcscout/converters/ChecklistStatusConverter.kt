package com.alphadevelopmentsolutions.frcscout.converters

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItemResult

class ChecklistStatusConverter {

    /**
     * Converts a [ChecklistItemResult.Status] to the [String] version
     * @param status [ChecklistItemResult.Status] to be converted
     * @return [String] version of the supplied [ChecklistItemResult.Status]
     */
    @TypeConverter
    fun toString(status: ChecklistItemResult.Status?): String? = status?.name

    /**
     * Converts a [String] to the [ChecklistItemResult.Status] version
     * @param status [String] to be converted
     * @return [ChecklistItemResult.Status] version of the supplied [String]
     */
    @TypeConverter
    fun fromString(status: String?): ChecklistItemResult.Status? = ChecklistItemResult.Status.fromString(status)
}
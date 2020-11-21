package com.alphadevelopmentsolutions.frcscout.converters

import androidx.room.TypeConverter
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount

class ItemStateConverter {

    /**
     * Converts a [UserTeamAccount.ItemState] to the [String] version
     * @param itemState [UserTeamAccount.ItemState] to be converted
     * @return [String] version of the supplied [UserTeamAccount.ItemState]
     */
    @TypeConverter
    fun toString(itemState: UserTeamAccount.ItemState?): String? = itemState?.name

    /**
     * Converts a [String] to the [UserTeamAccount.ItemState] version
     * @param itemState [String] to be converted
     * @return [UserTeamAccount.ItemState] version of the supplied [String]
     */
    @TypeConverter
    fun fromString(itemState: String?): UserTeamAccount.ItemState? = UserTeamAccount.ItemState.fromString(itemState)
}
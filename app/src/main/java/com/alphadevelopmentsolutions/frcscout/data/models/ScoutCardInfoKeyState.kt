package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.SCOUT_CARD_INFO_KEY_STATE,
    inheritSuperIndices = true
)
class ScoutCardInfoKeyState(
    @SerializedName("team_account_id") @ColumnInfo(name = "team_account_id", index = true) var teamAccountId: ByteArray,
    @SerializedName("year_id") @ColumnInfo(name = "year_id", index = true) var yearId: ByteArray,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("description") @ColumnInfo(name = "description") var description: String? = null
) : Table() {

    companion object : StaticTable<ScoutCardInfoKeyState> {
        override fun create(): ScoutCardInfoKeyState =
            ScoutCardInfoKeyState(
                ByteArray(0),
                ByteArray(0),
                "",
                null
            )
    }

    override fun toString(): String =
        name
}

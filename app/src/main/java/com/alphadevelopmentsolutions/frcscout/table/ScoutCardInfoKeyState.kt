package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = TableName.SCOUT_CARD_INFO_KEY_STATE,
    inheritSuperIndices = true
)
class ScoutCardInfoKeyState(
    @SerializedName("team_account_id") @ColumnInfo(name = "team_account_id", index = true) var teamAccountId: ByteArray,
    @SerializedName("year_id") @ColumnInfo(name = "year_id", index = true) var yearId: ByteArray,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("description") @ColumnInfo(name = "description") var description: String? = null
) : SubmittableTable(null, null, ByteArray(0)) {

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

package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.SCOUT_CARD_INFO,
    inheritSuperIndices = true
)
class ScoutCardInfo(
    @SerializedName("match_id") @ColumnInfo(name = "match_id", index = true) var matchId: ByteArray,
    @SerializedName("team_id") @ColumnInfo(name = "team_id", index = true) var teamId: ByteArray,
    @SerializedName("key_id") @ColumnInfo(name = "key_id", index = true) var keyId: ByteArray,
    @SerializedName("value") @ColumnInfo(name = "value") var value: String,
    @SerializedName("completed_by_id") @ColumnInfo(name = "completed_by_id", index = true) var completedById: ByteArray,
    @SerializedName("is_public") @ColumnInfo(name = "is_public", defaultValue = "0") var isPublic: Boolean = false
) : SubmittableTable(null, null, ByteArray(0)) {

    companion object : StaticTable<ScoutCardInfo> {
        override fun create(): ScoutCardInfo =
            ScoutCardInfo(
                ByteArray(0),
                ByteArray(0),
                ByteArray(0),
                "",
                ByteArray(0),
                false
            )
    }

    override fun toString(): String =
        value
}
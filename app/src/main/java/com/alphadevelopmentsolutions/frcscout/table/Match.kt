package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

@Entity(
    tableName = TableName.MATCH,
    inheritSuperIndices = true
)
class Match(
    @SerializedName("event_id") @ColumnInfo(name = "event_id", index = true) var eventId: ByteArray,
    @SerializedName("key") @ColumnInfo(name = "key") var key: String,
    @SerializedName("type_id") @ColumnInfo(name = "type_id", index = true) var typeId: ByteArray,
    @SerializedName("set_number") @ColumnInfo(name = "set_number") var setNumber: Int,
    @SerializedName("match_number") @ColumnInfo(name = "match_number") var matchNumber: Int,
    @SerializedName("blue_alliance_team_one_id") @ColumnInfo(name = "blue_alliance_team_one_id", index = true) var blueAllianceTeamOneId: ByteArray,
    @SerializedName("blue_alliance_team_two_id") @ColumnInfo(name = "blue_alliance_team_two_id", index = true) var blueAllianceTeamTwoId: ByteArray,
    @SerializedName("blue_alliance_team_three_id") @ColumnInfo(name = "blue_alliance_team_three_id", index = true) var blueAllianceTeamThreeId: ByteArray,
    @SerializedName("red_alliance_team_one_id") @ColumnInfo(name = "red_alliance_team_one_id", index = true) var redAllianceTeamOneId: ByteArray,
    @SerializedName("red_alliance_team_two_id") @ColumnInfo(name = "red_alliance_team_two_id", index = true) var redAllianceTeamTwoId: ByteArray,
    @SerializedName("red_alliance_team_three_id") @ColumnInfo(name = "red_alliance_team_three_id", index = true) var redAllianceTeamThreeId: ByteArray,
    @SerializedName("blue_alliance_score") @ColumnInfo(name = "blue_alliance_score") var blueAllianceScore: Int? = null,
    @SerializedName("red_alliance_score") @ColumnInfo(name = "red_alliance_score") var redAllianceScore: Int? = null,
    @SerializedName("time") @ColumnInfo(name = "time") var time: Date? = null
) : Table() {

    companion object : StaticTable<Match> {
        override fun create(): Match =
            Match(
                ByteArray(0),
                "",
                ByteArray(0),
                -1,
                -1,
                ByteArray(0),
                ByteArray(0),
                ByteArray(0),
                ByteArray(0),
                ByteArray(0),
                ByteArray(0),
                null,
                null,
                null
            )
    }

    override fun toString(): String =
        key
}
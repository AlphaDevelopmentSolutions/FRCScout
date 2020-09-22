package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

@Entity(
    tableName = TableName.MATCH,
    inheritSuperIndices = true
)
class Match(
        var eventId: ByteArray,
        var key: String,
        var typeId: ByteArray,
        var setNumber: Int,
        var matchNumber: Int,
        var blueAllianceTeamOneId: ByteArray,
        var blueAllianceTeamTwoId: ByteArray,
        var blueAllianceTeamThreeId: ByteArray,
        var redAllianceTeamOneId: ByteArray,
        var redAllianceTeamTwoId: ByteArray,
        var redAllianceTeamThreeId: ByteArray,
        var blueAllianceScore: Int? = null,
        var redAllianceScore: Int? = null,
        var time: Date? = null,
        var lastModifiedDate: Date = Date(),
        var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        key
}
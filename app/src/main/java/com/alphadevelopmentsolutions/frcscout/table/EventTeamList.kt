package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.EVENT_TEAM_LIST,
    inheritSuperIndices = true
)
class EventTeamList(
    var teamId: ByteArray,
    var eventId: ByteArray
) : Table() {
    override fun toString(): String =
        ""
}

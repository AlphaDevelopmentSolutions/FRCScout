package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.ROBOT_INFO,
    inheritSuperIndices = true
)
class RobotInfo(
    var eventId: ByteArray,
    var teamId: ByteArray,
    var keyId: ByteArray,
    var value: String,
    var completedById: ByteArray,
    var isPublic: Boolean = false,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        value
}
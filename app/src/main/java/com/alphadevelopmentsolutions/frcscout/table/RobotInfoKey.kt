package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.ROBOT_INFO_KEY,
    inheritSuperIndices = true
)
class RobotInfoKey(
    var stateId: ByteArray,
    var dataTypeId: ByteArray,
    var name: String,
    var description: String,
    var order: Int,
    var min: Int? = null,
    var max: Int? = null,
    var nullZeros: Boolean? = null,
    var includeInReports: Boolean? = null,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        name
}
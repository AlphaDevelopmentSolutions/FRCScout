package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.DATA_TYPE,
    inheritSuperIndices = true
)
class DataTypes(
    var name: String,
    var canMax: Boolean,
    var canMin: Boolean,
    var canNullZeros: Boolean,
    var canReport: Boolean,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        ""
}
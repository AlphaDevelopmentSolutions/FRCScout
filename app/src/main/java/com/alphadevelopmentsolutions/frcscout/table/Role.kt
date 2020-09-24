package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.ROLE,
    inheritSuperIndices = true
)
class Role(
    var name: String,
    var description: String?,
    var canManageTeam: Boolean,
    var canManageUsers: Boolean,
    var canScout: Boolean,
    var canCaptureMedia: Boolean,
    var canManageReports: Boolean,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        ""
}
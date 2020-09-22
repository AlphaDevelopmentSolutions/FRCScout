package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.enums.ChecklistStatus
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.text.SimpleDateFormat
import java.util.*

@Entity(
    tableName = TableName.CHECKLIST_ITEM_RESULT,
    inheritSuperIndices = true
)
class ChecklistItemResult(
    var checklistItemId: ByteArray,
    var matchId: ByteArray,
    var status: ChecklistStatus,
    var completedDate: Date,
    var completedById: ByteArray,
    var isPublic: Boolean = false,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        status.toString()
}
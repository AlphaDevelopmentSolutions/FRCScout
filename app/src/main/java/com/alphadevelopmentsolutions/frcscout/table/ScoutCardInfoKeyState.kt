package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.SCOUT_CARD_INFO_KEY_STATE,
    inheritSuperIndices = true
)
class ScoutCardInfoKeyState(
    var teamAccountId: ByteArray,
    var yearId: ByteArray,
    var name: String,
    var description: String,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        name
}

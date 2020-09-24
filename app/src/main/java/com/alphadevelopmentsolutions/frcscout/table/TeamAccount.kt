package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.TEAM_ACCOUNT,
    inheritSuperIndices = true
)
class TeamAccount(
    var teamId: ByteArray,
    var name: String,
    var description: String,
    var username: String,
    var ownerId: ByteArray,
    var avatarUri: String,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        name
}

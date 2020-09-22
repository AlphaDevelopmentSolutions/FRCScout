package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.USER,
    inheritSuperIndices = true
)
class User(
    var firstName: String,
    var lastName: String,
    var email: String,
    var username: String,
    var password: String,
    var description: String,
    var avatarUri: String,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        "$firstName $lastName"
}

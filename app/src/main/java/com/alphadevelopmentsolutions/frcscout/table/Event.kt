package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*
import kotlin.math.round

@Entity(
    tableName = TableName.EVENT,
    inheritSuperIndices = true
)
class Event(
    var yearId: ByteArray,
    var code: String,
    var key: String,
    var venue: String,
    var name: String,
    var address: String,
    var city: String? = null,
    var stateProvince: String? = null,
    var country: String? = null,
    var startTime: Date? = null,
    var endTime: Date? = null,
    var websiteUrl: String? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        name
}
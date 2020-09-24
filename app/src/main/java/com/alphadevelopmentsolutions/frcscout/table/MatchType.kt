package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

@Entity(
    tableName = TableName.MATCH_TYPE,
    inheritSuperIndices = true
)
class MatchType(
        var key: String,
        var name: String
) : Table() {
    override fun toString(): String =
        key
}
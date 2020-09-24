package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.CONFIG,
    inheritSuperIndices = true
)
class Config(
    var primaryColor: String,
    var primaryColorDark: String,
    var primaryColorLight: String,
    var primaryColorAccent: String
) : Table() {
    override fun toString(): String =
        ""
}
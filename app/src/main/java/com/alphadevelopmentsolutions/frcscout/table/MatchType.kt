package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.round

@Entity(
    tableName = TableName.MATCH_TYPE,
    inheritSuperIndices = true
)
class MatchType(
    @SerializedName("key") @ColumnInfo(name = "key") var key: String,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String
) : Table() {

    companion object : StaticTable<MatchType> {
        override fun create(): MatchType =
            MatchType(
                "",
                ""
            )
    }

    override fun toString(): String =
        key
}
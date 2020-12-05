package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName

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
        name
}
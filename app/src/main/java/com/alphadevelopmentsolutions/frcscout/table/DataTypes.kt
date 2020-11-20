package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = TableName.DATA_TYPE,
    inheritSuperIndices = true
)
class DataTypes(
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("can_max") @ColumnInfo(name = "can_max") var canMax: Boolean,
    @SerializedName("can_min") @ColumnInfo(name = "can_min") var canMin: Boolean,
    @SerializedName("can_null_zeros") @ColumnInfo(name = "can_null_zeros") var canNullZeros: Boolean,
    @SerializedName("can_report") @ColumnInfo(name = "can_report") var canReport: Boolean
) : SubmittableTable(null, null, ByteArray(0)) {

    companion object : StaticTable<DataTypes> {
        override fun create(): DataTypes =
            DataTypes(
                "",
                canMax = false,
                canMin = false,
                canNullZeros = false,
                canReport = false
            )
    }

    override fun toString(): String =
        name
}
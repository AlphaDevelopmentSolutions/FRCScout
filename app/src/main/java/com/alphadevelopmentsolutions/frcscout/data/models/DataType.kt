package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.DATA_TYPE,
    inheritSuperIndices = true
)
class DataType(
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("backend_name") @ColumnInfo(name = "backend_name") var backendName: String,
    @SerializedName("can_max") @ColumnInfo(name = "can_max") var canMax: Boolean,
    @SerializedName("can_min") @ColumnInfo(name = "can_min") var canMin: Boolean,
    @SerializedName("can_null_zeros") @ColumnInfo(name = "can_null_zeros") var canNullZeros: Boolean,
    @SerializedName("can_report") @ColumnInfo(name = "can_report") var canReport: Boolean
) : Table() {

    val backendDataType: BackendDataType
        get() =
            BackendDataType.fromString(backendName)

    companion object : StaticTable<DataType> {
        override fun create(): DataType =
            DataType(
                "",
                "",
                canMax = false,
                canMin = false,
                canNullZeros = false,
                canReport = false
            )
    }

    override fun toString(): String =
        name

    enum class BackendDataType {
        INTEGER,
        BOOLEAN,
        STRING;

        companion object {
            fun fromString(backendName: String) =
                valueOf(backendName.toUpperCase())
        }
    }
}
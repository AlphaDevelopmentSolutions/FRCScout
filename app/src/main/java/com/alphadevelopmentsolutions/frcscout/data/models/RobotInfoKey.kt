package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.table.StaticTable
import com.alphadevelopmentsolutions.frcscout.table.SubmittableTable
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.ROBOT_INFO_KEY,
    inheritSuperIndices = true
)
class RobotInfoKey(
    @SerializedName("state_id") @ColumnInfo(name = "state_id", index = true) var stateId: ByteArray,
    @SerializedName("data_type_id") @ColumnInfo(name = "data_type_id", index = true) var dataTypeId: ByteArray,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("description") @ColumnInfo(name = "description") var description: String? = null,
    @SerializedName("order") @ColumnInfo(name = "order") var order: Int,
    @SerializedName("min") @ColumnInfo(name = "min") var min: Int? = null,
    @SerializedName("max") @ColumnInfo(name = "max") var max: Int? = null,
    @SerializedName("null_zeros") @ColumnInfo(name = "null_zeros") var nullZeros: Boolean? = null,
    @SerializedName("include_in_reports") @ColumnInfo(name = "include_in_reports") var includeInReports: Boolean? = null
) : SubmittableTable(null, null, ByteArray(0)) {

    companion object : StaticTable<RobotInfoKey> {
        override fun create(): RobotInfoKey =
            RobotInfoKey(
                ByteArray(0),
                ByteArray(0),
                "",
                null,
                -1,
                null,
                null,
                null,
                null
            )
    }

    override fun toString(): String =
        name
}
package com.alphadevelopmentsolutions.frcscout.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.math.round

@Entity(
    tableName = TableName.EVENT,
    inheritSuperIndices = true
)
class Event(
    @SerializedName("year_id") @ColumnInfo(name = "year_id", index = true) var yearId: ByteArray,
    @SerializedName("code") @ColumnInfo(name = "code") var code: String,
    @SerializedName("key") @ColumnInfo(name = "key") var key: String,
    @SerializedName("venue") @ColumnInfo(name = "venue") var venue: String,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("address") @ColumnInfo(name = "address") var address: String,
    @SerializedName("city") @ColumnInfo(name = "city") var city: String? = null,
    @SerializedName("state_province") @ColumnInfo(name = "state_province") var stateProvince: String? = null,
    @SerializedName("country") @ColumnInfo(name = "country") var country: String? = null,
    @SerializedName("start_time") @ColumnInfo(name = "start_time") var startTime: Date? = null,
    @SerializedName("end_time") @ColumnInfo(name = "end_time") var endTime: Date? = null,
    @SerializedName("website_url") @ColumnInfo(name = "website_url") var websiteUrl: String? = null
) : Table() {

    companion object : StaticTable<Event> {
        override fun create(): Event =
            Event(
                ByteArray(0),
                "",
                "",
                "",
                "",
                ""
            )
    }

    override fun toString(): String =
        name
}
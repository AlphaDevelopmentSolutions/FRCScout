package com.alphadevelopmentsolutions.frcscout.table

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = TableName.YEAR,
    inheritSuperIndices = true
)
class Year(
    @SerializedName("number") @ColumnInfo(name = "number") var number: Int,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("start_date") @ColumnInfo(name = "start_date") var startDate: Date? = null,
    @SerializedName("end_date") @ColumnInfo(name = "end_date") var endDate: Date? = null,
    @SerializedName("image_uri") @ColumnInfo(name = "image_uri") var imageUri: String? = null
) : Table() {

    companion object : StaticTable<Year> {
        override fun create(): Year =
            Year(
                -1,
                ""
            )
    }

    override fun toString(): String =
        name
}

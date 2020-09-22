package com.alphadevelopmentsolutions.frcscout.table

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(
    tableName = TableName.YEAR,
    inheritSuperIndices = true
)
class Year(
    var number: Int,
    var name: String,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var imageUri: String? = null,
    var lastModified: Date = Date(),
    var modifiedById: ByteArray,
) : Table() {
    override fun toString(): String =
        name
}

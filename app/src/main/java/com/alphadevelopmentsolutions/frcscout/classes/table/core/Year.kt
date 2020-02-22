package com.alphadevelopmentsolutions.frcscout.classes.table.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.util.*

@Entity(tableName = TableName.YEAR)
class Year(
        var number: Int,
        var name: String,
        var startDate: Date? = null,
        var endDate: Date? = null,
        var imageUri: String? = null
) : Table()
{
    /**
     * Gets a bitmap version of the object
     * @return bitmap version of object
     */
    val imageBitmap: Bitmap
        get() = BitmapFactory.decodeFile(this.imageUri)


    /**
     * @see Table.toString
     */
    override fun toString() = "$number - $name"
}

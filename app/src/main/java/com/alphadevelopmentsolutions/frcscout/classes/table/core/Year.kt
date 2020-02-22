package com.alphadevelopmentsolutions.frcscout.classes.table.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import java.util.*

@Entity(tableName = "years")
class Year(
        var name: String = DEFAULT_STRING,
        var startDate: Date = DEFAULT_DATE,
        var endDate: Date = DEFAULT_DATE,
        var imageUri: String = DEFAULT_STRING) : Table()
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
    override fun toString(): String
    {
        return "$serverId - $name"
    }
}

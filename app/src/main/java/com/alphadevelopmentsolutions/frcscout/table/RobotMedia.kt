package com.alphadevelopmentsolutions.frcscout.table

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

@Entity(
    tableName = TableName.ROBOT_MEDIA,
    inheritSuperIndices = true
)
class RobotMedia(
    var teamAccountId: ByteArray,
    var eventId: ByteArray,
    var teamId: ByteArray,
    var createdById: ByteArray,
    var uri: String,
    var isPublic: Boolean = false,
    var deletedDate: Date? = null,
    var deletedBy: Date? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        uri
}
package com.alphadevelopmentsolutions.frcscout.table

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

@Entity(
    tableName = TableName.ROBOT_MEDIA,
    inheritSuperIndices = true
)
class RobotMedia(
    @SerializedName("team_account_id") @ColumnInfo(name = "team_account_id", index = true) var teamAccountId: ByteArray,
    @SerializedName("event_id") @ColumnInfo(name = "event_id", index = true) var eventId: ByteArray,
    @SerializedName("team_id") @ColumnInfo(name = "team_id", index = true) var teamId: ByteArray,
    @SerializedName("created_by_id") @ColumnInfo(name = "created_by_id", index = true) var createdById: ByteArray,
    @SerializedName("uri") @ColumnInfo(name = "uri") var uri: String,
    @SerializedName("is_public") @ColumnInfo(name = "is_public", defaultValue = "0") var isPublic: Boolean = false
) : SubmittableTable(null, null, ByteArray(0)) {

    companion object : StaticTable<RobotMedia> {
        override fun create(): RobotMedia =
            RobotMedia(
                ByteArray(0),
                ByteArray(0),
                ByteArray(0),
                ByteArray(0),
                "",
                false
            )
    }

    override fun toString(): String =
        uri
}
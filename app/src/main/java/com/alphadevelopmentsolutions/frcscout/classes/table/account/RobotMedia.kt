package com.alphadevelopmentsolutions.frcscout.classes.table.account

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

@Entity(tableName = TableName.ROBOT_MEDIA)
class RobotMedia(
        @SerializedName("event_id") var eventId: UUID,
        @SerializedName("team_id") var teamId: UUID,
        var uri: String
) : Table()
{
    /**
     * Gets a bitmap version of the robot image
     * @return bitmap version of robot image
     */
    val imageBitmap: Bitmap
        get()
        {
            return with(BitmapFactory.decodeFile(uri))
            {
                when (ExifInterface(uri).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED))
                {

                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90f)

                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180f)

                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270f)

                    ExifInterface.ORIENTATION_NORMAL -> rotateImage(this, 0f)

                    else -> rotateImage(this, 0f)
                }
            }
        }

    /**
     * Rotates the image specified by [angle]
     * @param bitmap [Bitmap] to rotate
     * @param angle [Float] angle to rotate to
     * @return [Bitmap] rotated to specified angle
     */
    private fun rotateImage(bitmap: Bitmap, angle: Float): Bitmap
    {
        return with(bitmap)
        {
            Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(angle) }, true)
        }

    }

    /**
     * @see Table.toString
     */
    override fun toString() = uri
}

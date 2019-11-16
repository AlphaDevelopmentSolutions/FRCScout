package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import androidx.room.Entity
import java.io.ByteArrayOutputStream
import java.io.File

@Entity(tableName = "robot_media")
class RobotMedia(
        var id: Int = DEFAULT_INT,
        var yearId: Int = DEFAULT_INT,
        var eventId: String = DEFAULT_STRING,
        var teamId: Int = DEFAULT_INT,
        var fileUri: String = DEFAULT_STRING,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table()
{
    /**
     * Converts the current robot media into base64 format for server submission
     * @return base64 bitmap image
     */
    val base64Image: String?
        get()
        {
            val robotMedia = File(fileUri)

            if (robotMedia.exists())
            {
                val byteArrayOutputStream = ByteArrayOutputStream()
                this.imageBitmap.compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream)
                val robotMediaBytes = byteArrayOutputStream.toByteArray()

                return Base64.encodeToString(robotMediaBytes, Base64.DEFAULT)
            }


            return null
        }

    /**
     * Gets a bitmap version of the robot image
     * @return bitmap version of robot image
     */
    val imageBitmap: Bitmap
        get()
        {
            return with(BitmapFactory.decodeFile(fileUri))
            {
                when (ExifInterface(fileUri).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED))
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
    override fun toString(): String
    {
        return "Team $teamId - Robot Media"
    }
}

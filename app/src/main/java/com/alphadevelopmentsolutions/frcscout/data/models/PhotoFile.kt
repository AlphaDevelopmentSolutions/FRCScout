package com.alphadevelopmentsolutions.frcscout.data.models

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.io.ByteArrayOutputStream

@Entity(
    tableName = TableName.PHOTO_FILE,
    inheritSuperIndices = true
)
class PhotoFile(
    @PrimaryKey @SerializedName("photo_id") @ColumnInfo(name = "photo_id", index = true) var photoId: ByteArray,
    @SerializedName("file_name") @ColumnInfo(name = "file_name") var fileName: String = "",
    @SerializedName("file_uri") @ColumnInfo(name = "file_uri") var fileUri: String = ""
) {

    @Ignore
    @Transient
    var byteArrayOutputStream: ByteArrayOutputStream = ByteArrayOutputStream()

    /**
     * @see Table.toString
     */
    override fun toString() = fileUri

    /**
     * Gets a [Bitmap] version of the photo
     * @return [Bitmap] version of photo
     */
    val imageBitmap: Bitmap
        get() {
            return with(BitmapFactory.decodeFile(fileUri)) {
                when (ExifInterface(fileUri).getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED)) {
                    ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(this, 90f)

                    ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(this, 180f)

                    ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(this, 270f)

                    else -> this
                }
            }
        }

    /**
     * Rotates the image specified by [angle]
     * @param bitmap [Bitmap] to rotate
     * @param angle [Float] angle to rotate to
     * @return [Bitmap] rotated to specified angle
     */
    private fun rotateImage(bitmap: Bitmap, angle: Float): Bitmap {
        return with(bitmap) {
            Bitmap.createBitmap(this, 0, 0, width, height, Matrix().apply { postRotate(angle) }, true)
        }
    }
}
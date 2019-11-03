package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.util.Base64
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class RobotMedia(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var yearId: Long = DEFAULT_LONG,
        var eventId: String = DEFAULT_STRING,
        var teamId: Long = DEFAULT_LONG,
        var fileUri: String = DEFAULT_STRING,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "robot_media"
        const val COLUMN_NAME_YEAR_ID = "YearId"
        const val COLUMN_NAME_EVENT_ID = "EventId"
        const val COLUMN_NAME_TEAM_ID = "TeamId"
        const val COLUMN_NAME_FILE_URI = "FileURI"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_YEAR_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_EVENT_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_TEAM_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_FILE_URI, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_IS_DRAFT, SQLiteDataTypes.INTEGER))
            }

        /**
         * Returns [ArrayList] of [RobotMedia] with specified filters from [database]
         * @param robotMedia if specified, filters [RobotMedia] by [robotMedia] id
         * @param year [Year] if specified, filters [RobotMedia] by [year] id
         * @param event [Event] if specified, filters [RobotMedia] by [event] id
         * @param team [Team] if specified, filters [RobotMedia] by [team] id
         * @param onlyDrafts [Boolean] if true, filters [RobotMedia] [isDraft]
         * @param database used to load [RobotMedia]
         * @return [ArrayList] of [RobotMedia]
         */
        fun getObjects(robotMedia: RobotMedia?, year: Year?, event: Event?, team: Team?, onlyDrafts: Boolean, database: Database): ArrayList<RobotMedia>
        {
            return ArrayList<RobotMedia>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (robotMedia != null)
                {
                    whereStatement.append("$COLUMN_NAME_LOCAL_ID = ?")
                    whereArgs.add(robotMedia.localId.toString())
                }

                //filter by object
                if (year != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_YEAR_ID = ?")
                    whereArgs.add(year.serverId.toString())
                }

                //filter by object
                if (event != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_EVENT_ID = ?")
                    whereArgs.add(event.blueAllianceId)
                }

                //filter by object
                if (team != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_TEAM_ID = ?")
                    whereArgs.add(team.serverId.toString())
                }

                //filter by object
                if (onlyDrafts)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_IS_DRAFT = 1")
                }

                //add all object records to array list
                with(database.getObjects(
                        TABLE_NAME,
                        whereStatement.toString(),
                        whereArgs))
                {
                    if (this != null) {
                        while (moveToNext()) {
                            add(
                                    RobotMedia(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getLong(COLUMN_NAME_YEAR_ID),
                                            getString(COLUMN_NAME_EVENT_ID),
                                            getLong(COLUMN_NAME_TEAM_ID),
                                            getString(COLUMN_NAME_FILE_URI),
                                            getBoolean(COLUMN_NAME_IS_DRAFT)
                                    )
                            )
                        }

                        close()
                    }
                }
            }
        }
    }

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
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(this, null, null, null, false, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    loadParentValues(this)
                    this@RobotMedia.yearId = yearId
                    this@RobotMedia.eventId = eventId
                    this@RobotMedia.teamId = teamId
                    this@RobotMedia.fileUri = fileUri
                    this@RobotMedia.isDraft = isDraft
                    return true
                }
            }
        }

        return false
    }

    /**
     * @see Table.childValues
     */
    override val childValues: MasterContentValues
        get() = MasterContentValues().apply {
            put(COLUMN_NAME_YEAR_ID, yearId)
            put(COLUMN_NAME_EVENT_ID, eventId)
            put(COLUMN_NAME_TEAM_ID, teamId)
            put(COLUMN_NAME_FILE_URI, fileUri)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Robot Media"
    }
}

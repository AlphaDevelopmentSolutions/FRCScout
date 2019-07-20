package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class RobotMedia(
        var id: Int,
        var teamId: Int,
        var fileUri: String,
        var isDraft: Boolean) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {

        val TABLE_NAME = "robot_media"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_TEAM_ID = "TeamId"
        val COLUMN_NAME_FILE_URI = "FileURI"
        val COLUMN_NAME_IS_DRAFT = "IsDraft"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_TEAM_ID + " INTEGER," +
                COLUMN_NAME_FILE_URI + " TEXT," +
                COLUMN_NAME_IS_DRAFT + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         * @param clearDrafts boolean if you want to include drafts in the clear
         */
        fun clearTable(database: Database, clearDrafts: Boolean)
        {
            clearTable(database, TABLE_NAME, clearDrafts)
        }

        /**
         * Returns arraylist of robot media with specified filters from database
         * @param robotMedia if specified, filters robot media by robotmedia id
         * @param team if specified, filters robot media by team id
         * @param onlyDrafts if true, filters robot media by draft
         * @param database used to load robot media
         * @return arraylist of robot media
         */
        fun getObjects(robotMedia: RobotMedia?, team: Team?, onlyDrafts: Boolean, database: Database): ArrayList<RobotMedia>?
        {
            return database.getRobotMedia(robotMedia, team, onlyDrafts)
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
        get() = BitmapFactory.decodeFile(this.fileUri)

    override fun toString(): String
    {
        return "Team $teamId - Robot Media"
    }

    //endregion

    //region Load, Save & Delete

    /**
     * Loads the object from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val robotMediaArrayList = getObjects(this, null, false, database)
            val robotMedia = if (robotMediaArrayList!!.size > 0) robotMediaArrayList[0] else null

            if (robotMedia != null)
            {
                teamId = robotMedia.teamId
                fileUri = robotMedia.fileUri
                isDraft = robotMedia.isDraft
                return true
            }
        }

        return false
    }

    /**
     * Saves the object into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved object
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setRobotMedia(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            this.id = id

        return id
    }

    /**
     * Deletes the object from the database
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun delete(database: Database): Boolean
    {
        var successful = false

        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            successful = database.deleteRobotMedia(this)

        }

        return successful
    }

    //endregion
}

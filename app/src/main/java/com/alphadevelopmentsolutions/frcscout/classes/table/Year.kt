package com.alphadevelopmentsolutions.frcscout.classes.table

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.alphadevelopmentsolutions.frcscout.classes.Database
import java.util.*

class Year(
        var id: Int = DEFAULT_INT,
        var serverId: Int = DEFAULT_INT,
        var name: String = DEFAULT_STRING,
        var startDate: Date = DEFAULT_DATE,
        var endDate: Date = DEFAULT_DATE,
        var imageUri: String = DEFAULT_STRING) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "years"
        val COLUMN_NAME_ID = "LocalId"
        val COLUMN_NAME_SERVER_ID = "Id"
        val COLUMN_NAME_NAME = "Name"
        val COLUMN_NAME_START_DATE = "StartDate"
        val COLUMN_NAME_END_DATE = "EndDate"
        val COLUMN_NAME_IMAGE_URI = "ImageUri"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_SERVER_ID + " INTEGER," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_START_DATE + " INTEGER," +
                COLUMN_NAME_END_DATE + " INTEGER," +
                COLUMN_NAME_IMAGE_URI + " TEXT)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            clearTable(database, TABLE_NAME)
        }

        /**
         * Returns arraylist of years with specified filters from database
         * @param year if specified, filters years by year id
         * @param database used to load robot media
         * @return ArrayList of years
         */
        fun getObjects(year: Year?, database: Database): ArrayList<Year>
        {
            return database.getYears(year)
        }
    }
    
    /**
     * Gets a bitmap version of the object
     * @return bitmap version of object
     */
    val imageBitmap: Bitmap
        get() = BitmapFactory.decodeFile(this.imageUri)


    override fun toString(): String
    {
        return "$serverId - $name"
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
            val yearArrayList = getObjects(this, database)
            val year = if (yearArrayList.size > 0) yearArrayList[0] else null

            if (year != null)
            {
                id = year.id
                name = year.name
                startDate = year.startDate
                endDate = year.endDate
                imageUri = year.imageUri
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
            id = database.setYears(this).toInt()

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
            successful = database.deleteYears(this)

        }

        return successful
    }

    //endregion
}

package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.alphadevelopmentsolutions.frcscout.Classes.Database

import java.util.ArrayList
import java.util.Date

class Year: Table
{

    var id: Int? = null
    var serverId: Int? = null
    lateinit var name: String
    lateinit var startDate: Date
    lateinit var endDate: Date
    lateinit var imageUri: String

    constructor(id: Int,
                serverId: Int,
                name: String,
                startDate: Date,
                endDate: Date,
                imageUri: String) : super(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
    {
        this.id = id
        this.serverId = serverId
        this.name = name
        this.startDate = startDate
        this.endDate = endDate
        this.imageUri = imageUri
    }

    constructor(id: Int, database: Database) : super(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
    {
        this.serverId = id
        load(database)
    }

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
            database.clearTable(TABLE_NAME)
        }

        /**
         * Returns arraylist of years with specified filters from database
         * @param year if specified, filters years by year id
         * @param database used to load robot media
         * @return ArrayList of years
         */
        fun getYears(year: Year?, database: Database): ArrayList<Year>?
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
            val yearArrayList = getYears(this, database)
            val year = if (yearArrayList!!.size > 0) yearArrayList[0] else null

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
            id = id

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

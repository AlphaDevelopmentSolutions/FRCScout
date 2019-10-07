package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

class User(
        var id: Int = DEFAULT_INT,
        var firstName: String = DEFAULT_STRING,
        var lastName: String = DEFAULT_STRING) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "users"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_FIRST_NAME = "FirstName"
        val COLUMN_NAME_LAST_NAME = "LastName"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_FIRST_NAME + " TEXT," +
                COLUMN_NAME_LAST_NAME + " TEXT)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            clearTable(database, TABLE_NAME)
        }

        /**
         * Returns arraylist of users with specified filters from database
         * @param user if specified, filters users by user id
         * @param database used to load users
         * @return arraylist of users
         */
        fun getObjects(user: User?, database: Database): ArrayList<User>?
        {
            return database.getUsers(user)
        }
    }

    override fun toString(): String
    {
        return "$firstName $lastName"
    }


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
            val users = getObjects(this, database)
            val user = if (users!!.size > 0) users[0] else null

            if (user != null)
            {
                firstName = user.firstName
                lastName = user.lastName
                return true
            }
        }

        return false
    }

    /**
     * Saves the object into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved ScoutCard
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setUser(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            this.id = id

        return id
    }

    /**
     * Deletes the ScoutCard from the database
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
            successful = database.deleteUser(this)

        }

        return successful
    }

    //endregion
}

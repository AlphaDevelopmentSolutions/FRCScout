package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import java.util.*
import kotlin.collections.ArrayList

class User(
        var firstName: String = DEFAULT_STRING,
        var lastName: String = DEFAULT_STRING) : Table()
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME: String
            get() = "users"

        const val COLUMN_NAME_FIRST_NAME = "FirstName"
        const val COLUMN_NAME_LAST_NAME = "LastName"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_FIRST_NAME, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_LAST_NAME, SQLiteDataTypes.TEXT))
            }

        /**
         * Returns [ArrayList] of [User] with specified filters from [database]
         * @param user if specified, filters [User] by [user] id
         * @param database used to load [User]
         * @return [ArrayList] of [User]
         */
        fun getObjects(user: User?, database: Database): ArrayList<User>
        {
            val users = ArrayList<User>()

            val whereStatement = StringBuilder()
            val whereArgs = ArrayList<String>()

            if (user != null)
            {
                whereStatement.append(User.COLUMN_NAME_ID + " = ?")
                whereArgs.add(user.id.toString())
            }
            
            
            with(database.getObjects(
                    TABLE_NAME,
                    whereStatement.toString(),
                    whereArgs))
            {
                //make sure the cursor isn't null, else we die
                if (this != null)
                {
                    while (moveToNext())
                    {

                        val id = getInt(getColumnIndex(User.COLUMN_NAME_ID))
                        val firstName = getString(getColumnIndex(COLUMN_NAME_FIRST_NAME))
                        val lastName = getString(getColumnIndex(COLUMN_NAME_LAST_NAME))

                        users.add(User(id, firstName, lastName))
                    }

                    close()

                    return users
                }
            }


            return null
        }
    }

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

//        set the id if the save was successful
//        if (id > 0)
//            this.id = id

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

    override fun getValues(): MasterContentValues
    {
        return MasterContentValues().apply {
            put(COLUMN_NAME_FIRST_NAME, firstName)
            put(COLUMN_NAME_LAST_NAME, lastName)
        }
    }

    override fun toString(): String
    {
        return "$firstName $lastName"
    }
}

package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import kotlin.collections.ArrayList

class User(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var firstName: String = DEFAULT_STRING,
        var lastName: String = DEFAULT_STRING) : Table(TABLE_NAME, localId, serverId)
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
        fun getObjects(user: User?, database: Database): ArrayList<User> {
            return ArrayList<User>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (user != null) {
                    whereStatement.append("$COLUMN_NAME_LOCAL_ID = ?")
                    whereArgs.add(user.localId.toString())
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
                                    User(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getString(COLUMN_NAME_FIRST_NAME),
                                            getString(COLUMN_NAME_LAST_NAME)
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
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(this, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    this@User.loadParentValues(this)
                    this@User.firstName = firstName
                    this@User.lastName = lastName
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
            put(COLUMN_NAME_FIRST_NAME, firstName)
            put(COLUMN_NAME_LAST_NAME, lastName)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "$firstName $lastName"
    }
}

package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*

class RobotInfoKey(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        lastUpdated: Date = DEFAULT_DATE,
        var yearId: Int = DEFAULT_INT,
        var keyState: String = DEFAULT_STRING,
        var keyName: String = DEFAULT_STRING,
        var sortOrder: Int = DEFAULT_INT) : Table(TABLE_NAME, localId, serverId, lastUpdated)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "robot_info_keys"

        const val COLUMN_NAME_YEAR_ID = "YearId"
        const val COLUMN_NAME_SORT_ORDER = "SortOrder"
        const val COLUMN_NAME_KEY_STATE = "KeyState"
        const val COLUMN_NAME_KEY_NAME = "KeyName"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_YEAR_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_SORT_ORDER, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_KEY_STATE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_KEY_NAME, SQLiteDataTypes.TEXT))
            }

        /**
         * Returns [ArrayList] of [RobotInfoKey] with specified filters from [database]
         * @param year if specified, filters [RobotInfoKey] by [year] id
         * @param robotInfoKey if specified, filters [RobotInfoKey] by [robotInfoKey] id
         * @param database used to load [RobotInfoKey]
         * @return [ArrayList] of [RobotInfoKey]
         */
        fun getObjects(year: Year?, robotInfoKey: RobotInfoKey?, database: Database): ArrayList<RobotInfoKey> {
            return ArrayList<RobotInfoKey>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (year != null)
                {
                    whereStatement.append("$COLUMN_NAME_YEAR_ID = ? ")
                    whereArgs.add(year.serverId.toString())
                }

                //filter by object
                if (robotInfoKey != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("${if(robotInfoKey.localId != DEFAULT_LONG) COLUMN_NAME_LOCAL_ID else COLUMN_NAME_SERVER_ID} = ? ")
                    whereArgs.add(
                            if(robotInfoKey.localId != DEFAULT_LONG)
                                robotInfoKey.localId.toString()
                            else
                                robotInfoKey.serverId.toString()
                    )
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
                                 RobotInfoKey(
                                         getLong(COLUMN_NAME_LOCAL_ID),
                                         getLong(COLUMN_NAME_SERVER_ID),
                                         getDate(COLUMN_NAME_LAST_UPDATED),
                                         getInt(COLUMN_NAME_YEAR_ID),
                                         getString(COLUMN_NAME_KEY_STATE),
                                         getString(COLUMN_NAME_KEY_NAME),
                                         getInt(COLUMN_NAME_SORT_ORDER)
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
        with(getObjects(null, this, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    this@RobotInfoKey.loadParentValues(this)
                    this@RobotInfoKey.yearId = yearId
                    this@RobotInfoKey.sortOrder = sortOrder
                    this@RobotInfoKey.keyState = keyState
                    this@RobotInfoKey.keyName = keyName
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
            put(COLUMN_NAME_SORT_ORDER, sortOrder)
            put(COLUMN_NAME_KEY_STATE, keyState)
            put(COLUMN_NAME_KEY_NAME, keyName)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return ""
    }
}

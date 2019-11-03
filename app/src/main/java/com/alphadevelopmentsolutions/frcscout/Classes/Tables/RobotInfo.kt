package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*

class RobotInfo(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var yearId: Long = DEFAULT_LONG,
        var eventId: String = DEFAULT_STRING,
        var teamId: Long = DEFAULT_LONG,
        var propertyValue: String= DEFAULT_STRING,
        var propertyKeyId: Long = DEFAULT_LONG,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "robot_info"

        const val COLUMN_NAME_YEAR_ID = "YearId"
        const val COLUMN_NAME_EVENT_ID = "EventId"
        const val COLUMN_NAME_TEAM_ID = "TeamId"
        const val COLUMN_NAME_PROPERTY_VALUE = "PropertyValue"
        const val COLUMN_NAME_PROPERTY_KEY_ID = "PropertyKeyId"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_YEAR_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_EVENT_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_TEAM_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_PROPERTY_VALUE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_PROPERTY_KEY_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_IS_DRAFT, SQLiteDataTypes.INTEGER))
            }
        
        /**
         * Returns [ArrayList] of [RobotInfo] with specified filters from [database]
         * @param year if specified, filters by [year] id
         * @param event if specified, filters by [event] id
         * @param team if specified, filters by [team] id
         * @param robotInfo if specified, filters by [robotInfo] id
         * @param onlyDrafts if true, filters by [isDraft]
         * @param database used to load
         * @return [ArrayList] of [RobotInfo]
         */
        fun getObjects(year: Year?, event: Event?, team: Team?, robotInfoKey: RobotInfoKey?, robotInfo: RobotInfo?, onlyDrafts: Boolean, database: Database): ArrayList<RobotInfo> {
            return ArrayList<RobotInfo>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (year != null)
                {
                    whereStatement.append("$COLUMN_NAME_YEAR_ID = ? ")
                    whereArgs.add(year.serverId.toString())
                }

                //filter by object
                if (event != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_EVENT_ID = ? ")
                    whereArgs.add(event.blueAllianceId)
                }

                //filter by object
                if (team != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_TEAM_ID = ? ")
                    whereArgs.add(team.serverId.toString())
                }

                //filter by object
                if (robotInfoKey != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_PROPERTY_KEY_ID = ? ")
                    whereArgs.add(robotInfoKey.serverId.toString())
                }

                //filter by object
                if (robotInfo != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_LOCAL_ID = ? ")
                    whereArgs.add(robotInfo.localId.toString())
                }

                //filter by object
                if (onlyDrafts)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_IS_DRAFT = 1 ")
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
                                    RobotInfo(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getLong(COLUMN_NAME_YEAR_ID),
                                            getString(COLUMN_NAME_EVENT_ID),
                                            getLong(COLUMN_NAME_TEAM_ID),
                                            getString(COLUMN_NAME_PROPERTY_VALUE),
                                            getLong(COLUMN_NAME_PROPERTY_KEY_ID),
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
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(null, null, null, null, this, false, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    loadParentValues(this)
                    this@RobotInfo.yearId = yearId
                    this@RobotInfo.eventId = eventId
                    this@RobotInfo.teamId = teamId
                    this@RobotInfo.propertyValue = propertyValue
                    this@RobotInfo.propertyKeyId = propertyKeyId
                    this@RobotInfo.isDraft = isDraft
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
            put(COLUMN_NAME_PROPERTY_VALUE, propertyValue)
            put(COLUMN_NAME_PROPERTY_KEY_ID, propertyKeyId)
            put(COLUMN_NAME_IS_DRAFT, isDraft)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Robot Info"
    }
}

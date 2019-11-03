package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*

class EventTeamList(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var teamId: Long = DEFAULT_LONG,
        var eventId: String = DEFAULT_STRING) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "event_team_list"

        const val COLUMN_NAME_TEAM_ID = "TeamId"
        const val COLUMN_NAME_EVENT_ID = "EventId"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_TEAM_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_EVENT_ID, SQLiteDataTypes.TEXT))
            }

        /**
         * Returns [ArrayList] of [EventTeamList] with specified filters from [database]
         * @param eventTeamList if specified, filters [EventTeamList] by [eventTeamList] id
         * @param event if specified, filters [EventTeamList] by [event] id
         * @param database used to load [EventTeamList]
         * @return [ArrayList] of [EventTeamList]
         */
        fun getObjects(eventTeamList: EventTeamList?, event: Event?, database: Database): ArrayList<EventTeamList> {
            return ArrayList<EventTeamList>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (eventTeamList != null)
                {
                    whereStatement.append("$COLUMN_NAME_EVENT_ID = ?")
                    whereArgs.add(eventTeamList.localId.toString())
                }

                //filter by object
                if (event != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_EVENT_ID = ?")
                    whereArgs.add(event.blueAllianceId)
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
                                    EventTeamList(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getLong(COLUMN_NAME_TEAM_ID),
                                            getString(COLUMN_NAME_EVENT_ID)
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
        with(getObjects(this, null, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    loadParentValues(this)
                    this@EventTeamList.teamId = teamId
                    this@EventTeamList.eventId = eventId
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
            put(COLUMN_NAME_TEAM_ID, teamId)
            put(COLUMN_NAME_EVENT_ID, eventId)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return ""
    }
}

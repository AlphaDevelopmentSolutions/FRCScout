package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*

class ScoutCardInfo(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var yearId: Long = DEFAULT_LONG,
        var eventId: String = DEFAULT_STRING,
        var matchId: String = DEFAULT_STRING,
        var teamId: Long = DEFAULT_LONG,
        var completedBy: String = DEFAULT_STRING,
        var propertyValue: String = DEFAULT_STRING,
        var propertyKeyId: Long = DEFAULT_LONG,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "scout_card_info"
        const val COLUMN_NAME_YEAR_ID = "YearId"
        const val COLUMN_NAME_EVENT_ID = "EventId"
        const val COLUMN_NAME_MATCH_ID = "MatchId"
        const val COLUMN_NAME_TEAM_ID = "TeamId"
        const val COLUMN_NAME_COMPLETED_BY = "CompletedBy"
        const val COLUMN_NAME_PROPERTY_VALUE = "PropertyValue"
        const val COLUMN_NAME_PROPERTY_KEY_ID = "PropertyKeyId"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_YEAR_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_EVENT_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_MATCH_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_TEAM_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_COMPLETED_BY, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_PROPERTY_VALUE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_PROPERTY_KEY_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_IS_DRAFT, SQLiteDataTypes.INTEGER))
            }

        /**
         * Returns [ArrayList] of [User] with specified filters from [database]
         * @param event if specified, filters [ScoutCardInfo] by [event] id
         * @param match if specified, filters [ScoutCardInfo] by [match] id
         * @param team if specified, filters [ScoutCardInfo] by [team] id
         * @param scoutCardInfoKey if specified, filters [ScoutCardInfo] by [scoutCardInfoKey] id
         * @param scoutCardInfo if specified, filters [ScoutCardInfo] by [scoutCardInfo] id
         * @param onlyDrafts if true, filters [ScoutCardInfo] by [isDraft]
         * @param database used to load [ScoutCardInfo]
         * @return [ArrayList] of [ScoutCardInfo]
         */
        fun getObjects(event: Event?, match: Match?, team: Team?, scoutCardInfoKey: ScoutCardInfoKey?, scoutCardInfo: ScoutCardInfo?, onlyDrafts: Boolean, database: Database): ArrayList<ScoutCardInfo>
        {
            return ArrayList<ScoutCardInfo>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (event != null)
                {
                    whereStatement.append("$COLUMN_NAME_EVENT_ID = ?")
                    whereArgs.add(event.blueAllianceId)
                }

                //filter by object
                if (match != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_MATCH_ID = ?")
                    whereArgs.add(match.key)
                }

                //filter by object
                if (team != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_TEAM_ID = ?")
                    whereArgs.add(team.serverId.toString())
                }

                //filter by object
                if (scoutCardInfoKey != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_PROPERTY_KEY_ID = ?")
                    whereArgs.add(scoutCardInfoKey.serverId.toString())
                }

                //filter by object
                if (scoutCardInfo != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("$COLUMN_NAME_LOCAL_ID = ?")
                    whereArgs.add(scoutCardInfo.localId.toString())
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
                                    ScoutCardInfo(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getLong(COLUMN_NAME_YEAR_ID),
                                            getString(COLUMN_NAME_EVENT_ID),
                                            getString(COLUMN_NAME_MATCH_ID),
                                            getLong(COLUMN_NAME_TEAM_ID),
                                            getString(COLUMN_NAME_COMPLETED_BY),
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
                    this@ScoutCardInfo.yearId = yearId
                    this@ScoutCardInfo.eventId = eventId
                    this@ScoutCardInfo.matchId = matchId
                    this@ScoutCardInfo.teamId = teamId
                    this@ScoutCardInfo.completedBy = completedBy
                    this@ScoutCardInfo.propertyValue = propertyValue
                    this@ScoutCardInfo.propertyKeyId = propertyKeyId
                    this@ScoutCardInfo.isDraft = isDraft
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
            put(COLUMN_NAME_MATCH_ID, matchId)
            put(COLUMN_NAME_TEAM_ID, teamId)
            put(COLUMN_NAME_COMPLETED_BY, completedBy)
            put(COLUMN_NAME_PROPERTY_VALUE, propertyValue)
            put(COLUMN_NAME_PROPERTY_KEY_ID, propertyKeyId)
            put(COLUMN_NAME_IS_DRAFT, isDraft)
        }


    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "Team $teamId - Scout Card"
    }

    //endregion
}

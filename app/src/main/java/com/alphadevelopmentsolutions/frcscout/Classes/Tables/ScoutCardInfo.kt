package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import java.util.*

class ScoutCardInfo(
        var id: Int = DEFAULT_INT,
        var yearId: Int = DEFAULT_INT,
        var eventId: String = DEFAULT_STRING,
        var matchId: String = DEFAULT_STRING,
        var teamId: Int = DEFAULT_INT,
        var completedBy: String = DEFAULT_STRING,
        var propertyValue: String = DEFAULT_STRING,
        var propertyKeyId: Int = DEFAULT_INT,
        var isDraft: Boolean = DEFAULT_BOOLEAN) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {
        val TABLE_NAME = "scout_card_info"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_YEAR_ID = "YearId"
        val COLUMN_NAME_EVENT_ID = "EventId"
        val COLUMN_NAME_MATCH_ID = "MatchId"
        val COLUMN_NAME_TEAM_ID = "TeamId"

        val COLUMN_NAME_COMPLETED_BY = "CompletedBy"

        val COLUMN_NAME_PROPERTY_VALUE = "PropertyValue"
        val COLUMN_NAME_PROPERTY_KEY_ID = "PropertyKeyId"

        val COLUMN_NAME_IS_DRAFT = "IsDraft"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_YEAR_ID + " INTEGER," +
                COLUMN_NAME_EVENT_ID + " TEXT," +
                COLUMN_NAME_MATCH_ID + " TEXT," +
                COLUMN_NAME_TEAM_ID + " INTEGER," +

                COLUMN_NAME_COMPLETED_BY + " TEXT," +

                COLUMN_NAME_PROPERTY_VALUE + " TEXT," +
                COLUMN_NAME_PROPERTY_KEY_ID + " TEXT," +

                COLUMN_NAME_IS_DRAFT + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         * @param clearDrafts boolean if you want to include drafts in the clear
         */
        fun clearTable(database: Database, clearDrafts: Boolean = false)
        {
            clearTable(database, TABLE_NAME, clearDrafts)
        }

        /**
         * Returns arraylist of scout cards with specified filters from database
         * @param event if specified, filters scout cards by event id
         * @param match if specified, filters scout cards by match id
         * @param team if specified, filters scout cards by team id
         * @param scoutCardInfoKey if specified, filters scout cards by scout card id
         * @param scoutCardInfo if specified, filters scout cards by scout card id
         * @param onlyDrafts if true, filters scout cards by draft
         * @param database used to load scout cards
         * @return [ArrayList] of [ScoutCardInfo]
         */
        fun getObjects(event: Event?, match: Match?, team: Team?, scoutCardInfoKey: ScoutCardInfoKey?, scoutCardInfo: ScoutCardInfo?, onlyDrafts: Boolean, database: Database): ArrayList<ScoutCardInfo>
        {
            return database.getScoutCardInfo(event, match, team, scoutCardInfoKey, scoutCardInfo, onlyDrafts)
        }
    }

    override fun toString(): String
    {
        return "Team $teamId - Scout Card"
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
            val scoutCardInfos = getObjects(null, null, null, null,this, false, database)
            val scoutCardInfo = if (scoutCardInfos.size > 0) scoutCardInfos[0] else null

            if (scoutCardInfo != null)
            {
                yearId = scoutCardInfo.yearId
                eventId = scoutCardInfo.eventId
                matchId = scoutCardInfo.matchId
                teamId = scoutCardInfo.teamId

                completedBy = scoutCardInfo.completedBy

                propertyValue = scoutCardInfo.propertyValue
                propertyKeyId = scoutCardInfo.propertyKeyId

                isDraft = scoutCardInfo.isDraft
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
            id = database.setScoutCardInfo(this).toInt()

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
            successful = database.deleteScoutCardInfo(this)

        }

        return successful
    }

    //endregion
}

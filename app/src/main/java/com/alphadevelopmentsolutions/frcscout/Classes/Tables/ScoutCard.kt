package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition
import java.text.SimpleDateFormat
import java.util.*

class ScoutCard(
        var id: Int,
        var matchId: String,
        var teamId: Int,
        var eventId: String,
        var allianceColor: String,
        var completedBy: String,
        var preGameStartingLevel: Int,
        var preGameStartingPosition: StartingPosition,
        var preGameStartingPiece: StartingPiece,
        var autonomousExitHabitat: Boolean,
        var autonomousHatchPanelsPickedUp: Int,
        var autonomousHatchPanelsSecuredAttempts: Int,
        var autonomousHatchPanelsSecured: Int,
        var autonomousCargoPickedUp: Int,
        var autonomousCargoStoredAttempts: Int,
        var autonomousCargoStored: Int,
        var teleopHatchPanelsPickedUp: Int,
        var teleopHatchPanelsSecuredAttempts: Int,
        var teleopHatchPanelsSecured: Int,
        var teleopCargoPickedUp: Int,
        var teleopCargoStoredAttempts: Int,
        var teleopCargoStored: Int,
        var endGameReturnedToHabitat: Int,
        var endGameReturnedToHabitatAttempts: Int,
        var defenseRating: Int,
        var offenseRating: Int,
        var driveRating: Int,
        var notes: String,
        var completedDate: Date,
        var isDraft: Boolean) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
    companion object
    {

        val TABLE_NAME = "scout_cards"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_MATCH_ID = "MatchId"
        val COLUMN_NAME_TEAM_ID = "TeamId"
        val COLUMN_NAME_EVENT_ID = "EventId"
        val COLUMN_NAME_ALLIANCE_COLOR = "AllianceColor"
        val COLUMN_NAME_COMPLETED_BY = "CompletedBy"

        val COLUMN_NAME_PRE_GAME_STARTING_LEVEL = "PreGameStartingLevel"
        val COLUMN_NAME_PRE_GAME_STARTING_POSITION = "PreGameStartingPosition"
        val COLUMN_NAME_PRE_GAME_STARTING_PIECE = "PreGameStartingPiece"

        val COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT = "AutonomousExitHabitat"
        val COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP = "AutonomousHatchPanelsPickedUp"
        val COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED = "AutonomousHatchPanelsSecured"
        val COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS = "AutonomousHatchPanelsSecuredAttempts"
        val COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP = "AutonomousCargoPickedUp"
        val COLUMN_NAME_AUTONOMOUS_CARGO_STORED = "AutonomousCargoStored"
        val COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS = "AutonomousCargoStoredAttempts"

        val COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP = "TeleopHatchPanelsPickedUp"
        val COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED = "TeleopHatchPanelsSecured"
        val COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS = "TeleopHatchPanelsSecuredAttempts"
        val COLUMN_NAME_TELEOP_CARGO_PICKED_UP = "TeleopCargoPickedUp"
        val COLUMN_NAME_TELEOP_CARGO_STORED = "TeleopCargoStored"
        val COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS = "TeleopCargoStoredAttempts"

        val COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT = "EndGameReturnedToHabitat"
        val COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS = "EndGameReturnedToHabitatAttempts"

        val COLUMN_NAME_DEFENSE_RATING = "DefenseRating"
        val COLUMN_NAME_OFFENSE_RATING = "OffenseRating"
        val COLUMN_NAME_DRIVE_RATING = "DriveRating"
        val COLUMN_NAME_NOTES = "Notes"

        val COLUMN_NAME_COMPLETED_DATE = "CompletedDate"
        val COLUMN_NAME_IS_DRAFT = "IsDraft"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_MATCH_ID + " TEXT," +
                COLUMN_NAME_TEAM_ID + " INTEGER," +
                COLUMN_NAME_EVENT_ID + " TEXT," +
                COLUMN_NAME_ALLIANCE_COLOR + " TEXT," +
                COLUMN_NAME_COMPLETED_BY + " TEXT," +

                COLUMN_NAME_PRE_GAME_STARTING_LEVEL + " INTEGER," +
                COLUMN_NAME_PRE_GAME_STARTING_POSITION + " TEXT," +
                COLUMN_NAME_PRE_GAME_STARTING_PIECE + " TEXT," +

                COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT + " INTEGER," +
                COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP + " INTEGER," +
                COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS + " INTEGER," +
                COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED + " INTEGER," +
                COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP + " INTEGER," +
                COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS + " INTEGER," +
                COLUMN_NAME_AUTONOMOUS_CARGO_STORED + " INTEGER," +

                COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP + " INTEGER," +
                COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS + " INTEGER," +
                COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED + " INTEGER," +
                COLUMN_NAME_TELEOP_CARGO_PICKED_UP + " INTEGER," +
                COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS + " INTEGER," +
                COLUMN_NAME_TELEOP_CARGO_STORED + " INTEGER," +

                COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT + " INTEGER," +
                COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS + " INTEGER," +

                COLUMN_NAME_DEFENSE_RATING + " INTEGER," +
                COLUMN_NAME_OFFENSE_RATING + " INTEGER," +
                COLUMN_NAME_DRIVE_RATING + " INTEGER," +
                COLUMN_NAME_NOTES + " TEXT," +

                COLUMN_NAME_COMPLETED_DATE + " INTEGER," +
                COLUMN_NAME_IS_DRAFT + " INTEGER)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         * @param clearDrafts boolean if you want to include drafts in the clear
         */
        fun clearTable(database: Database, clearDrafts: Boolean)
        {
            clearTable(database, TABLE_NAME, clearDrafts)
        }

        /**
         * Returns arraylist of scout cards with specified filters from database
         * @param event if specified, filters scout cards by event id
         * @param match if specified, filters scout cards by match id
         * @param team if specified, filters scout cards by team id
         * @param scoutCard if specified, filters scout cards by scout card id
         * @param onlyDrafts if true, filters scout cards by draft
         * @param database used to load scout cards
         * @return arraylist of scout cards
         */
        fun getObjects(event: Event?, match: Match?, team: Team?, scoutCard: ScoutCard?, onlyDrafts: Boolean, database: Database): ArrayList<ScoutCard>?
        {
            return database.getScoutCards(event, match, team, scoutCard, onlyDrafts)
        }
    }

    /**
     * Gets the completed date formated for MySQL timestamp
     * @return MySQL time stamp formatted date
     */
    val completedDateForSQL: String
        get()
        {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd H:mm:ss")

            return simpleDateFormat.format(completedDate)
        }


    override fun toString(): String
    {
        return "Team $teamId - Scout Card"
    }


    //endregion

    //region Load, Save & Delete

    /**
     * Loads the ScoutCard from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val scoutCards = getObjects(null, null, null, this, false, database)
            val scoutCard = if (scoutCards!!.size > 0) scoutCards[0] else null

            if (scoutCard != null)
            {
                matchId = scoutCard.matchId
                teamId = scoutCard.teamId
                eventId = scoutCard.eventId
                allianceColor = scoutCard.allianceColor
                completedBy = scoutCard.completedBy

                preGameStartingLevel = scoutCard.preGameStartingLevel
                preGameStartingPosition = scoutCard.preGameStartingPosition
                preGameStartingPiece = scoutCard.preGameStartingPiece

                autonomousExitHabitat = scoutCard.autonomousExitHabitat
                autonomousHatchPanelsPickedUp = scoutCard.autonomousHatchPanelsPickedUp
                autonomousHatchPanelsSecuredAttempts = scoutCard.autonomousHatchPanelsSecuredAttempts
                autonomousHatchPanelsSecured = scoutCard.autonomousHatchPanelsSecured
                autonomousCargoPickedUp = scoutCard.autonomousCargoPickedUp
                autonomousCargoStoredAttempts = scoutCard.autonomousCargoStoredAttempts
                autonomousCargoStored = scoutCard.autonomousCargoStored

                teleopHatchPanelsPickedUp = scoutCard.teleopHatchPanelsPickedUp
                teleopHatchPanelsSecuredAttempts = scoutCard.teleopHatchPanelsSecuredAttempts
                teleopHatchPanelsSecured = scoutCard.teleopHatchPanelsSecured
                teleopCargoPickedUp = scoutCard.teleopCargoPickedUp
                teleopCargoStoredAttempts = scoutCard.teleopCargoStoredAttempts
                teleopCargoStored = scoutCard.teleopCargoStored

                endGameReturnedToHabitat = scoutCard.endGameReturnedToHabitat
                endGameReturnedToHabitatAttempts = scoutCard.endGameReturnedToHabitatAttempts

                defenseRating = scoutCard.defenseRating
                offenseRating = scoutCard.offenseRating
                driveRating = scoutCard.driveRating
                notes = scoutCard.notes
                completedDate = scoutCard.completedDate
                isDraft = scoutCard.isDraft
                return true
            }
        }

        return false
    }

    /**
     * Saves the ScoutCard into the database
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
            id = database.setScoutCard(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            id = id

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
            successful = database.deleteScoutCard(this)

        }

        return successful
    }

    //endregion
}

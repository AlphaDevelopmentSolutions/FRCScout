package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Interfaces.StatsKeys

import java.io.File
import java.util.ArrayList
import java.util.HashMap

class Team : Table
{

    var id: Int? = null
    lateinit var name: String
    lateinit var city: String
    lateinit var stateProvince: String
    lateinit var country: String
    var rookieYear: Int? = null
    lateinit var facebookURL: String
    lateinit var twitterURL: String
    lateinit var instagramURL: String
    lateinit var youtubeURL: String
    lateinit var websiteURL: String
    lateinit var imageFileURI: String

    constructor( id: Int,
                 name: String,
                 city: String,
                 stateProvince: String,
                 country: String,
                 rookieYear: Int,
                 facebookURL: String,
                 twitterURL: String,
                 instagramURL: String,
                 youtubeURL: String,
                 websiteURL: String,
                 imageFileURI: String) : super(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
    {
        this.id = id
        this.name = name
        this.city = city
        this.stateProvince = stateProvince
        this.country = country
        this.rookieYear = rookieYear
        this.facebookURL = facebookURL
        this.twitterURL = twitterURL
        this.instagramURL = instagramURL
        this.youtubeURL = youtubeURL
        this.websiteURL = websiteURL
        this.imageFileURI = imageFileURI
    }

    constructor(id: Int, database: Database) : super(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
    {
        this.id = id
        load(database)
    }

    companion object
    {

        val TABLE_NAME = "teams"
        val COLUMN_NAME_ID = "Id"
        val COLUMN_NAME_NAME = "Name"
        val COLUMN_NAME_CITY = "City"
        val COLUMN_NAME_STATEPROVINCE = "StateProvince"
        val COLUMN_NAME_COUNTRY = "Country"
        val COLUMN_NAME_ROOKIE_YEAR = "RookieYear"
        val COLUMN_NAME_FACEBOOK_URL = "FacebookURL"
        val COLUMN_NAME_TWITTER_URL = "TwitterURL"
        val COLUMN_NAME_INSTAGRAM_URL = "InstagramURL"
        val COLUMN_NAME_YOUTUBE_URL = "YoutubeURL"
        val COLUMN_NAME_WEBSITE_URL = "WebsiteURL"
        val COLUMN_NAME_IMAGE_FILE_URI = "ImageFileURI"

        val CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_NAME + " TEXT," +
                COLUMN_NAME_CITY + " TEXT," +
                COLUMN_NAME_STATEPROVINCE + " TEXT," +
                COLUMN_NAME_COUNTRY + " TEXT," +
                COLUMN_NAME_ROOKIE_YEAR + " INTEGER," +
                COLUMN_NAME_FACEBOOK_URL + " TEXT," +
                COLUMN_NAME_TWITTER_URL + " TEXT," +
                COLUMN_NAME_INSTAGRAM_URL + " TEXT," +
                COLUMN_NAME_YOUTUBE_URL + " TEXT," +
                COLUMN_NAME_WEBSITE_URL + " TEXT," +
                COLUMN_NAME_IMAGE_FILE_URI + " TEXT)"

        /**
         * Clears all data from the classes table
         * @param database used to clear table
         */
        fun clearTable(database: Database)
        {
            database.clearTable(TABLE_NAME)
        }

        /**
         * Returns arraylist of teams with specified filters from database
         * @param event if specified, filters teams by event id
         * @param match if specified, filters teams by match id
         * @param team if specified, filters teams by team id
         * @param database used to load teams
         * @return arraylist of teams
         */
        fun getTeams(event: Event?, match: Match?, team: Team?, database: Database): ArrayList<Team>?
        {
            return database.getTeams(event, match, team)
        }
    }

    /**
     * Returns the bitmap from the specified file location
     * @return null if no image found, bitmap if image found
     */
    //check if the image exists
    val imageBitmap: Bitmap?
        get()
        {
            val file = File(imageFileURI)
            return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null

        }

    /**
     * Calculates all stats for the QuickStatsFragment
     * Highly recommended that this gets ran inside it's own thread is it
     * will take a long time to process
     * @param database used to pull data
     * @return hashmap of stats
     */
    fun getStats(database: Database, event: Event): HashMap<String, HashMap<String, Double>>
    {
        val stats = HashMap<String, HashMap<String, Double>>()
        val minStats = HashMap<String, Double>()
        val avgStats = HashMap<String, Double>()
        val maxStats = HashMap<String, Double>()

        //pre-populate min stats
        minStats[StatsKeys.AUTO_EXIT_HAB] = 1000.0
        minStats[StatsKeys.AUTO_HATCH] = 1000.0
        minStats[StatsKeys.AUTO_HATCH_DROPPED] = 1000.0
        minStats[StatsKeys.AUTO_CARGO] = 1000.0
        minStats[StatsKeys.AUTO_CARGO_DROPPED] = 1000.0
        minStats[StatsKeys.TELEOP_HATCH] = 1000.0
        minStats[StatsKeys.TELEOP_HATCH_DROPPED] = 1000.0
        minStats[StatsKeys.TELEOP_CARGO] = 1000.0
        minStats[StatsKeys.TELEOP_CARGO_DROPPED] = 1000.0
        minStats[StatsKeys.RETURNED_HAB] = 1000.0
        minStats[StatsKeys.RETURNED_HAB_FAILS] = 1000.0
        minStats[StatsKeys.DEFENSE_RATING] = 1000.0
        minStats[StatsKeys.OFFENSE_RATING] = 1000.0
        minStats[StatsKeys.DRIVE_RATING] = 1000.0

        //pre-populate avg stats
        avgStats[StatsKeys.AUTO_EXIT_HAB] = 0.0
        avgStats[StatsKeys.AUTO_HATCH] = 0.0
        avgStats[StatsKeys.AUTO_HATCH_DROPPED] = 0.0
        avgStats[StatsKeys.AUTO_CARGO] = 0.0
        avgStats[StatsKeys.AUTO_CARGO_DROPPED] = 0.0
        avgStats[StatsKeys.TELEOP_HATCH] = 0.0
        avgStats[StatsKeys.TELEOP_HATCH_DROPPED] = 0.0
        avgStats[StatsKeys.TELEOP_CARGO] = 0.0
        avgStats[StatsKeys.TELEOP_CARGO_DROPPED] = 0.0
        avgStats[StatsKeys.RETURNED_HAB] = 0.0
        avgStats[StatsKeys.RETURNED_HAB_FAILS] = 0.0
        avgStats[StatsKeys.DEFENSE_RATING] = 0.0
        avgStats[StatsKeys.OFFENSE_RATING] = 0.0
        avgStats[StatsKeys.DRIVE_RATING] = 0.0

        //pre-populate max stats
        maxStats[StatsKeys.AUTO_EXIT_HAB] = 0.0
        maxStats[StatsKeys.AUTO_HATCH] = 0.0
        maxStats[StatsKeys.AUTO_HATCH_DROPPED] = 0.0
        maxStats[StatsKeys.AUTO_CARGO] = 0.0
        maxStats[StatsKeys.AUTO_CARGO_DROPPED] = 0.0
        maxStats[StatsKeys.TELEOP_HATCH] = 0.0
        maxStats[StatsKeys.TELEOP_HATCH_DROPPED] = 0.0
        maxStats[StatsKeys.TELEOP_CARGO] = 0.0
        maxStats[StatsKeys.TELEOP_CARGO_DROPPED] = 0.0
        maxStats[StatsKeys.RETURNED_HAB] = 0.0
        maxStats[StatsKeys.RETURNED_HAB_FAILS] = 0.0
        maxStats[StatsKeys.DEFENSE_RATING] = 0.0
        maxStats[StatsKeys.OFFENSE_RATING] = 0.0
        maxStats[StatsKeys.DRIVE_RATING] = 0.0


        //get all scout cards from the database
        val scoutCards = getScoutCards(event, null, null, false, database)

        //store iterations for avg
        var i = 0

        var nulledDefenseRatings = 0
        var nulledOffenseRatings = 0

        for (scoutCard in scoutCards!!)
        {
            //calculate min

            //if they exited, check the level
            if (scoutCard.autonomousExitHabitat)
            {
                if (scoutCard.preGameStartingLevel < minStats[StatsKeys.AUTO_EXIT_HAB]!!)
                    minStats[StatsKeys.AUTO_EXIT_HAB] = scoutCard.preGameStartingLevel.toDouble()
            } else if (0 < minStats[StatsKeys.AUTO_EXIT_HAB]!!)
                minStats[StatsKeys.AUTO_EXIT_HAB] = 0.0//if they didn't exit, set to 0

            if (scoutCard.autonomousHatchPanelsSecured < minStats[StatsKeys.AUTO_HATCH]!!)
                minStats[StatsKeys.AUTO_HATCH] = scoutCard.autonomousHatchPanelsSecured.toDouble()

            if (scoutCard.autonomousHatchPanelsSecuredAttempts < minStats[StatsKeys.AUTO_HATCH_DROPPED]!!)
                minStats[StatsKeys.AUTO_HATCH_DROPPED] = scoutCard.autonomousHatchPanelsSecuredAttempts.toDouble()

            if (scoutCard.autonomousCargoStored < minStats[StatsKeys.AUTO_CARGO]!!)
                minStats[StatsKeys.AUTO_CARGO] = scoutCard.autonomousCargoStored.toDouble()

            if (scoutCard.autonomousCargoStoredAttempts < minStats[StatsKeys.AUTO_CARGO_DROPPED]!!)
                minStats[StatsKeys.AUTO_CARGO_DROPPED] = scoutCard.autonomousCargoStoredAttempts.toDouble()

            if (scoutCard.teleopHatchPanelsSecured < minStats[StatsKeys.TELEOP_HATCH]!!)
                minStats[StatsKeys.TELEOP_HATCH] = scoutCard.teleopHatchPanelsSecured.toDouble()

            if (scoutCard.teleopHatchPanelsSecuredAttempts < minStats[StatsKeys.TELEOP_HATCH_DROPPED]!!)
                minStats[StatsKeys.TELEOP_HATCH_DROPPED] = scoutCard.teleopHatchPanelsSecuredAttempts.toDouble()

            if (scoutCard.teleopCargoStored < minStats[StatsKeys.TELEOP_CARGO]!!)
                minStats[StatsKeys.TELEOP_CARGO] = scoutCard.teleopCargoStored.toDouble()

            if (scoutCard.teleopCargoStoredAttempts < minStats[StatsKeys.TELEOP_CARGO_DROPPED]!!)
                minStats[StatsKeys.TELEOP_CARGO_DROPPED] = scoutCard.teleopCargoStoredAttempts.toDouble()

            if (scoutCard.endGameReturnedToHabitat < minStats[StatsKeys.RETURNED_HAB]!!)
                minStats[StatsKeys.RETURNED_HAB] = scoutCard.endGameReturnedToHabitat.toDouble()

            if (scoutCard.endGameReturnedToHabitatAttempts < minStats[StatsKeys.RETURNED_HAB_FAILS]!!)
                minStats[StatsKeys.RETURNED_HAB_FAILS] = scoutCard.endGameReturnedToHabitatAttempts.toDouble()

            if (scoutCard.defenseRating != 0 && scoutCard.defenseRating < minStats[StatsKeys.DEFENSE_RATING]!!)
                minStats[StatsKeys.DEFENSE_RATING] = scoutCard.defenseRating.toDouble()

            if (scoutCard.offenseRating != 0 && scoutCard.offenseRating < minStats[StatsKeys.OFFENSE_RATING]!!)
                minStats[StatsKeys.OFFENSE_RATING] = scoutCard.offenseRating.toDouble()

            if (scoutCard.driveRating < minStats[StatsKeys.DRIVE_RATING]!!)
                minStats[StatsKeys.DRIVE_RATING] = scoutCard.driveRating.toDouble()

            //calculate avg
            if (scoutCard.autonomousExitHabitat)
                avgStats[StatsKeys.AUTO_EXIT_HAB] = avgStats[StatsKeys.AUTO_EXIT_HAB]!! + scoutCard.preGameStartingLevel
            avgStats[StatsKeys.AUTO_HATCH] = avgStats[StatsKeys.AUTO_HATCH]!! + scoutCard.autonomousHatchPanelsSecured
            avgStats[StatsKeys.AUTO_HATCH_DROPPED] = avgStats[StatsKeys.AUTO_HATCH_DROPPED]!! + scoutCard.autonomousHatchPanelsSecuredAttempts
            avgStats[StatsKeys.AUTO_CARGO] = avgStats[StatsKeys.AUTO_CARGO]!! + scoutCard.autonomousCargoStored
            avgStats[StatsKeys.AUTO_CARGO_DROPPED] = avgStats[StatsKeys.AUTO_CARGO_DROPPED]!! + scoutCard.autonomousCargoStoredAttempts

            avgStats[StatsKeys.TELEOP_HATCH] = avgStats[StatsKeys.TELEOP_HATCH]!! + scoutCard.teleopHatchPanelsSecured
            avgStats[StatsKeys.TELEOP_HATCH_DROPPED] = avgStats[StatsKeys.TELEOP_HATCH_DROPPED]!! + scoutCard.teleopHatchPanelsSecuredAttempts
            avgStats[StatsKeys.TELEOP_CARGO] = avgStats[StatsKeys.TELEOP_CARGO]!! + scoutCard.teleopCargoStored
            avgStats[StatsKeys.TELEOP_CARGO_DROPPED] = avgStats[StatsKeys.TELEOP_CARGO_DROPPED]!! + scoutCard.teleopCargoStoredAttempts

            avgStats[StatsKeys.RETURNED_HAB] = avgStats[StatsKeys.RETURNED_HAB]!! + scoutCard.endGameReturnedToHabitat
            avgStats[StatsKeys.RETURNED_HAB_FAILS] = avgStats[StatsKeys.RETURNED_HAB_FAILS]!! + scoutCard.endGameReturnedToHabitatAttempts

            avgStats[StatsKeys.DEFENSE_RATING] = avgStats[StatsKeys.DEFENSE_RATING]!! + scoutCard.defenseRating
            avgStats[StatsKeys.OFFENSE_RATING] = avgStats[StatsKeys.OFFENSE_RATING]!! + scoutCard.offenseRating
            avgStats[StatsKeys.DRIVE_RATING] = avgStats[StatsKeys.DRIVE_RATING]!! + scoutCard.driveRating

            nulledDefenseRatings = if (scoutCard.defenseRating == 0) nulledDefenseRatings + 1 else nulledDefenseRatings
            nulledOffenseRatings = if (scoutCard.offenseRating == 0) nulledOffenseRatings + 1 else nulledOffenseRatings

            i++

            //calculate max

            //if they exited, check the level
            if (scoutCard.autonomousExitHabitat)
            {
                if (scoutCard.preGameStartingLevel > maxStats[StatsKeys.AUTO_EXIT_HAB]!!)
                    maxStats[StatsKeys.AUTO_EXIT_HAB] = scoutCard.preGameStartingLevel.toDouble()
            } else if (0 > maxStats[StatsKeys.AUTO_EXIT_HAB]!!)
                maxStats[StatsKeys.AUTO_EXIT_HAB] = scoutCard.preGameStartingLevel.toDouble()//if they didn't exit, set to 0

            if (scoutCard.autonomousHatchPanelsSecured > maxStats[StatsKeys.AUTO_HATCH]!!)
                maxStats[StatsKeys.AUTO_HATCH] = scoutCard.autonomousHatchPanelsSecured.toDouble()

            if (scoutCard.autonomousHatchPanelsSecuredAttempts > maxStats[StatsKeys.AUTO_HATCH_DROPPED]!!)
                maxStats[StatsKeys.AUTO_HATCH_DROPPED] = scoutCard.autonomousHatchPanelsSecuredAttempts.toDouble()

            if (scoutCard.autonomousCargoStored > maxStats[StatsKeys.AUTO_CARGO]!!)
                maxStats[StatsKeys.AUTO_CARGO] = scoutCard.autonomousCargoStored.toDouble()

            if (scoutCard.autonomousCargoStoredAttempts > maxStats[StatsKeys.AUTO_CARGO_DROPPED]!!)
                maxStats[StatsKeys.AUTO_CARGO_DROPPED] = scoutCard.autonomousCargoStoredAttempts.toDouble()

            if (scoutCard.teleopHatchPanelsSecured > maxStats[StatsKeys.TELEOP_HATCH]!!)
                maxStats[StatsKeys.TELEOP_HATCH] = scoutCard.teleopHatchPanelsSecured.toDouble()

            if (scoutCard.teleopHatchPanelsSecuredAttempts > maxStats[StatsKeys.TELEOP_HATCH_DROPPED]!!)
                maxStats[StatsKeys.TELEOP_HATCH_DROPPED] = scoutCard.teleopHatchPanelsSecuredAttempts.toDouble()

            if (scoutCard.teleopCargoStored > maxStats[StatsKeys.TELEOP_CARGO]!!)
                maxStats[StatsKeys.TELEOP_CARGO] = scoutCard.teleopCargoStored.toDouble()

            if (scoutCard.teleopCargoStoredAttempts > maxStats[StatsKeys.TELEOP_CARGO_DROPPED]!!)
                maxStats[StatsKeys.TELEOP_CARGO_DROPPED] = scoutCard.teleopCargoStoredAttempts.toDouble()

            if (scoutCard.endGameReturnedToHabitat > maxStats[StatsKeys.RETURNED_HAB]!!)
                maxStats[StatsKeys.RETURNED_HAB] = scoutCard.endGameReturnedToHabitat.toDouble()

            if (scoutCard.endGameReturnedToHabitatAttempts > maxStats[StatsKeys.RETURNED_HAB_FAILS]!!)
                maxStats[StatsKeys.RETURNED_HAB_FAILS] = scoutCard.endGameReturnedToHabitatAttempts.toDouble()

            if (scoutCard.defenseRating > maxStats[StatsKeys.DEFENSE_RATING]!!)
                maxStats[StatsKeys.DEFENSE_RATING] = scoutCard.defenseRating.toDouble()

            if (scoutCard.offenseRating > maxStats[StatsKeys.OFFENSE_RATING]!!)
                maxStats[StatsKeys.OFFENSE_RATING] = scoutCard.offenseRating.toDouble()

            if (scoutCard.driveRating > maxStats[StatsKeys.DRIVE_RATING]!!)
                maxStats[StatsKeys.DRIVE_RATING] = scoutCard.driveRating.toDouble()
        }

        //once iterations have ended, do a final calculation for avg
        //calculate avg
        avgStats[StatsKeys.AUTO_EXIT_HAB] = avgStats[StatsKeys.AUTO_EXIT_HAB]!! / i
        avgStats[StatsKeys.AUTO_HATCH] = avgStats[StatsKeys.AUTO_HATCH]!! / i
        avgStats[StatsKeys.AUTO_HATCH_DROPPED] = avgStats[StatsKeys.AUTO_HATCH_DROPPED]!! / i
        avgStats[StatsKeys.AUTO_CARGO] = avgStats[StatsKeys.AUTO_CARGO]!! / i
        avgStats[StatsKeys.AUTO_CARGO_DROPPED] = avgStats[StatsKeys.AUTO_CARGO_DROPPED]!! / i

        avgStats[StatsKeys.TELEOP_HATCH] = avgStats[StatsKeys.TELEOP_HATCH]!! / i
        avgStats[StatsKeys.TELEOP_HATCH_DROPPED] = avgStats[StatsKeys.TELEOP_HATCH_DROPPED]!! / i
        avgStats[StatsKeys.TELEOP_CARGO] = avgStats[StatsKeys.TELEOP_CARGO]!! / i
        avgStats[StatsKeys.TELEOP_CARGO_DROPPED] = avgStats[StatsKeys.TELEOP_CARGO_DROPPED]!! / i

        avgStats[StatsKeys.RETURNED_HAB] = avgStats[StatsKeys.RETURNED_HAB]!! / i
        avgStats[StatsKeys.RETURNED_HAB_FAILS] = avgStats[StatsKeys.RETURNED_HAB_FAILS]!! / i

        avgStats[StatsKeys.DEFENSE_RATING] = avgStats[StatsKeys.DEFENSE_RATING]!! / (i - nulledDefenseRatings)
        avgStats[StatsKeys.OFFENSE_RATING] = avgStats[StatsKeys.OFFENSE_RATING]!! / (i - nulledOffenseRatings)
        avgStats[StatsKeys.DRIVE_RATING] = avgStats[StatsKeys.DRIVE_RATING]!! / i

        //verify the min stats are not still at default 1000
        for ((key, value) in minStats)
        {
            if (value > 900)
                minStats[key] = 0.0
        }


        stats[StatsKeys.MIN] = minStats
        stats[StatsKeys.AVG] = avgStats
        stats[StatsKeys.MAX] = maxStats

        return stats
    }

    /**
     * Gets all scout cards associated with the team
     * @param event if specified, filters scout cards by event id
     * @param match if specified, filters scout cards by match id
     * @param scoutCard if specified, filters scout cards by scoutcard id
     * @param database used for loading cards
     * @param onlyDrafts boolean if you only want drafts
     * @return arraylist of scout cards
     */
    fun getScoutCards(event: Event?, match: Match?, scoutCard: ScoutCard?, onlyDrafts: Boolean, database: Database): ArrayList<ScoutCard>?
    {
        return ScoutCard.getScoutCards(event, match, this, scoutCard, onlyDrafts, database)
    }

    /**
     * Gets all scout cards associated with the team
     * @param robotMedia if specified, filters robot media by robot media id
     * @param database used for loading cards
     * @param onlyDrafts boolean if you only want drafts
     * @return arraylist of scout cards
     */
    fun getRobotMedia(robotMedia: RobotMedia?, onlyDrafts: Boolean, database: Database): ArrayList<RobotMedia>?
    {
        return RobotMedia.getRobotMedia(robotMedia, this, onlyDrafts, database)
    }

    override fun toString(): String
    {
        return "$id - $name"
    }

    //endregion

    //region Load, Save & Delete

    /**
     * Loads the team from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    override fun load(database: Database): Boolean
    {
        //try to open the DB if it is not open
        if (!database.isOpen) database.open()

        if (database.isOpen)
        {
            val teams = getTeams(null, null, this, database)
            val team = if (teams!!.size > 0) teams[0] else null

            if (team != null)
            {
                name = team.name
                city = team.city
                stateProvince = team.stateProvince
                country = team.country
                rookieYear = team.rookieYear
                facebookURL = team.facebookURL
                twitterURL = team.twitterURL
                instagramURL = team.instagramURL
                youtubeURL = team.youtubeURL
                websiteURL = team.websiteURL
                imageFileURI = team.imageFileURI
                return true
            }
        }

        return false
    }

    /**
     * Saves the team into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved team
     */
    override fun save(database: Database): Int
    {
        var id = -1

        //try to open the DB if it is not open
        if (!database.isOpen)
            database.open()

        if (database.isOpen)
            id = database.setTeam(this).toInt()

        //set the id if the save was successful
        if (id > 0)
            id = id

        return id
    }

    /**
     * Deletes the team from the database
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
            successful = database.deleteTeam(this)

        }

        return successful
    }

    //endregion
}

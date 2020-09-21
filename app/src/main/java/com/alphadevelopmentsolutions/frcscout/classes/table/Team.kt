package com.alphadevelopmentsolutions.frcscout.classes.table

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.alphadevelopmentsolutions.frcscout.classes.Database
import java.io.File

class Team(
        var id: Int = DEFAULT_INT,
        var name: String = DEFAULT_STRING,
        var city: String? = null,
        var stateProvince: String? = null,
        var country: String? = null,
        var rookieYear: Int? = null,
        var facebookURL: String? = null,
        var twitterURL: String? = null,
        var instagramURL: String? = null,
        var youtubeURL: String? = null,
        var websiteURL: String? = null,
        var imageFileURI: String? = null) : Table(TABLE_NAME, COLUMN_NAME_ID, CREATE_TABLE)
{
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
            clearTable(database, TABLE_NAME)
        }

        /**
         * Returns arraylist of teams with specified filters from database
         * @param event if specified, filters teams by event id
         * @param match if specified, filters teams by match id
         * @param team if specified, filters teams by team id
         * @param database used to load teams
         * @return arraylist of teams
         */
        fun getObjects(event: Event?, match: Match?, team: Team?, database: Database): ArrayList<Team>?
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
     * Calculates all stats for this [Team]
     * Highly recommended that this gets ran inside it's own thread
     * it will take a long time to process
     * @param year [Year] used to pull data
     * @param event [Event] used to pull data
     * @param matches [ArrayList] list of [Match] for stats
     * @param scoutCardInfoKeys [ArrayList] list of [ScoutCardInfoKey] for stats
     * @param scoutCardInfos [ArrayList] list of [ScoutCardInfo] for stats
     * @param database [Database] used to pull data
     * @return [HashMap] of stats
     */
    fun getStats(year: Year?, event: Event?, matches: ArrayList<Match>?, scoutCardInfoKeys: ArrayList<ScoutCardInfoKey>?, scoutCardInfos: ArrayList<ScoutCardInfo>?,  database: Database): HashMap<String, HashMap<String, Int>>
    {
        val teamStats = HashMap<String, HashMap<String, Int>>()

        val scoutCardInfoKeys = scoutCardInfoKeys ?: ScoutCardInfoKey.getObjects(year, null, database)
        val scoutCardInfos = scoutCardInfos ?: ScoutCardInfo.getObjects(event, null, this, null, null, false, database)
        val matches = matches ?: Match.getObjects(event, null, this, database)

        if (!matches.isNullOrEmpty() && !scoutCardInfoKeys.isNullOrEmpty() && !scoutCardInfos.isNullOrEmpty())
        {
            for(match in matches)
            {
                val statsHashMap = HashMap<String, Int>()

                //filter the cards that match this match
                val filteredScoutCardInfos = ArrayList<ScoutCardInfo>().apply {

                    for(scoutCard in scoutCardInfos)
                    {
                        if(scoutCard.matchId == match.key && scoutCard.teamId == id)
                            add(scoutCard)
                    }
                }

                //iterate through each scout card info key
                for(scoutCardInfoKey in scoutCardInfoKeys)
                {
                    if(scoutCardInfoKey.includeInStats == true)
                    {
                        if(!filteredScoutCardInfos.isNullOrEmpty())
                        {
                            for(scoutCardInfo in filteredScoutCardInfos)
                            {
                                if(scoutCardInfo.propertyKeyId == scoutCardInfoKey.serverId)
                                    statsHashMap[scoutCardInfoKey.toString()] = Integer.parseInt(scoutCardInfo.propertyValue) //get the most recent stat and set it
                            }

                            //if stat was never set, default to 0
                            if(statsHashMap[scoutCardInfoKey.toString()] == null)
                                statsHashMap[scoutCardInfoKey.toString()] = 0

                        }
                        else
                            statsHashMap[scoutCardInfoKey.toString()] = 0
                    }
                }

                //add the stats to the running list of stats per match
                teamStats[match.toString()] = statsHashMap
            }
        }

        return teamStats
    }

    /**
     * Gets all scout cards associated with the team
     * @param event if specified, filters scout cards by event id
     * @param match if specified, filters scout cards by match id
     * @param scoutCardInfo if specified, filters scout cards by scoutcard id
     * @param database used for loading cards
     * @param onlyDrafts boolean if you only want drafts
     * @return arraylist of scout cards
     */
    fun getScoutCards(event: Event?, match: Match?, scoutCardInfo: ScoutCardInfo?, onlyDrafts: Boolean, database: Database): ArrayList<ScoutCardInfo>?
    {
        return ScoutCardInfo.getObjects(event, match, this, null, scoutCardInfo, onlyDrafts, database)
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
            val teams = getObjects(null, null, this, database)
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
            this.id = id

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

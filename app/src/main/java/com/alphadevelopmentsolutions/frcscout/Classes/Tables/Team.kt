package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class Team(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        lastUpdated: Date = DEFAULT_DATE,
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
        var imageFileURI: String? = null) : Table(TABLE_NAME, localId, serverId, lastUpdated)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "teams"
        const val COLUMN_NAME_NAME = "Name"
        const val COLUMN_NAME_CITY = "City"
        const val COLUMN_NAME_STATEPROVINCE = "StateProvince"
        const val COLUMN_NAME_COUNTRY = "Country"
        const val COLUMN_NAME_ROOKIE_YEAR = "RookieYear"
        const val COLUMN_NAME_FACEBOOK_URL = "FacebookURL"
        const val COLUMN_NAME_TWITTER_URL = "TwitterURL"
        const val COLUMN_NAME_INSTAGRAM_URL = "InstagramURL"
        const val COLUMN_NAME_YOUTUBE_URL = "YoutubeURL"
        const val COLUMN_NAME_WEBSITE_URL = "WebsiteURL"
        const val COLUMN_NAME_IMAGE_FILE_URI = "ImageFileURI"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_NAME, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_CITY, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_STATEPROVINCE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_COUNTRY, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_ROOKIE_YEAR, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_FACEBOOK_URL, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_TWITTER_URL, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_INSTAGRAM_URL, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_YOUTUBE_URL, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_WEBSITE_URL, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_IMAGE_FILE_URI, SQLiteDataTypes.TEXT))
            }

        /**
         * Returns [ArrayList] of [Team] with specified filters from [database]
         * @param event if specified, filters [Team] by [event] id
         * @param match if specified, filters [Team] by [match] id
         * @param team if specified, filters [Team] by [team] id
         * @param database used to load [Team]
         * @return [ArrayList] of [Team]
         */
        fun getObjects(event: Event?, match: Match?, team: Team?, database: Database): ArrayList<Team> {
            return ArrayList<Team>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (event != null)
                {
                    whereStatement.append("$COLUMN_NAME_SERVER_ID IN (SELECT ${EventTeamList.COLUMN_NAME_TEAM_ID} FROM ${EventTeamList.TABLE_NAME} WHERE ${EventTeamList.COLUMN_NAME_EVENT_ID} = ?) ")
                    whereArgs.add(event.blueAllianceId)
                }

                //filter by object
                if (match != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "")
                            .append("($COLUMN_NAME_SERVER_ID IN (SELECT ${Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID} FROM ${Match.TABLE_NAME} WHERE ${Match.COLUMN_NAME_KEY} = ?) OR ")
                            .append("($COLUMN_NAME_SERVER_ID IN (SELECT ${Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID} FROM ${Match.TABLE_NAME} WHERE ${Match.COLUMN_NAME_KEY} = ?) OR ")
                            .append("($COLUMN_NAME_SERVER_ID IN (SELECT ${Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID} FROM ${Match.TABLE_NAME} WHERE ${Match.COLUMN_NAME_KEY} = ?) OR ")

                            .append("($COLUMN_NAME_SERVER_ID IN (SELECT ${Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID} FROM ${Match.TABLE_NAME} WHERE ${Match.COLUMN_NAME_KEY} = ?) OR ")
                            .append("($COLUMN_NAME_SERVER_ID IN (SELECT ${Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID} FROM ${Match.TABLE_NAME} WHERE ${Match.COLUMN_NAME_KEY} = ?) OR ")
                            .append("($COLUMN_NAME_SERVER_ID IN (SELECT ${Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID} FROM ${Match.TABLE_NAME} WHERE ${Match.COLUMN_NAME_KEY} = ?) OR ")

                    whereArgs.add(match.key)
                    whereArgs.add(match.key)
                    whereArgs.add(match.key)
                    whereArgs.add(match.key)
                    whereArgs.add(match.key)
                    whereArgs.add(match.key)
                }

                //filter by object
                if (team != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("${if(team.localId != DEFAULT_LONG) COLUMN_NAME_LOCAL_ID else COLUMN_NAME_SERVER_ID} = ? ")
                    whereArgs.add(
                                if(team.localId != DEFAULT_LONG)
                                    team.localId.toString()
                                else
                                    team.serverId.toString()
                            )
                }

                //add all object records to array list
                with(database.getObjects(
                        TABLE_NAME,
                        whereStatement.toString(),
                        whereArgs,
                        null,
                        "$COLUMN_NAME_SERVER_ID ASC"))
                {
                    if (this != null) {
                        while (moveToNext()) {
                            add(
                                Team(
                                    getLong(COLUMN_NAME_LOCAL_ID),
                                    getLong(COLUMN_NAME_SERVER_ID),
                                    getDate(COLUMN_NAME_LAST_UPDATED),
                                    getString(COLUMN_NAME_NAME),
                                    getStringOrNull(COLUMN_NAME_CITY),
                                    getStringOrNull(COLUMN_NAME_STATEPROVINCE),
                                    getStringOrNull(COLUMN_NAME_COUNTRY),
                                    getIntOrNull(COLUMN_NAME_ROOKIE_YEAR),
                                    getStringOrNull(COLUMN_NAME_FACEBOOK_URL),
                                    getStringOrNull(COLUMN_NAME_TWITTER_URL),
                                    getStringOrNull(COLUMN_NAME_INSTAGRAM_URL),
                                    getStringOrNull(COLUMN_NAME_YOUTUBE_URL),
                                    getStringOrNull(COLUMN_NAME_WEBSITE_URL),
                                    getStringOrNull(COLUMN_NAME_IMAGE_FILE_URI)
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
                        if(scoutCard.matchId == match.key && scoutCard.teamId == serverId)
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
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "$serverId - $name"
    }

    /**
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(null, null, this, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    this@Team.loadParentValues(this)
                    this@Team.name = name
                    this@Team.city = city
                    this@Team.stateProvince = stateProvince
                    this@Team.country = country
                    this@Team.rookieYear = rookieYear
                    this@Team.facebookURL = facebookURL
                    this@Team.twitterURL = twitterURL
                    this@Team.instagramURL = instagramURL
                    this@Team.youtubeURL = youtubeURL
                    this@Team.websiteURL = websiteURL
                    this@Team.imageFileURI = imageFileURI
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
            put(COLUMN_NAME_NAME, name)
            put(COLUMN_NAME_CITY, city)
            put(COLUMN_NAME_STATEPROVINCE, stateProvince)
            put(COLUMN_NAME_COUNTRY, country)
            put(COLUMN_NAME_ROOKIE_YEAR, rookieYear)
            put(COLUMN_NAME_FACEBOOK_URL, facebookURL)
            put(COLUMN_NAME_TWITTER_URL, twitterURL)
            put(COLUMN_NAME_INSTAGRAM_URL, instagramURL)
            put(COLUMN_NAME_YOUTUBE_URL, youtubeURL)
            put(COLUMN_NAME_WEBSITE_URL, websiteURL)
            put(COLUMN_NAME_IMAGE_FILE_URI, imageFileURI)
        }
}

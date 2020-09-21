package com.alphadevelopmentsolutions.frcscout.api

import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.NullableJSONObject
import com.alphadevelopmentsolutions.frcscout.classes.table.*
import com.alphadevelopmentsolutions.frcscout.exception.ApiException
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.interfaces.HttpResponseCodes
import com.alphadevelopmentsolutions.frcscout.R
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat

abstract class Api internal constructor(
        private val context: MainActivity,
        var url: String,
        private val apiKey: String,
        private val postData: HashMap<String, String>)
{
    private val MYSQL_DATE_FORMAT = "yyyy-MM-dd H:mm:ss"
    protected var simpleDateFormat: SimpleDateFormat

    protected lateinit var apiResponse: ApiResponse

    init
    {
        url = Constants.API_URL + if(url.isNotEmpty() && url[url.length - 1] != '/') "$url/" else url

        simpleDateFormat = SimpleDateFormat(MYSQL_DATE_FORMAT)
    }

    /**
     * Formats the post data from the hashmap
     * @return formatted post data
     */
    @Throws(UnsupportedEncodingException::class)
    fun getPostData(): String
    {
        val formattedPostData = StringBuilder()

        //Specify version of current API
        formattedPostData.append("$API_PARAM_API_KEY=").append(apiKey)

        //add each post data to the string builder
        for ((key1, value) in postData)
        {
            //replace all spaced with %20
            val parsedKey = URLEncoder.encode(key1, "UTF-8")
            val parsedValue = URLEncoder.encode(value, "UTF-8")

            formattedPostData
                    .append("&")
                    .append(parsedKey)
                    .append("=")
                    .append(parsedValue)
        }

        //return the formatted data
        return formattedPostData.toString()
    }

    abstract fun execute(): Boolean

    /**
     * Queries the server and gets a response back
     */
    @Throws(ApiException::class)
    fun query(api: Api)
    {
        apiResponse = ApiResponse(api).parse()
        with(apiResponse)
        {
            if(responseCode != HttpResponseCodes.OK)
                throw ApiException(responseCode, response)
        }
    }

    companion object
    {

        @JvmStatic
        protected val API_PARAM_API_KEY = "ApiKey"

        @JvmStatic
        protected val API_PARAM_API_VERSION = "apiVersion"

        @JvmStatic
        protected val API_PARAM_API_ACTION = "Action"

    }

    //region Connect

    abstract class Connect internal constructor(
            context: MainActivity,
            apiKey: String,
            postData: HashMap<String, String>) :
            Api(
                    context,
                    "",
                    apiKey,
                    postData
            )
    {

        /**
         * Attempts to establish a connection to the API
         */
        class Hello(context: MainActivity) : Connect(context, "", HashMap())
        {
            override fun execute(): Boolean
            {
                return try
                {
                    query(this)
                    true
                }
                catch (e: ApiException)
                {
                    AppLog.error(e)
                    false
                }

            }
        }
    }

    //endregion

    //region Account

    abstract class Account internal constructor(
            context: MainActivity,
            apiKey: String,
            postData: java.util.HashMap<String, String>) :
            Api(
                    context,
                    "account",
                    apiKey,
                    postData
            )
    {

        /**
         * Attempts to log into the web server
         */
        class Login(
                private val context: MainActivity,
                username: String,
                password: String,
                captchaKey: String
        ) :
                Account(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : java.util.HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "Login")
                                put("Username", username)
                                put("Password", password)
                                put("CaptchaKey", captchaKey)
                            }
                        }
                )
        {

            override fun execute(): Boolean
            {
                return try
                {
                    query(this)

                    //populate all config values
                    for(i in 0 until apiResponse.response.length())
                    {
                        val serverConfigObject = apiResponse.response.getJSONObject(i)
                        val key = serverConfigObject.getString("Key")
                        val value = serverConfigObject.getString("Value")

                        context.keyStore.setPreference(key, if(serverConfigObject.getString("Value").toIntOrNull() == null) value else value.toInt())
                    }

                    true
                }
                catch (e: ApiException)
                {
                    AppLog.error(e)
                    false
                }

            }
        }

    }

    //endregion

    //region Get

    abstract class Get internal constructor(
            context: MainActivity,
            apiKey: String,
            postData: HashMap<String, String>) :
            Api(
                    context,
                    "get",
                    apiKey,
                    postData
            )
    {

        /**
         * Gets configs from the server
         */
        class ServerConfig(private val context: MainActivity) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetServerConfig")
                            }
                        }
                )
        {
            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    for(i in 0 until apiResponse.response.length())
                    {
                        val serverConfigObject = apiResponse.response.getJSONObject(i)
                        val key = serverConfigObject.getString("Key")
                        val value = serverConfigObject.getString("Value")

                        context.keyStore.setPreference(key, if(serverConfigObject.getString("Value").toIntOrNull() == null) value else value.toInt())
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.app_config)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [Team] records from the web server
         * @param event if specified, filters [Team] records by [Event]
         */
        class Teams(
                private val context: MainActivity,
                event: Event? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetTeams")
                                put("EventId", event?.blueAllianceId ?: "")
                            }
                        }
                )
        {
            val teams: ArrayList<Team> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val teamObject = NullableJSONObject(apiResponse.response.getJSONObject(i))

                        val teamId = teamObject.getInt(Team.COLUMN_NAME_ID)
                        val name = teamObject.getString(Team.COLUMN_NAME_NAME)
                        val city = teamObject.getStringOrNull(Team.COLUMN_NAME_CITY)
                        val stateProvince = teamObject.getStringOrNull(Team.COLUMN_NAME_STATEPROVINCE)
                        val country = teamObject.getStringOrNull(Team.COLUMN_NAME_COUNTRY)
                        val rookieYear = teamObject.getIntOrNull(Team.COLUMN_NAME_ROOKIE_YEAR)
                        val facebookURL = teamObject.getStringOrNull(Team.COLUMN_NAME_FACEBOOK_URL)
                        val twitterURL = teamObject.getStringOrNull(Team.COLUMN_NAME_TWITTER_URL)
                        val instagramURL = teamObject.getStringOrNull(Team.COLUMN_NAME_INSTAGRAM_URL)
                        val youtubeURL = teamObject.getStringOrNull(Team.COLUMN_NAME_YOUTUBE_URL)
                        val websiteUrl = teamObject.getStringOrNull(Team.COLUMN_NAME_WEBSITE_URL)

                        teams.add(Team(
                                teamId,
                                name,
                                city,
                                stateProvince,
                                country,
                                rookieYear,
                                if (facebookURL == "null") "" else "https://www.facebook.com/$facebookURL",
                                if (twitterURL == "null") "" else "https://www.twitter.com/$twitterURL",
                                if (instagramURL == "null") "" else "https://www.instagram.com/$instagramURL",
                                if (youtubeURL == "null") "" else "https://www.youtube.com/$youtubeURL",
                                websiteUrl,
                                ""))
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.teams)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [EventTeamList] records from the web server
         * @param event if specified, filters [EventTeamList] records by [Event]
         */
        class EventTeamList(
                private val context: MainActivity,
                event: Event? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetEventTeamList")
                                put("EventId", event?.blueAllianceId ?: "")
                            }
                        }
                )
        {
            val eventTeamList: ArrayList<com.alphadevelopmentsolutions.frcscout.classes.table.EventTeamList> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val eventTeamListObj = apiResponse.response.getJSONObject(i)

                        with(EventTeamList)
                        {
                            val teamId = eventTeamListObj.getInt(COLUMN_NAME_TEAM_ID)
                            val eventId = eventTeamListObj.getString(COLUMN_NAME_EVENT_ID)

                            eventTeamList.add(EventTeamList(
                                    -1,
                                    teamId,
                                    eventId))
                        }
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.event_metadata)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [User] records from the web server
         */
        class Users(private val context: MainActivity) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetUsers")
                            }
                        }
                )
        {
            val users: ArrayList<User> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val teamObject = apiResponse.response.getJSONObject(i)

                        val firstName = teamObject.getString(User.COLUMN_NAME_FIRST_NAME)
                        val lastName = teamObject.getString(User.COLUMN_NAME_LAST_NAME)

                        users.add(User(-1, firstName, lastName))
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.users)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [ScoutCardInfo] records from the web server
         * @param event if specified, filters [ScoutCardInfo] records by [Event]
         */
        class ScoutCardInfo(
                private val context: MainActivity,
                event: Event? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetScoutCardInfo")
                                put("EventId", event?.blueAllianceId ?: "")
                            }
                        }
                )
        {
            val scoutCardInfos: ArrayList<com.alphadevelopmentsolutions.frcscout.classes.table.ScoutCardInfo> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val scoutCardInfoObject = apiResponse.response.getJSONObject(i)

                        with(ScoutCardInfo)
                        {
                            val yearId = scoutCardInfoObject.getInt(COLUMN_NAME_YEAR_ID)
                            val eventId = scoutCardInfoObject.getString(COLUMN_NAME_EVENT_ID)
                            val matchId = scoutCardInfoObject.getString(COLUMN_NAME_MATCH_ID)
                            val teamId = scoutCardInfoObject.getInt(COLUMN_NAME_TEAM_ID)

                            val completedBy = scoutCardInfoObject.getString(COLUMN_NAME_COMPLETED_BY)

                            val propertyValue = scoutCardInfoObject.getString(COLUMN_NAME_PROPERTY_VALUE)
                            val propertyKeyId = scoutCardInfoObject.getInt(COLUMN_NAME_PROPERTY_KEY_ID)

                            scoutCardInfos.add(
                                    ScoutCardInfo(
                                            -1,
                                            yearId,
                                            eventId,
                                            matchId,
                                            teamId,

                                            completedBy,

                                            propertyValue,
                                            propertyKeyId,

                                            false))
                        }
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.scout_cards)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [ScoutCardInfoKey] records from the web server
         * @param year if specified, filters [ScoutCardInfoKey] records by [Year]
         */
        class ScoutCardInfoKeys(
                private val context: MainActivity,
                year: Year? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetScoutCardInfoKeys")
                                put("YearId", year?.serverId?.toString() ?: "")
                            }
                        }
                )
        {
            val scoutCardInfoKeys: ArrayList<ScoutCardInfoKey> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val robotInfoKeyObject = NullableJSONObject(apiResponse.response.getJSONObject(i))

                        val serverId = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_SERVER_ID)
                        val yearId = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID)

                        val keyState = robotInfoKeyObject.getString(ScoutCardInfoKey.COLUMN_NAME_KEY_STATE)
                        val keyName = robotInfoKeyObject.getString(ScoutCardInfoKey.COLUMN_NAME_KEY_NAME)

                        val sortOrder = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER)

                        val minValue = robotInfoKeyObject.getIntOrNull(ScoutCardInfoKey.COLUMN_NAME_MIN_VALUE)
                        val maxValue = robotInfoKeyObject.getIntOrNull(ScoutCardInfoKey.COLUMN_NAME_MAX_VALUE)

                        val nullZeros = robotInfoKeyObject.getBoolean(ScoutCardInfoKey.COLUMN_NAME_NULL_ZEROS)
                        val includeInStats = robotInfoKeyObject.getBoolean(ScoutCardInfoKey.COLUMN_NAME_INCLUDE_IN_STATS)

                        val dataType = ScoutCardInfoKey.DataTypes.parseString(robotInfoKeyObject.getString(ScoutCardInfoKey.COLUMN_NAME_DATA_TYPE))

                        scoutCardInfoKeys.add(ScoutCardInfoKey(
                                -1,

                                serverId,
                                yearId,

                                keyState,
                                keyName,

                                sortOrder,

                                minValue,
                                maxValue,

                                nullZeros,
                                includeInStats,

                                dataType
                        ))
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.scout_card_metadata)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [RobotInfo] records from the web server
         * @param event if specified, filters [RobotInfo] records by [Event]
         */
        class RobotInfo(
                private val context: MainActivity,
                event: Event? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetRobotInfo")
                                put("EventId", event?.blueAllianceId ?: "")
                            }
                        }
                )
        {
            val robotInfoList: ArrayList<com.alphadevelopmentsolutions.frcscout.classes.table.RobotInfo> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val robotInfoObject = apiResponse.response.getJSONObject(i)

                        with(RobotInfo)
                        {
                            val yearId = robotInfoObject.getInt(COLUMN_NAME_YEAR_ID)
                            val eventId = robotInfoObject.getString(COLUMN_NAME_EVENT_ID)
                            val teamId = robotInfoObject.getInt(COLUMN_NAME_TEAM_ID)

                            val propertyValue = robotInfoObject.getString(COLUMN_NAME_PROPERTY_VALUE)
                            val propertyKeyId = robotInfoObject.getInt(COLUMN_NAME_PROPERTY_KEY_ID)

                            robotInfoList.add(RobotInfo(
                                    -1,
                                    yearId,
                                    eventId,
                                    teamId,

                                    propertyValue,
                                    propertyKeyId,

                                    false
                            ))
                        }
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.robot_info)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [RobotInfoKey] records from the web server
         * @param year if specified, filters [RobotInfoKey] records by [Year]
         */
        class RobotInfoKeys(
                private val context: MainActivity,
                year: Year? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetRobotInfoKeys")
                                put("YearId", year?.serverId?.toString() ?: "")
                            }
                        }
                )
        {
            val robotInfoKeyList: ArrayList<RobotInfoKey> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val robotInfoKeyObject = apiResponse.response.getJSONObject(i)

                        val serverId = robotInfoKeyObject.getInt(RobotInfoKey.COLUMN_NAME_SERVER_ID)
                        val yearId = robotInfoKeyObject.getInt(RobotInfoKey.COLUMN_NAME_YEAR_ID)

                        val keyState = robotInfoKeyObject.getString(RobotInfoKey.COLUMN_NAME_KEY_STATE)
                        val keyName = robotInfoKeyObject.getString(RobotInfoKey.COLUMN_NAME_KEY_NAME)

                        val sortOrder = robotInfoKeyObject.getInt(RobotInfoKey.COLUMN_NAME_SORT_ORDER)

                        robotInfoKeyList.add(RobotInfoKey(
                                -1,
                                serverId,
                                yearId,

                                keyState,
                                keyName,

                                sortOrder
                        ))
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.robot_info_metadata)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [Match] records from the web server
         * @param event if specified, filters [Match] records by [Event]
         */
        class Matches(
                private val context: MainActivity,
                event: Event? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetMatches")
                                put("EventId", event?.blueAllianceId ?: "")
                            }
                        }
                )
        {
            val matches: ArrayList<Match> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val matchObject = NullableJSONObject(apiResponse.response.getJSONObject(i))

                        val date = simpleDateFormat.parse(matchObject.getString(Match.COLUMN_NAME_DATE))
                        val eventId = matchObject.getString(Match.COLUMN_NAME_EVENT_ID)
                        val key = matchObject.getString(Match.COLUMN_NAME_KEY)
                        val matchType = Match.Type.getTypeFromString(matchObject.getString(Match.COLUMN_NAME_MATCH_TYPE))
                        val setNumber = matchObject.getInt(Match.COLUMN_NAME_SET_NUMBER)
                        val matchNumber = matchObject.getInt(Match.COLUMN_NAME_MATCH_NUMBER)

                        val blueAllianceTeamOneId = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID)
                        val blueAllianceTeamTwoId = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID)
                        val blueAllianceTeamThreeId = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID)

                        val redAllianceTeamOneId = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID)
                        val redAllianceTeamTwoId = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID)
                        val redAllianceTeamThreeId = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID)

                        val blueAllianceScore = matchObject.getIntOrNull(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE)
                        val redAllianceScore = matchObject.getIntOrNull(Match.COLUMN_NAME_RED_ALLIANCE_SCORE)


                        matches.add(Match(
                                -1,
                                date,
                                eventId,
                                key,
                                matchType,
                                setNumber,
                                matchNumber,

                                blueAllianceTeamOneId,
                                blueAllianceTeamTwoId,
                                blueAllianceTeamThreeId,

                                redAllianceTeamOneId,
                                redAllianceTeamTwoId,
                                redAllianceTeamThreeId,

                                blueAllianceScore,
                                redAllianceScore
                        ))
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.matches)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [RobotMedia] records from the web server
         * @param team if specified, filters [RobotMedia] records by [Team]
         */
        class RobotMedia(
                private val context: MainActivity,
                team: Team? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetRobotMedia")
                                put(com.alphadevelopmentsolutions.frcscout.classes.table.RobotMedia.COLUMN_NAME_TEAM_ID, team?.id?.toString() ?: "")
                            }
                        }
                )
        {
            val robotMedia: ArrayList<com.alphadevelopmentsolutions.frcscout.classes.table.RobotMedia> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val robotMediaJson = apiResponse.response.getJSONObject(i)

                        with(RobotMedia)
                        {
                            val yearId = robotMediaJson.getInt(COLUMN_NAME_YEAR_ID)
                            val eventId = robotMediaJson.getString(COLUMN_NAME_EVENT_ID)
                            val teamId = robotMediaJson.getInt(COLUMN_NAME_TEAM_ID)
                            var fileUri = Constants.WEB_URL + "/assets/robot-media/originals/" + context.keyStore.getPreference(Constants.SharedPrefKeys.TEAM_ROBOT_MEDIA_DIR_KEY, "") + "/" + robotMediaJson.getString(COLUMN_NAME_FILE_URI)

                            fileUri = apiResponse.downloadImage(fileUri, Constants.ROBOT_MEDIA_DIRECTORY, context).absolutePath

                            robotMedia.add(RobotMedia(
                                    -1,
                                    yearId,
                                    eventId,
                                    teamId,
                                    fileUri,
                                    false)
                            )
                        }
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.robot_media)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [Year] records from the web server
         */
        class Years(private val context: MainActivity) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetYears")
                            }
                        }
                )
        {
            val years: ArrayList<Year> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val yearsJson = apiResponse.response.getJSONObject(i)

                        val serverId = yearsJson.getInt(Year.COLUMN_NAME_SERVER_ID)
                        val name = yearsJson.getString(Year.COLUMN_NAME_NAME)
                        val startDate = simpleDateFormat.parse(yearsJson.getString(Year.COLUMN_NAME_START_DATE))
                        val endDate = simpleDateFormat.parse(yearsJson.getString(Year.COLUMN_NAME_END_DATE))
                        var fileUri = Constants.WEB_URL + "/assets/year-media/" + yearsJson.getString(Year.COLUMN_NAME_IMAGE_URI)

                        fileUri = apiResponse.downloadImage(fileUri, Constants.YEAR_MEDIA_DIRECTORY, context).absolutePath

                        years.add(Year(
                                -1,
                                serverId,
                                name,
                                startDate,
                                endDate,
                                fileUri
                        ))

                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.years)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [Event] records from the web server
         * @param team if specified, filters [Event] records by [Team]
         */
        class Events(
                private val context: MainActivity,
                private val team: Team? = null) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetEvents")
                                put("TeamId", team?.id?.toString() ?: "")
                            }
                        }
                )
        {
            val events: ArrayList<Event> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val eventObject = apiResponse.response.getJSONObject(i)

                        val yearId = eventObject.getInt(Event.COLUMN_NAME_YEAR_ID)
                        val blueAllianceId = eventObject.getString(Event.COLUMN_NAME_BLUE_ALLIANCE_ID)
                        val name = eventObject.getString(Event.COLUMN_NAME_NAME)
                        val city = eventObject.getString(Event.COLUMN_NAME_CITY)
                        val stateProvince = eventObject.getString(Event.COLUMN_NAME_STATEPROVINCE)
                        val country = eventObject.getString(Event.COLUMN_NAME_COUNTRY)
                        val startDate = eventObject.getString(Event.COLUMN_NAME_START_DATE)
                        val endDate = eventObject.getString(Event.COLUMN_NAME_END_DATE)

                        events.add(Event(
                                -1,
                                yearId,
                                blueAllianceId,
                                name,
                                city,
                                stateProvince,
                                country,
                                simpleDateFormat.parse(startDate),
                                simpleDateFormat.parse(endDate)))
                    }


                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.events)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [ChecklistItem] records from the web server
         */
        class ChecklistItems(private val context: MainActivity) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetChecklistItems")
                            }
                        }
                )
        {
            val checklistItems: ArrayList<ChecklistItem> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    //iterate through, create a new object and add it to the arraylist
                    for (i in 0 until apiResponse.response.length())
                    {
                        val checklistItemObject = apiResponse.response.getJSONObject(i)

                        val serverId = checklistItemObject.getInt(ChecklistItem.COLUMN_NAME_SERVER_ID)

                        val title = checklistItemObject.getString(ChecklistItem.COLUMN_NAME_TITLE)
                        val description = checklistItemObject.getString(ChecklistItem.COLUMN_NAME_DESCRIPTION)

                        checklistItems.add(
                                ChecklistItem(
                                        -1,
                                        serverId,
                                        title,
                                        description))
                    }

                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.checklist)))
                    AppLog.error(e)
                    return false
                }

            }
        }

        /**
         * Gets [ChecklistItemResult] records from the web server
         */
        class ChecklistItemResults(private val context: MainActivity) :
                Get(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "GetChecklistItemResults")
                            }
                        }
                )
        {
            val checklistItemResults: ArrayList<ChecklistItemResult> = ArrayList()

            override fun execute(): Boolean
            {
                try
                {
                    query(this)

                    for (i in 0 until apiResponse.response.length())
                    {
                        val checklistItemResultObject = NullableJSONObject(apiResponse.response.getJSONObject(i))

                        val checklistItemId = checklistItemResultObject.getInt(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID)

                        val matchId = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_MATCH_ID)
                        val status = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_STATUS)
                        val completedBy = checklistItemResultObject.getStringOrNull(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY)

                        val completedDate = with(checklistItemResultObject.getStringOrNull(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE)) { if (this != null) simpleDateFormat.parse(this) else null }

                        checklistItemResults.add(
                                ChecklistItemResult(
                                        -1,
                                        checklistItemId,
                                        matchId,

                                        status,
                                        completedBy,

                                        completedDate,
                                        false
                                ))
                    }


                    return true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_get_failure), context.getString(R.string.checklist_results)))
                    AppLog.error(e)
                    return false
                }

            }
        }
    }

    //endregion

    //region Set

    abstract class Set internal constructor(
            context: MainActivity,
            apiKey: String,
            postData: HashMap<String, String>) :
            Api(
                    context,
                    "set",
                    apiKey,
                    postData
            )
    {
        /**
         * Submits a [RobotInfo] record to the web server
         */
        class RobotInfo(
                private val context: MainActivity,
                robotInfo: com.alphadevelopmentsolutions.frcscout.classes.table.RobotInfo) :
                Set(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "SubmitRobotInfo")

                                with(RobotInfo)
                                {
                                    put(COLUMN_NAME_YEAR_ID, robotInfo.yearId.toString())
                                    put(COLUMN_NAME_EVENT_ID, robotInfo.eventId)
                                    put(COLUMN_NAME_TEAM_ID, robotInfo.teamId.toString())

                                    put(COLUMN_NAME_PROPERTY_VALUE, robotInfo.propertyValue)
                                    put(COLUMN_NAME_PROPERTY_KEY_ID, robotInfo.propertyKeyId.toString())
                                }
                            }
                        }
                )
        {
            override fun execute(): Boolean
            {
                return try
                {
                    query(this)

                    true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_submit_failure), context.getString(R.string.robot_info)))
                    AppLog.error(e)
                    false
                }

            }
        }

        /**
         * Submits a [ScoutCardInfo] record to the web server
         */
        class ScoutCardInfo(
                private val context: MainActivity,
                scoutCardInfo: com.alphadevelopmentsolutions.frcscout.classes.table.ScoutCardInfo) :
                Set(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "SubmitScoutCardInfo")

                                with(ScoutCardInfo)
                                {
                                    put(COLUMN_NAME_YEAR_ID, scoutCardInfo.yearId.toString())
                                    put(COLUMN_NAME_EVENT_ID, scoutCardInfo.eventId)
                                    put(COLUMN_NAME_MATCH_ID, scoutCardInfo.matchId)
                                    put(COLUMN_NAME_TEAM_ID, scoutCardInfo.teamId.toString())

                                    put(COLUMN_NAME_COMPLETED_BY, scoutCardInfo.completedBy)

                                    put(COLUMN_NAME_PROPERTY_VALUE, scoutCardInfo.propertyValue)
                                    put(COLUMN_NAME_PROPERTY_KEY_ID, scoutCardInfo.propertyKeyId.toString())
                                }
                            }
                        }
                )
        {
            override fun execute(): Boolean
            {
                return try
                {
                    query(this)

                    true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_submit_failure), context.getString(R.string.scout_cards)))
                    AppLog.error(e)
                    false
                }

            }
        }

        /**
         * Submits a [RobotMedia] record to the web server
         */
        class RobotMedia(
                private val context: MainActivity,
                robotMedia: com.alphadevelopmentsolutions.frcscout.classes.table.RobotMedia) :
                Set(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "SubmitRobotMedia")

                                with(RobotMedia)
                                {
                                    put(COLUMN_NAME_YEAR_ID, robotMedia.yearId.toString())
                                    put(COLUMN_NAME_EVENT_ID, robotMedia.eventId)
                                    put(COLUMN_NAME_TEAM_ID, robotMedia.teamId.toString())
                                    put(COLUMN_NAME_FILE_URI, robotMedia.base64Image!!)
                                }
                            }
                        }
                )
        {
            override fun execute(): Boolean
            {
                return try
                {
                    query(this)

                    true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_submit_failure), context.getString(R.string.robot_media)))
                    AppLog.error(e)
                    false
                }

            }
        }

        /**
         * Submits a [ChecklistItemResult] record to the web server
         */
        class ChecklistItemResult(
                private val context: MainActivity,
                checklistItemResult: com.alphadevelopmentsolutions.frcscout.classes.table.ChecklistItemResult) :
                Set(
                        context,
                        context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(),
                        object : HashMap<String, String>()
                        {
                            init
                            {
                                put(API_PARAM_API_ACTION, "SubmitChecklistItemResult")

                                with(ChecklistItemResult)
                                {
                                    put(COLUMN_NAME_CHECKLIST_ITEM_ID, checklistItemResult.checklistItemId.toString())
                                    put(COLUMN_NAME_MATCH_ID, checklistItemResult.matchId)

                                    put(COLUMN_NAME_STATUS, checklistItemResult.status)
                                    put(COLUMN_NAME_COMPLETED_BY, checklistItemResult.completedBy ?: "")

                                    put(COLUMN_NAME_COMPLETED_DATE, checklistItemResult.completedDateForSQL)
                                }
                            }
                        }
                )
        {
            override fun execute(): Boolean
            {
                return try
                {
                    query(this)

                    true
                }
                catch (e: ApiException)
                {
                    context.showSnackbar(String.format(context.getString(R.string.server_submit_failure), context.getString(R.string.checklist_results)))
                    AppLog.error(e)
                    false
                }

            }
        }
    }

    //endregion
}


package com.alphadevelopmentsolutions.frcscout.Api

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

abstract class Server internal constructor(
        context: MainActivity,
        key: String,
        postData: HashMap<String, String>) : Api(context, key, postData)
{

    //region Getters

    class Hello(private val context: MainActivity) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "Hello")
        }
    })
    {

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                //could not connect to server

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(context.getString(R.string.server_error))

                return true
            } catch (e: Exception)
            {
                return false
            }

        }
    }

    class GetServerConfig(private val context: MainActivity) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetServerConfig")
        }
    })
    {

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))

                val responseArray = response.getJSONArray(API_FIELD_NAME_RESPONSE)

                for(i in 0 until responseArray.length())
                {
                    val serverConfigObject = responseArray.getJSONObject(i)
                    val key = serverConfigObject.getString("Key")
                    val value = serverConfigObject.getString("Value")

                    context.keyStore.setPreference(key, if(serverConfigObject.getString("Value").toIntOrNull() == null) value else value.toInt())
                }

                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }
    }

    class GetTeams(private val context: MainActivity, event: Event? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetTeams")
            put("EventId", event?.blueAllianceId ?: "")
        }
    })
    {
        //region Getters

        val teams: ArrayList<Team> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val teamObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val teamId = teamObject.getInt(Team.COLUMN_NAME_ID)
                    val name = teamObject.getString(Team.COLUMN_NAME_NAME)
                    val city = teamObject.getString(Team.COLUMN_NAME_CITY)
                    val stateProvince = teamObject.getString(Team.COLUMN_NAME_STATEPROVINCE)
                    val country = teamObject.getString(Team.COLUMN_NAME_COUNTRY)
                    val rookieYear = teamObject.getInt(Team.COLUMN_NAME_ROOKIE_YEAR)
                    val facebookURL = teamObject.getString(Team.COLUMN_NAME_FACEBOOK_URL)
                    val twitterURL = teamObject.getString(Team.COLUMN_NAME_TWITTER_URL)
                    val instagramURL = teamObject.getString(Team.COLUMN_NAME_INSTAGRAM_URL)
                    val youtubeURL = teamObject.getString(Team.COLUMN_NAME_YOUTUBE_URL)
                    val websiteUrl = teamObject.getString(Team.COLUMN_NAME_WEBSITE_URL)

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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }


        //endregion
    }

    class GetEventTeamList(private val context: MainActivity, event: Event? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetEventTeamList")
            put("EventId", event?.blueAllianceId ?: "")
        }
    })
    {
        //region Getters

        val eventTeamList: ArrayList<EventTeamList> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val eventTeamListObj = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val teamId = eventTeamListObj.getInt(EventTeamList.COLUMN_NAME_TEAM_ID)
                    val eventId = eventTeamListObj.getString(EventTeamList.COLUMN_NAME_EVENT_ID)

                    eventTeamList.add(EventTeamList(
                            -1,
                            teamId,
                            eventId))
                }

                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }
    }

    class GetUsers(private val context: MainActivity) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetUsers")
        }
    })
    {
        //region Getters

        val users: ArrayList<User> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val teamObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val firstName = teamObject.getString(User.COLUMN_NAME_FIRST_NAME)
                    val lastName = teamObject.getString(User.COLUMN_NAME_LAST_NAME)

                    users.add(User(-1, firstName, lastName))
                }

                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }


        //endregion
    }

    class GetScoutCardInfo(private val context: MainActivity, event: Event? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetScoutCardInfo")
            put("EventId", event?.blueAllianceId ?: "")
        }
    })
    {
        //region Getters

        val scoutCardInfos: ArrayList<ScoutCardInfo> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val scoutCardInfoObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val yearId = scoutCardInfoObject.getInt(ScoutCardInfo.COLUMN_NAME_YEAR_ID)
                    val eventId = scoutCardInfoObject.getString(ScoutCardInfo.COLUMN_NAME_EVENT_ID)
                    val matchId = scoutCardInfoObject.getString(ScoutCardInfo.COLUMN_NAME_MATCH_ID)
                    val teamId = scoutCardInfoObject.getInt(ScoutCardInfo.COLUMN_NAME_TEAM_ID)

                    val completedBy = scoutCardInfoObject.getString(ScoutCardInfo.COLUMN_NAME_COMPLETED_BY)

                    val propertyValue = scoutCardInfoObject.getString(ScoutCardInfo.COLUMN_NAME_PROPERTY_VALUE)
                    val propertyKeyId = scoutCardInfoObject.getInt(ScoutCardInfo.COLUMN_NAME_PROPERTY_KEY_ID)

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

                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }


        //endregion
    }

    class GetScoutCardInfoKeys(private val context: MainActivity, year: Year? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetScoutCardInfoKeys")
            put("YearId", year?.serverId?.toString() ?: "")
        }
    })
    {
        //region Getters

        val scoutCardInfoKeys: ArrayList<ScoutCardInfoKey> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val robotInfoKeyObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val serverId = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_SERVER_ID)
                    val yearId = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID)

                    val keyState = robotInfoKeyObject.getString(ScoutCardInfoKey.COLUMN_NAME_KEY_STATE)
                    val keyName = robotInfoKeyObject.getString(ScoutCardInfoKey.COLUMN_NAME_KEY_NAME)

                    val sortOrder = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER)

                    val minValue = if (robotInfoKeyObject.isNull(ScoutCardInfoKey.COLUMN_NAME_MIN_VALUE)) null else robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_MIN_VALUE)
                    val maxValue = if (robotInfoKeyObject.isNull(ScoutCardInfoKey.COLUMN_NAME_MAX_VALUE)) null else robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_MAX_VALUE)

                    val nullZeros = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_NULL_ZEROS) == 1
                    val includeInStats = robotInfoKeyObject.getInt(ScoutCardInfoKey.COLUMN_NAME_INCLUDE_IN_STATS) == 1

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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }


        //endregion
    }

    class GetRobotInfo(private val context: MainActivity, event: Event? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetRobotInfo")
            put("EventId", event?.blueAllianceId ?: "")
        }
    })
    {
        //region Getters

        val robotInfoList: ArrayList<RobotInfo> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val robotInfoObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val yearId = robotInfoObject.getInt(RobotInfo.COLUMN_NAME_YEAR_ID)
                    val eventId = robotInfoObject.getString(RobotInfo.COLUMN_NAME_EVENT_ID)
                    val teamId = robotInfoObject.getInt(RobotInfo.COLUMN_NAME_TEAM_ID)

                    val propertyValue = robotInfoObject.getString(RobotInfo.COLUMN_NAME_PROPERTY_VALUE)
                    val propertyKeyId = robotInfoObject.getInt(RobotInfo.COLUMN_NAME_PROPERTY_KEY_ID)

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

                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }


        //endregion
    }

    class GetRobotInfoKeys(private val context: MainActivity, year: Year? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetRobotInfoKeys")
            put("YearId", year?.serverId?.toString() ?: "")
        }
    })
    {
        //region Getters

        val robotInfoKeyList: ArrayList<RobotInfoKey> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val robotInfoKeyObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }

        //endregion
    }

    class GetMatches(private val context: MainActivity, event: Event? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetMatches")
            put("EventId", event?.blueAllianceId ?: "")
        }
    })
    {
        //region Getters

        val matches: ArrayList<Match> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val matchObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

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

                    val blueAllianceScore = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE)
                    val redAllianceScore = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_SCORE)


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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }


        //endregion
    }

    class GetRobotMedia(private val context: MainActivity, team: Team? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetRobotMedia")
            put("TeamId", team?.id?.toString() ?: "")
        }
    })
    {
        //region Getters

        val robotMedia: ArrayList<RobotMedia> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val robotMediaJson = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val teamId = robotMediaJson.getInt(RobotMedia.COLUMN_NAME_TEAM_ID)
                    var fileUri = Constants.WEB_URL + "/assets/robot-media/originals/" + robotMediaJson.getString(RobotMedia.COLUMN_NAME_FILE_URI)

                    fileUri = apiParser.downloadImage(fileUri, Constants.ROBOT_MEDIA_DIRECTORY, context).absolutePath

                    robotMedia.add(RobotMedia(
                            -1,
                            teamId,
                            fileUri,
                            false
                    ))

                }

                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }

        //endregion
    }

    class GetYears(private val context: MainActivity) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetYears")
        }
    })
    {
        //region Getters

        val years: ArrayList<Year> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val yearsJson = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

                    val serverId = yearsJson.getInt(Year.COLUMN_NAME_SERVER_ID)
                    val name = yearsJson.getString(Year.COLUMN_NAME_NAME)
                    val startDate = simpleDateFormat.parse(yearsJson.getString(Year.COLUMN_NAME_START_DATE))
                    val endDate = simpleDateFormat.parse(yearsJson.getString(Year.COLUMN_NAME_END_DATE))
                    var fileUri = Constants.WEB_URL + "/assets/year-media/" + yearsJson.getString(Year.COLUMN_NAME_IMAGE_URI)

                    fileUri = apiParser.downloadImage(fileUri, Constants.YEAR_MEDIA_DIRECTORY, context).absolutePath

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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }


        //endregion
    }

    class GetEvents(private val context: MainActivity, private val team: Team? = null) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetEvents")
            put("TeamId", team?.id?.toString() ?: "")
        }
    })
    {
        val events: ArrayList<Event> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))

                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val eventObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }
    }

    class GetChecklistItems(private val context: MainActivity) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetChecklistItems")
        }
    })
    {
        //region Getters

        val checklistItems: ArrayList<ChecklistItem> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                //iterate through, create a new object and add it to the arraylist
                for (i in 0 until response.getJSONArray(API_FIELD_NAME_RESPONSE).length())
                {
                    val checklistItemObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i)

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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }

        //endregion
    }

    class GetChecklistItemResults(private val context: MainActivity) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "GetChecklistItemResults")
        }
    })
    {
        //region Getters

        val checklistItemResults: ArrayList<ChecklistItemResult> = ArrayList()

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                val checklistItemResultArray = response.getJSONArray(API_FIELD_NAME_RESPONSE)

                for (i in 0 until checklistItemResultArray.length())
                {
                    val checklistItemResultObject = checklistItemResultArray.getJSONObject(i)

                    val checklistItemId = checklistItemResultObject.getInt(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID)

                    val matchId = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_MATCH_ID)
                    val status = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_STATUS)
                    val completedBy = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY)

                    val completedDate = simpleDateFormat.parse(checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE))

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
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }

        //endregion
    }

    //endregion

    //region Setters

    class SubmitRobotInfo(private val context: MainActivity, robotInfo: RobotInfo) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "SubmitRobotInfo")

            put(RobotInfo.COLUMN_NAME_YEAR_ID, robotInfo.yearId.toString())
            put(RobotInfo.COLUMN_NAME_EVENT_ID, robotInfo.eventId)
            put(RobotInfo.COLUMN_NAME_TEAM_ID, robotInfo.teamId.toString())

            put(RobotInfo.COLUMN_NAME_PROPERTY_VALUE, robotInfo.propertyValue)
            put(RobotInfo.COLUMN_NAME_PROPERTY_KEY_ID, robotInfo.propertyKeyId.toString())

        }
    })
    {

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }
    }

    class SubmitScoutCardInfo(private val context: MainActivity, scoutCardInfo: ScoutCardInfo) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "SubmitScoutCardInfo")

            put(ScoutCardInfo.COLUMN_NAME_YEAR_ID, scoutCardInfo.yearId.toString())
            put(ScoutCardInfo.COLUMN_NAME_EVENT_ID, scoutCardInfo.eventId)
            put(ScoutCardInfo.COLUMN_NAME_MATCH_ID, scoutCardInfo.matchId)
            put(ScoutCardInfo.COLUMN_NAME_TEAM_ID, scoutCardInfo.teamId.toString())

            put(ScoutCardInfo.COLUMN_NAME_COMPLETED_BY, scoutCardInfo.completedBy)

            put(ScoutCardInfo.COLUMN_NAME_PROPERTY_VALUE, scoutCardInfo.propertyValue)
            put(ScoutCardInfo.COLUMN_NAME_PROPERTY_KEY_ID, scoutCardInfo.propertyKeyId.toString())

        }
    })
    {

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }
    }

    class SubmitRobotMedia(private val context: MainActivity, robotMedia: RobotMedia) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "SubmitRobotMedia")

            put("TeamId", robotMedia.teamId.toString())
            put("Base64Image", robotMedia.base64Image!!)
        }
    })
    {

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()

                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))


                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }
    }

    class SubmitChecklistItemResult(private val context: MainActivity, checklistItemResult: ChecklistItemResult) : Server(context, context.keyStore.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "")!!.toString(), object : HashMap<String, String>()
    {
        init
        {
            put(API_PARAM_API_ACTION, "SubmitChecklistItemResult")

            put(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID, checklistItemResult.checklistItemId.toString())
            put(ChecklistItemResult.COLUMN_NAME_MATCH_ID, checklistItemResult.matchId)

            put(ChecklistItemResult.COLUMN_NAME_STATUS, checklistItemResult.status)
            put(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY, checklistItemResult.completedBy)

            put(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE, checklistItemResult.completedDateForSQL)
        }
    })
    {

        override fun execute(): Boolean
        {
            try
            {
                //parse the data from the server
                val apiParser = ApiParser(this)

                //get the response from the server
                val response = apiParser.parse()
                        
                if (response.getString(API_FIELD_NAME_STATUS) != API_FIELD_NAME_STATUS_SUCCESS)
                    throw Exception(response.getString(API_FIELD_NAME_RESPONSE))

                return true
            } catch (e: Exception)
            {
                context.showSnackbar(e.message!!)
                return false
            }

        }
    }
    //endregion

}


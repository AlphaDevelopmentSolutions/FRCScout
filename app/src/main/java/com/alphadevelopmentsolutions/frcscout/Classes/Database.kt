package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.ContentValues
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Exceptions.UnauthorizedClassException
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmName

class Database(private val context: MainActivity)
{
    private val databaseHelper: DatabaseHelper
    private var db: SQLiteDatabase? = null

    /**
     * Checks if the database is currently open
     *
     * @return boolean if database is open
     */
    val isOpen: Boolean
        get() = if (db != null) db!!.isOpen else false

    enum class SortDirection
    {
        ASC,
        DESC
    }

    /**
     * Gets the class that called this thread
     * If the class that called this thread is not a child of
     * Table.class, UnauthorizedClassException will be thrown
     * @return class that called thread
     * @throws ClassNotFoundException
     */
    protected val callingClass: KClass<*>?
        @Throws(ClassNotFoundException::class)
        get()
        {
            //skip over the first 2 elements, stack trace works as follows:
            //[0] = Thread
            //[1] = this (usually thread)
            //[2] = direct caller (usually self AKA Database)
            //[3+n] = classes and methods called to get to index <= 2
            //keep iterating through the indexes until we find the class we need
            //if the current class in the stack trace is not the same as this class
            //set the class
            //ensure the superclass of the selected class is from Table.class
            //if not kill, only children of Table.class are allowed to access the database
            val stackTraceElements = Thread.currentThread().stackTrace
            for (i in 2 until stackTraceElements.size)
            {
                if (stackTraceElements[i].className != this.javaClass.name)
                {
                    val clazz = Class.forName(stackTraceElements[i].className).kotlin
                    if (!clazz.jvmName.contains("Table"))
                        throw UnauthorizedClassException(Table::class.java)

                    return clazz
                }
            }

            return null
        }

    /**
     * Returns all columns inside the an object in string array format
     * @return string array of all columns
     */
    private//get all the COLUMN fields from the calling class
    //retrieve all fields and iterate through finding the COLUMN fields
    //add the COLUMN field to the columns array
    val columns: Array<String>
        get()
        {
            val columns = ArrayList<String>()
            try
            {
                val clazz = callingClass

                if (clazz != null)
                {
                    for (field in clazz.memberProperties)
                    {
                        if (field.name.startsWith("COLUMN"))
                        {
                            field.javaField!!.isAccessible = true
                            columns.add(field.javaField!!.get(field.name) as String)
                        }
                    }
                }
            } catch (e: IllegalAccessException)
            {
                e.printStackTrace()
            } catch (e: ClassNotFoundException)
            {
                e.printStackTrace()
            }

            return Arrays.copyOf(Objects.requireNonNull<Array<Any>>(columns.toTypedArray()), columns.size, Array<String>::class.java)
        }

    init
    {
        databaseHelper = DatabaseHelper(context)
    }

    /**
     * Starts a transaction when inserting a large number of records
     */
    fun beginTransaction()
    {
        db!!.beginTransaction()
    }

    /**
     * Completes the transaction that was previously opened
     */
    fun finishTransaction()
    {
        db!!.setTransactionSuccessful()
        db!!.endTransaction()
    }

    /**
     * Opens the database for usage
     *
     * @return boolean if database was opened successfully
     */
    fun open(): Boolean
    {
        try
        {
            db = databaseHelper.writableDatabase
            return true
        } catch (e: SQLException)
        {
            return false
        }

    }

    /**
     * Closes the database after usage
     *
     * @return boolean if database was closed successfully
     */
    fun close(): Boolean
    {
        try
        {
            databaseHelper.close()
            return true
        } catch (e: SQLException)
        {
            return false
        }

    }


    /**
     * Clears a selected table
     * @param tableName table name
     * @param clearDrafts boolean to clear drafts in table
     */
    fun clearTable(tableName: String, clearDrafts: Boolean? = null)
    {
        db!!.execSQL(String.format("DELETE FROM %s%s", tableName, if (clearDrafts != null) (if(clearDrafts) "" else " WHERE IsDraft = 0") else ""))
    }

    //region Event Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return Event object converted data
     */
    private fun getEventFromCursor(cursor: Cursor): Event
    {
        val id = cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_ID))
        val yearId = cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_YEAR_ID))
        val blueAllianceId = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_BLUE_ALLIANCE_ID))
        val name = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_NAME))
        val city = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_CITY))
        val stateProvince = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_STATEPROVINCE))
        val country = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_COUNTRY))
        val startDate = Date(cursor.getLong(cursor.getColumnIndex(Event.COLUMN_NAME_START_DATE)))
        val endDate = Date(cursor.getLong(cursor.getColumnIndex(Event.COLUMN_NAME_END_DATE)))

        return Event(
                id,
                yearId,
                blueAllianceId,
                name,
                city,
                stateProvince,
                country,
                startDate,
                endDate)
    }

    /**
     * Gets a specific event from the database and returns it
     * @param year if specified, filters events by year id
     * @param event if specified, filters events by event id
     * @return event based off given ID
     */
    fun getEvents(year: Year?, event: Event?, team: Team?): ArrayList<Event>?
    {
        val events = ArrayList<Event>()

        //insert columns you are going to use here
        val columns = columns

        //where statement
        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (year != null)
        {
            whereStatement.append(Event.COLUMN_NAME_YEAR_ID).append(" = ?")
            whereArgs.add(year.serverId.toString())
        }

        if (event != null)
        {
            whereStatement
                    .append(if (whereStatement.length > 0) " AND " else "")
                    .append(Event.COLUMN_NAME_ID).append(" = ?")
            whereArgs.add(event.id.toString())
        }

        if (team != null)
        {
            whereStatement
                    .append(if (whereStatement.length > 0) " AND " else "")
                    .append("${Event.COLUMN_NAME_BLUE_ALLIANCE_ID} IN (SELECT ${EventTeamList.COLUMN_NAME_EVENT_ID} FROM ${EventTeamList.TABLE_NAME} WHERE ${EventTeamList.COLUMN_NAME_TEAM_ID} = ${team.id})")
        }

        //select the info from the db
        val cursor = db!!.query(
                Event.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null, null)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                events.add(getEventFromCursor(cursor))
            }

            cursor.close()

            return events
        }


        return null
    }

    /**
     * Saves a specific event from the database and returns it
     * @param event with specified ID
     * @return id of the saved event
     */
    fun setEvent(event: Event): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(Event.COLUMN_NAME_YEAR_ID, event.yearId)
        contentValues.put(Event.COLUMN_NAME_BLUE_ALLIANCE_ID, event.blueAllianceId)
        contentValues.put(Event.COLUMN_NAME_NAME, event.name)
        contentValues.put(Event.COLUMN_NAME_CITY, event.city)
        contentValues.put(Event.COLUMN_NAME_STATEPROVINCE, event.stateProvince)
        contentValues.put(Event.COLUMN_NAME_COUNTRY, event.country)
        contentValues.put(Event.COLUMN_NAME_START_DATE, event.startDate!!.time)
        contentValues.put(Event.COLUMN_NAME_END_DATE, event.endDate!!.time)

        Log.i("Database Save", "Saving ${Event.TABLE_NAME} with the Id ${event.id}")

        //event already exists in DB, update
        if (event.id > 0)
        {
            //create the where statement
            val whereStatement = Event.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(event.id.toString() + "")

            //update
            return db!!.update(Event.TABLE_NAME, contentValues, whereStatement, whereArgs).toLong()
        } else
            return db!!.insert(Event.TABLE_NAME, null, contentValues)//insert new event in db

    }

    /**
     * Deletes a specific event from the database
     * @param event with specified ID
     * @return successful delete
     */
    fun deleteEvent(event: Event): Boolean
    {
        if (event.id > 0)
        {
            //create the where statement
            val whereStatement = Event.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(event.id.toString() + "")

            //delete
            return db!!.delete(Event.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Team Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return Team object converted data
     */
    private fun getTeamFromCursor(cursor: Cursor): Team
    {
        val id = cursor.getInt(cursor.getColumnIndex(Team.COLUMN_NAME_ID))
        val name = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_NAME))
        val city = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_CITY))
        val stateProvince = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_STATEPROVINCE))
        val country = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_COUNTRY))
        val rookieYear = cursor.getInt(cursor.getColumnIndex(Team.COLUMN_NAME_ROOKIE_YEAR))
        val facebookURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_FACEBOOK_URL))
        val twitterURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_TWITTER_URL))
        val instagramURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_INSTAGRAM_URL))
        val youtubeURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_YOUTUBE_URL))
        val websiteURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_WEBSITE_URL))
        val imageFileURI = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_IMAGE_FILE_URI))

        return Team(
                id,
                name,
                city,
                stateProvince,
                country,
                rookieYear,
                facebookURL,
                twitterURL,
                instagramURL,
                youtubeURL,
                websiteURL,
                imageFileURI)
    }

    /**
     * Gets a specific team from the database and returns it
     * @param event if specified, filters teams by event id
     * @param team if specified, filters teams by team id
     * @return team based off given ID
     */
    fun getTeams(event: Event?, match: Match?, team: Team?): ArrayList<Team>?
    {
        val teams = ArrayList<Team>()

        //insert columns you are going to use here
        val columns = columns

        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (event != null)
        {
            whereStatement.append(Team.COLUMN_NAME_ID + " IN (SELECT " + EventTeamList.COLUMN_NAME_TEAM_ID + " FROM " + EventTeamList.TABLE_NAME + " WHERE " + EventTeamList.COLUMN_NAME_EVENT_ID + " = ?) ")
            whereArgs.add(event.blueAllianceId!!)
        }

        if (match != null)
        {

            whereStatement.append(if (whereStatement.length > 0) " AND " else "")
                    .append("(" + Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")

                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?))")

            whereArgs.add(match.key)
            whereArgs.add(match.key)
            whereArgs.add(match.key)
            whereArgs.add(match.key)
            whereArgs.add(match.key)
            whereArgs.add(match.key)
        }

        if (team != null)
        {

            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(Team.COLUMN_NAME_ID + " = ? ")
            whereArgs.add(team.id.toString())
        }

        //select the info from the db
        val cursor = db!!.query(
                Team.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null, null)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                teams.add(getTeamFromCursor(cursor))
            }

            cursor.close()

            return teams
        }

        return null
    }

    /**
     * Saves a specific team from the database and returns it
     * @param team with specified ID
     * @return id of the saved team
     */
    fun setTeam(team: Team): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(Team.COLUMN_NAME_NAME, team.name)
        contentValues.put(Team.COLUMN_NAME_CITY, team.city)
        contentValues.put(Team.COLUMN_NAME_STATEPROVINCE, team.stateProvince)
        contentValues.put(Team.COLUMN_NAME_COUNTRY, team.country)
        contentValues.put(Team.COLUMN_NAME_ROOKIE_YEAR, team.rookieYear)
        contentValues.put(Team.COLUMN_NAME_FACEBOOK_URL, team.facebookURL)
        contentValues.put(Team.COLUMN_NAME_TWITTER_URL, team.twitterURL)
        contentValues.put(Team.COLUMN_NAME_INSTAGRAM_URL, team.instagramURL)
        contentValues.put(Team.COLUMN_NAME_YOUTUBE_URL, team.youtubeURL)
        contentValues.put(Team.COLUMN_NAME_WEBSITE_URL, team.websiteURL)
        contentValues.put(Team.COLUMN_NAME_IMAGE_FILE_URI, team.imageFileURI)

        Log.i("Database Save", "Saving ${Team.TABLE_NAME} with the Id ${team.id}")

        //team already exists in DB, update
        if (team.id!! > 0)
        {
            //create the where statement
            val whereStatement = Team.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(team.id.toString() + "")

            //insert columns you are going to use here
            val columns = arrayOf(Team.COLUMN_NAME_ID)


            //select the info from the db
            val cursor = db!!.query(
                    Team.TABLE_NAME,
                    columns,
                    whereStatement,
                    whereArgs, null, null, null)

            if (cursor.count > 0)
            {
                cursor.close()
                //update
                return db!!.update(Team.TABLE_NAME, contentValues, whereStatement, whereArgs).toLong()
            }
            else
            {
                cursor.close()
                contentValues.put(Team.COLUMN_NAME_ID, team.id)
                return db!!.insert(Team.TABLE_NAME, null, contentValues)
            }//record doesn't exist yet, insert
        } else
            return db!!.insert(Team.TABLE_NAME, null, contentValues)//insert new team in db

    }

    /**
     * Deletes a specific team from the database
     * @param team with specified ID
     * @return successful delete
     */
    fun deleteTeam(team: Team): Boolean
    {
        if (team.id!! > 0)
        {
            //create the where statement
            val whereStatement = Team.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(team.id.toString() + "")

            //delete
            return db!!.delete(Team.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Robot Logic

    /**
     * Gets a specific robot from the database and returns it
     *
     * @param robot with specified ID
     * @return robot based off given ID
     */
    fun getRobot(robot: Robot): Robot?
    {
        //insert columns you are going to use here
        val columns = arrayOf(Robot.COLUMN_NAME_NAME, Robot.COLUMN_NAME_TEAM_NUMBER)

        //where statement
        val whereStatement = Robot.COLUMN_NAME_ID + " = ?"
        val whereArgs = arrayOf(robot.id.toString() + "")

        //select the info from the db
        val cursor = db!!.query(
                Robot.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs, null, null, null)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            //move to the first result in the set
            cursor.moveToFirst()

            val name = cursor.getString(cursor.getColumnIndex(Robot.COLUMN_NAME_NAME))
            val teamNumber = cursor.getInt(cursor.getColumnIndex(Robot.COLUMN_NAME_TEAM_NUMBER))

            cursor.close()

            return Robot(robot.id, name, teamNumber)
        }


        return null
    }

    /**
     * Saves a specific robot from the database and returns it
     *
     * @param robot with specified ID
     * @return id of the saved robot
     */
    fun setRobot(robot: Robot): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(Robot.COLUMN_NAME_NAME, robot.name)
        contentValues.put(Robot.COLUMN_NAME_TEAM_NUMBER, robot.teamNumber)

        Log.i("Database Save", "Saving ${Robot.TABLE_NAME} with the Id ${robot.id}")

        //robot already exists in DB, update
        if (robot.id > 0)
        {
            //create the where statement
            val whereStatement = Robot.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(robot.id.toString() + "")

            //update
            return db!!.update(Robot.TABLE_NAME, contentValues, whereStatement, whereArgs).toLong()
        } else
            return db!!.insert(Robot.TABLE_NAME, null, contentValues)//insert new robot in db

    }

    /**
     * Deletes a specific robot from the database
     *
     * @param robot with specified ID
     * @return successful delete
     */
    fun deleteRobot(robot: Robot): Boolean
    {
        if (robot.id > 0)
        {
            //create the where statement
            val whereStatement = Robot.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(robot.id.toString() + "")

            //delete
            return db!!.delete(Robot.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Match Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return Match object converted data
     */
    private fun getMatchFromCursor(cursor: Cursor): Match
    {
        val id = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_ID))
        val date = Date(cursor.getLong(cursor.getColumnIndex(Match.COLUMN_NAME_DATE)))
        val eventId = cursor.getString(cursor.getColumnIndex(Match.COLUMN_NAME_EVENT_ID))
        val key = cursor.getString(cursor.getColumnIndex(Match.COLUMN_NAME_KEY))
        val matchType = Match.Type.getTypeFromString(cursor.getString(cursor.getColumnIndex(Match.COLUMN_NAME_MATCH_TYPE)))
        val setNumber = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_SET_NUMBER))
        val matchNumber = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_MATCH_NUMBER))

        val blueAllianceTeamOneId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID))
        val blueAllianceTeamTwoId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID))
        val blueAllianceTeamThreeId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID))

        val redAllianceTeamOne = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID))
        val redAllianceTeamTwo = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID))
        val redAllianceTeamThree = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID))

        val blueAllianceScore = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE))
        val redAllianceScore = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_SCORE))

        return Match(
                id,
                date,
                eventId,
                key,
                matchType,
                setNumber,
                matchNumber,

                blueAllianceTeamOneId,
                blueAllianceTeamTwoId,
                blueAllianceTeamThreeId,

                redAllianceTeamOne,
                redAllianceTeamTwo,
                redAllianceTeamThree,

                blueAllianceScore,
                redAllianceScore)
    }

    /**
     * Gets all matches in the database
     * @param event if specified, filters matches by event id
     * @param match if specified, filters matches by match id
     * @param team if specified, filters matches by team id
     * @return all events inside database
     */
    fun getMatches(event: Event?, match: Match?, team: Team?, sortDirection: SortDirection = SortDirection.DESC): ArrayList<Match>?
    {
        val matches = ArrayList<Match>()

        //insert columns you are going to use here
        val columns = columns

        //begin a transaction for multiple calls
        if(!db!!.inTransaction())
            beginTransaction()

        Match.Type.getTypes().forEach{

            //where statement
            val whereStatement = StringBuilder()
            val whereArgs = ArrayList<String>()

            if (event != null)
            {
                whereStatement.append(Match.COLUMN_NAME_EVENT_ID).append(" = ?")
                whereArgs.add(event.blueAllianceId!!)
            }

            if (match != null)
            {
                whereStatement
                        .append(if (whereStatement.isNotEmpty()) " AND " else "")
                        .append(Match.COLUMN_NAME_KEY).append(" = ?")
                whereArgs.add(match.key)
            }

            if (team != null)
            {
                whereStatement
                        .append(if (whereStatement.isNotEmpty()) " AND " else "")
                        .append(team.id).append(" IN (")

                        .append(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID).append(", ")
                        .append(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID).append(", ")
                        .append(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID).append(", ")

                        .append(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID).append(", ")
                        .append(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID).append(", ")
                        .append(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID).append(")")
            }

            whereStatement
                    .append(if (whereStatement.isNotEmpty()) " AND " else "")
                    .append(" ${Match.COLUMN_NAME_MATCH_TYPE} = ? ")
            whereArgs.add(it.toString())

            //select the info from the db
            val cursor = db!!.query(
                    Match.TABLE_NAME,
                    columns,
                    whereStatement.toString(),
                    Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java),
                    null,
                    null,
                    "${Match.COLUMN_NAME_MATCH_NUMBER} ${sortDirection.name}")

            //make sure the cursor isn't null, else we die
            if (cursor != null)
            {
                while (cursor.moveToNext())
                    matches.add(getMatchFromCursor(cursor))

                cursor.close()
            }
        }

        finishTransaction()

        return matches
    }

    /**
     * Saves a specific match from the database and returns it
     *
     * @param match with specified ID
     * @return id of the saved match
     */
    fun setMatch(match: Match): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(Match.COLUMN_NAME_DATE, match.date.time)
        contentValues.put(Match.COLUMN_NAME_EVENT_ID, match.eventId)
        contentValues.put(Match.COLUMN_NAME_KEY, match.key)
        contentValues.put(Match.COLUMN_NAME_MATCH_TYPE, match.matchType.name)
        contentValues.put(Match.COLUMN_NAME_SET_NUMBER, match.setNumber)
        contentValues.put(Match.COLUMN_NAME_MATCH_NUMBER, match.matchNumber)

        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID, match.blueAllianceTeamOneId)
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID, match.blueAllianceTeamTwoId)
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID, match.blueAllianceTeamThreeId)

        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID, match.redAllianceTeamOneId)
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID, match.redAllianceTeamTwoId)
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID, match.redAllianceTeamThreeId)

        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE, match.blueAllianceScore)
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_SCORE, match.redAllianceScore)

        Log.i("Database Save", "Saving ${Match.TABLE_NAME} with the Id ${match.id}")


        //Match already exists in DB, update
        if (match.id > 0)
        {
            //create the where statement
            val whereStatement = Match.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(match.id.toString() + "")

            //update
            return if (db!!.update(Match.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                match.id.toLong()
            else
                -1
        } else
            return db!!.insert(Match.TABLE_NAME, null, contentValues)//insert new Match in db

    }

    /**
     * Deletes a specific match from the database
     *
     * @param match with specified ID
     * @return successful delete
     */
    fun deleteMatch(match: Match): Boolean
    {
        if (match.id > 0)
        {
            //create the where statement
            val whereStatement = Match.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(match.id.toString() + "")

            //delete
            return db!!.delete(Match.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Scout Card Info Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into a scout card
     * @param cursor info from database
     * @return scoutcardinfo converted data
     */
    private fun getScoutCardInfoFromCursor(cursor: Cursor): ScoutCardInfo
    {
        val id = cursor.getInt(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_ID))
        val yearId = cursor.getInt(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_YEAR_ID))
        val eventId = cursor.getString(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_EVENT_ID))
        val matchId = cursor.getString(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_MATCH_ID))
        val teamId = cursor.getInt(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_TEAM_ID))
        
        val completedBy = cursor.getString(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_COMPLETED_BY))
        
        val propertyValue = cursor.getString(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_PROPERTY_VALUE))
        val propertyKeyId = cursor.getInt(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_PROPERTY_KEY_ID))

        val isDraft = cursor.getInt(cursor.getColumnIndex(ScoutCardInfo.COLUMN_NAME_IS_DRAFT)) == 1

        return ScoutCardInfo(
                id,
                yearId,
                eventId,
                matchId,
                teamId,
                
                completedBy,
                
                propertyValue,
                propertyKeyId,
                
                isDraft)
    }

    /**
     * Gets all scout cards for a match at an event
     * @param event if specified, filters scout cards by event id
     * @param match if specified, filters scout cards by match id
     * @param team if specified, filters scout cards by team id
     * @param scoutCardInfo if specified, filters scout cards by scout card info id
     * @param onlyDrafts  if true, filters scout cards by draft
     * @return scoutcard based off given info
     */
    fun getScoutCardInfo(event: Event?, match: Match?, team: Team?, scoutCardInfoKey: ScoutCardInfoKey?, scoutCardInfo: ScoutCardInfo?, onlyDrafts: Boolean): ArrayList<ScoutCardInfo>?
    {
        val scoutCardInfos = ArrayList<ScoutCardInfo>()

        //insert columns you are going to use here
        val columns = columns

        //where statement
        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (event != null)
        {
            whereStatement.append(ScoutCardInfo.COLUMN_NAME_EVENT_ID).append(" = ?")
            whereArgs.add(event.blueAllianceId!!)
        }

        if (match != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ScoutCardInfo.COLUMN_NAME_MATCH_ID).append(" = ?")
            whereArgs.add(match.key)
        }

        if (team != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ScoutCardInfo.COLUMN_NAME_TEAM_ID).append(" = ?")
            whereArgs.add(team.id.toString())
        }

        if (scoutCardInfoKey != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ScoutCardInfo.COLUMN_NAME_PROPERTY_KEY_ID + " = ?")
            whereArgs.add(scoutCardInfoKey.serverId.toString())
        }

        if (scoutCardInfo != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ScoutCardInfo.COLUMN_NAME_ID).append(" = ?")
            whereArgs.add(scoutCardInfo.id.toString())
        }

        if (onlyDrafts)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ScoutCardInfo.COLUMN_NAME_IS_DRAFT).append(" = 1")
        }

        val orderBy = ScoutCardInfo.COLUMN_NAME_MATCH_ID + " DESC"

        //select the info from the db
        val cursor = db!!.query(
                ScoutCardInfo.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null,
                orderBy)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                scoutCardInfos.add(getScoutCardInfoFromCursor(cursor))
            }

            cursor.close()

            return scoutCardInfos
        }

        return null
    }

    /**
     * Saves a specific scoutCardInfo from the database and returns it
     *
     * @param scoutCardInfo with specified ID
     * @return id of the saved scoutCardInfo
     */
    fun setScoutCardInfo(scoutCardInfo: ScoutCardInfo): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(ScoutCardInfo.COLUMN_NAME_YEAR_ID, scoutCardInfo.yearId)
        contentValues.put(ScoutCardInfo.COLUMN_NAME_EVENT_ID, scoutCardInfo.eventId)
        contentValues.put(ScoutCardInfo.COLUMN_NAME_MATCH_ID, scoutCardInfo.matchId)
        contentValues.put(ScoutCardInfo.COLUMN_NAME_TEAM_ID, scoutCardInfo.teamId)

        contentValues.put(ScoutCardInfo.COLUMN_NAME_COMPLETED_BY, scoutCardInfo.completedBy)
        
        contentValues.put(ScoutCardInfo.COLUMN_NAME_PROPERTY_VALUE, scoutCardInfo.propertyValue)
        contentValues.put(ScoutCardInfo.COLUMN_NAME_PROPERTY_KEY_ID, scoutCardInfo.propertyKeyId)

        contentValues.put(ScoutCardInfo.COLUMN_NAME_IS_DRAFT, if (scoutCardInfo.isDraft) "1" else "0")

        Log.i("Database Save", "Saving ${ScoutCardInfoKey.TABLE_NAME} with the Id ${scoutCardInfo.id}")

        //scoutCardInfo already exists in DB, update
        if (scoutCardInfo.id > 0)
        {
            //create the where statement
            val whereStatement = ScoutCardInfo.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(scoutCardInfo.id.toString() + "")

            //update
            return if (db!!.update(ScoutCardInfo.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                scoutCardInfo.id.toLong()
            else
                -1
        } else
            return db!!.insert(ScoutCardInfo.TABLE_NAME, null, contentValues)//insert new scoutCardInfo in db

    }

    /**
     * Deletes a specific scoutCardInfo from the database
     *
     * @param scoutCardInfo with specified ID
     * @return successful delete
     */
    fun deleteScoutCardInfo(scoutCardInfo: ScoutCardInfo): Boolean
    {
        if (scoutCardInfo.id > 0)
        {
            //create the where statement
            val whereStatement = ScoutCardInfo.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(scoutCardInfo.id.toString() + "")

            //delete
            return db!!.delete(ScoutCardInfo.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Scout Card Info Keys Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into a pit card
     * @param cursor info from database
     * @return pitcard converted data
     */
    private fun getScoutCardInfoKeyFromCursor(cursor: Cursor): ScoutCardInfoKey
    {
        val id = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_ID))
        val serverId = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_SERVER_ID))
        val yearId = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID))

        val keyState = cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_KEY_STATE))
        val keyName = cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_KEY_NAME))

        val sortOrder = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER))

        val stringMinValue = cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_MIN_VALUE))
        val stringMaxValue = cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_MAX_VALUE))

        val minValue = if(stringMinValue != null) Integer.parseInt(stringMinValue) else null
        val maxValue = if(stringMaxValue != null) Integer.parseInt(stringMaxValue) else null


        val nullZeros = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_NULL_ZEROS)) == 1
        val includeInStats = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_INCLUDE_IN_STATS)) == 1

        val dataType = ScoutCardInfoKey.DataTypes.parseString(cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_DATA_TYPE)))

        return ScoutCardInfoKey(
                id,
                serverId,
                yearId,

                keyState,
                keyName,

                sortOrder,

                minValue,
                maxValue,

                nullZeros,
                includeInStats,

                dataType)
    }

    /**
     * Gets all robot info keys
     * @param year if specified, filters by year id
     * @param scoutCardInfoKey if specified, filters by scoutCardInfoKey id
     * @return object based off given team ID
     */
    fun getScoutCardInfoKeys(year: Year?, scoutCardInfoKey: ScoutCardInfoKey?): ArrayList<ScoutCardInfoKey>?
    {
        val scoutCardInfoKeys = ArrayList<ScoutCardInfoKey>()

        //insert columns you are going to use here
        val columns = columns

        //where statement
        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (year != null)
        {
            whereStatement.append(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID + " = ? ")
            whereArgs.add(year.serverId.toString())
        }

        if (scoutCardInfoKey != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ScoutCardInfoKey.COLUMN_NAME_ID + " = ?")
            whereArgs.add(scoutCardInfoKey.id.toString())
        }


        val orderBy = ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER + " ASC"

        //select the info from the db
        val cursor = db!!.query(
                ScoutCardInfoKey.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null,
                orderBy)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                scoutCardInfoKeys.add(getScoutCardInfoKeyFromCursor(cursor))
            }

            cursor.close()

            return scoutCardInfoKeys
        }


        return null
    }

    /**
     * Saves a specific object from the database and returns it
     *
     * @param scoutCardInfoKey with specified ID
     * @return id of the saved scoutCardInfoKey
     */
    fun setScoutCardInfoKey(scoutCardInfoKey: ScoutCardInfoKey): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_SERVER_ID, scoutCardInfoKey.serverId)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID, scoutCardInfoKey.yearId)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_KEY_STATE, scoutCardInfoKey.keyState)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_KEY_NAME, scoutCardInfoKey.keyName)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER, scoutCardInfoKey.sortOrder)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_MIN_VALUE, scoutCardInfoKey.minValue)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_MAX_VALUE, scoutCardInfoKey.maxValue)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_NULL_ZEROS, if (scoutCardInfoKey.nullZeros) 1 else 0)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_INCLUDE_IN_STATS, if (scoutCardInfoKey.includeInStats) 1 else 0)
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_DATA_TYPE, scoutCardInfoKey.dataType.name)

        Log.i("Database Save", "Saving ${ScoutCardInfoKey.TABLE_NAME} with the Id ${scoutCardInfoKey.id}")


        //scoutCardInfoKey already exists in DB, update
        if (scoutCardInfoKey.id > 0)
        {
            //create the where statement
            val whereStatement = ScoutCardInfoKey.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(scoutCardInfoKey.id.toString() + "")

            //update
            return if (db!!.update(ScoutCardInfoKey.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                scoutCardInfoKey.id.toLong()
            else
                -1
        } else
            return db!!.insert(ScoutCardInfoKey.TABLE_NAME, null, contentValues)//insert new scoutCardInfo in db

    }

    /**
     * Deletes a specific scoutCardInfo from the database
     *
     * @param scoutCardInfoKey with specified ID
     * @return successful delete
     */
    fun deleteScoutCardInfoKey(scoutCardInfoKey: ScoutCardInfoKey): Boolean
    {
        if (scoutCardInfoKey.id > 0)
        {
            //create the where statement
            val whereStatement = ScoutCardInfoKey.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(scoutCardInfoKey.id.toString() + "")

            //delete
            return db!!.delete(ScoutCardInfoKey.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Robot Info Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into a pit card
     * @param cursor info from database
     * @return pitcard converted data
     */
    private fun getRobotInfoFromCursor(cursor: Cursor): RobotInfo
    {
        val id = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_ID))
        val yearId = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_YEAR_ID))
        val eventId = cursor.getString(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_EVENT_ID))
        val teamId = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_TEAM_ID))

        val propertyValue = cursor.getString(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_PROPERTY_VALUE))
        val propertyKeyId = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_PROPERTY_KEY_ID))

        val isDraft = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_IS_DRAFT)) == 1


        return RobotInfo(
                id,
                yearId,
                eventId,
                teamId,

                propertyValue,
                propertyKeyId,

                isDraft)
    }

    /**
     * Gets all robot info
     * @param year if specified, filters robot info by year id
     * @param event if specified, filters robot info by event id
     * @param team if specified, filters robot info by team id
     * @param robotInfo if specified, filters robot info by robotInfo id
     * @param robotInfoKey if specified, filters robot info by robotInfoKey
     * @param onlyDrafts if true, filters by only drafts
     * @return object based off given team ID
     */
    fun getRobotInfo(year: Year?, event: Event?, team: Team?, robotInfoKey: RobotInfoKey?, robotInfo: RobotInfo?, onlyDrafts: Boolean): ArrayList<RobotInfo>?
    {
        val robotInfos = ArrayList<RobotInfo>()

        //insert columns you are going to use here
        val columns = columns

        //where statement
        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (year != null)
        {
            whereStatement.append(RobotInfo.COLUMN_NAME_YEAR_ID + " = ? ")
            whereArgs.add(year.serverId.toString())
        }

        if (event != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotInfo.COLUMN_NAME_EVENT_ID + " = ? ")
            whereArgs.add(event.blueAllianceId!!)
        }

        if (team != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotInfo.COLUMN_NAME_TEAM_ID + " = ? ")
            whereArgs.add(team.id.toString())
        }

        if (robotInfoKey != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotInfo.COLUMN_NAME_PROPERTY_KEY_ID + " = ? ")
            whereArgs.add(robotInfoKey.serverId.toString())
        }

        if (robotInfo != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotInfo.COLUMN_NAME_ID + " = ? ")
            whereArgs.add(robotInfo.id.toString())
        }

        if (onlyDrafts)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotInfo.COLUMN_NAME_IS_DRAFT + " = 1 ")
        }

        val orderBy = RobotInfo.COLUMN_NAME_ID + " DESC"

        //select the info from the db
        val cursor = db!!.query(
                RobotInfo.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null,
                orderBy)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                robotInfos.add(getRobotInfoFromCursor(cursor))
            }

            cursor.close()

            return robotInfos
        }


        return null
    }

    /**
     * Saves a specific object from the database and returns it
     *
     * @param robotInfo with specified ID
     * @return id of the saved robotInfo
     */
    fun setRobotInfo(robotInfo: RobotInfo): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(RobotInfo.COLUMN_NAME_YEAR_ID, robotInfo.yearId)
        contentValues.put(RobotInfo.COLUMN_NAME_EVENT_ID, robotInfo.eventId)
        contentValues.put(RobotInfo.COLUMN_NAME_TEAM_ID, robotInfo.teamId)

        contentValues.put(RobotInfo.COLUMN_NAME_PROPERTY_VALUE, robotInfo.propertyValue)
        contentValues.put(RobotInfo.COLUMN_NAME_PROPERTY_KEY_ID, robotInfo.propertyKeyId)

        contentValues.put(RobotInfo.COLUMN_NAME_IS_DRAFT, if (robotInfo.isDraft) "1" else "0")

        Log.i("Database Save", "Saving ${RobotInfo.TABLE_NAME} with the Id ${robotInfo.id}")

        //try and update first
        //if update fails, no record was in the database so add a new record

        //create the where statement
        val whereStatement = RobotInfo.COLUMN_NAME_YEAR_ID + " = ? AND " +
                RobotInfo.COLUMN_NAME_EVENT_ID + " = ? AND " +
                RobotInfo.COLUMN_NAME_TEAM_ID + " = ? AND " +
                RobotInfo.COLUMN_NAME_PROPERTY_KEY_ID + " = ? "
        val whereArgs = arrayOf<String>(robotInfo.yearId.toString(), robotInfo.eventId, robotInfo.teamId.toString(),
                robotInfo.propertyKeyId.toString())

        //update
        return if (db!!.update(RobotInfo.TABLE_NAME, contentValues, whereStatement, whereArgs) > 0)
            robotInfo.id.toLong()
        else
            db!!.insert(RobotInfo.TABLE_NAME, null, contentValues)
    }

    /**
     * Deletes a specific scoutCardInfo from the database
     *
     * @param robotInfo with specified ID
     * @return successful delete
     */
    fun deleteRobotInfo(robotInfo: RobotInfo): Boolean
    {
        if (robotInfo.id > 0)
        {
            //create the where statement
            val whereStatement = RobotInfo.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(robotInfo.id.toString() + "")

            //delete
            return db!!.delete(RobotInfo.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Robot Info Keys Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into a pit card
     * @param cursor info from database
     * @return pitcard converted data
     */
    private fun getRobotInfoKeyFromCursor(cursor: Cursor): RobotInfoKey
    {
        val id = cursor.getInt(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_ID))
        val serverId = cursor.getInt(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_SERVER_ID))
        val yearId = cursor.getInt(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_YEAR_ID))
        val sortOrder = cursor.getInt(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_SORT_ORDER))

        val keyState = cursor.getString(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_KEY_STATE))
        val keyName = cursor.getString(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_KEY_NAME))


        return RobotInfoKey(
                id,
                serverId,
                yearId,

                keyState,
                keyName,

                sortOrder)
    }

    /**
     * Gets all robot info keys
     * @param year if specified, filters by year id
     * @param robotInfoKey if specified, filters by robotInfoKey id
     * @return object based off given team ID
     */
    fun getRobotInfoKeys(year: Year?, robotInfoKey: RobotInfoKey?): ArrayList<RobotInfoKey>?
    {
        val robotInfoKeys = ArrayList<RobotInfoKey>()

        //insert columns you are going to use here
        val columns = columns

        //where statement
        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (year != null)
        {
            whereStatement.append(RobotInfoKey.COLUMN_NAME_YEAR_ID + " = ? ")
            whereArgs.add(year.serverId.toString())
        }

        if (robotInfoKey != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotInfoKey.COLUMN_NAME_ID + " = ?")
            whereArgs.add(robotInfoKey.id.toString())
        }


        val orderBy = RobotInfoKey.COLUMN_NAME_SORT_ORDER + " ASC"

        //select the info from the db
        val cursor = db!!.query(
                RobotInfoKey.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null,
                orderBy)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                robotInfoKeys.add(getRobotInfoKeyFromCursor(cursor))
            }

            cursor.close()

            return robotInfoKeys
        }


        return null
    }

    /**
     * Saves a specific object from the database and returns it
     *
     * @param robotInfoKey with specified ID
     * @return id of the saved robotInfoKey
     */
    fun setRobotInfoKey(robotInfoKey: RobotInfoKey): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(RobotInfoKey.COLUMN_NAME_SERVER_ID, robotInfoKey.serverId)
        contentValues.put(RobotInfoKey.COLUMN_NAME_YEAR_ID, robotInfoKey.yearId)
        contentValues.put(RobotInfoKey.COLUMN_NAME_SORT_ORDER, robotInfoKey.sortOrder)
        contentValues.put(RobotInfoKey.COLUMN_NAME_KEY_STATE, robotInfoKey.keyState)
        contentValues.put(RobotInfoKey.COLUMN_NAME_KEY_NAME, robotInfoKey.keyName)

        Log.i("Database Save", "Saving ${RobotInfoKey.TABLE_NAME} with the Id ${robotInfoKey.id}")

        //robotInfoKey already exists in DB, update
        if (robotInfoKey.id > 0)
        {
            //create the where statement
            val whereStatement = RobotInfoKey.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(robotInfoKey.id.toString() + "")

            //update
            return if (db!!.update(RobotInfoKey.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                robotInfoKey.id.toLong()
            else
                -1
        } else
            return db!!.insert(RobotInfoKey.TABLE_NAME, null, contentValues)//insert new scoutCardInfo in db

    }

    /**
     * Deletes a specific scoutCardInfo from the database
     *
     * @param robotInfoKey with specified ID
     * @return successful delete
     */
    fun deleteRobotInfoKey(robotInfoKey: RobotInfoKey): Boolean
    {
        if (robotInfoKey.id > 0)
        {
            //create the where statement
            val whereStatement = RobotInfoKey.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(robotInfoKey.id.toString() + "")

            //delete
            return db!!.delete(RobotInfoKey.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region User Logic

    /**
     * Gets all objects in the database
     * @param user if specified, filter by user id
     * @return all objects inside database
     */
    fun getUsers(user: User?): ArrayList<User>?
    {
        val users = ArrayList<User>()

        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (user != null)
        {
            whereStatement.append(User.COLUMN_NAME_ID + " = ?")
            whereArgs.add(user.id.toString())
        }

        //insert columns you are going to use here
        val columns = columns

        //select the info from the db
        val cursor = db!!.query(
                User.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null, null)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                val id = cursor.getInt(cursor.getColumnIndex(User.COLUMN_NAME_ID))
                val firstName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME_FIRST_NAME))
                val lastName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME_LAST_NAME))

                users.add(User(id, firstName, lastName))
            }

            cursor.close()

            return users
        }


        return null
    }

    /**
     * Saves a specific scoutCardInfo from the database and returns it
     * @param user with specified ID
     * @return id of the saved user
     */
    fun setUser(user: User): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(User.COLUMN_NAME_FIRST_NAME, user.firstName)
        contentValues.put(User.COLUMN_NAME_LAST_NAME, user.lastName)

        Log.i("Database Save", "Saving ${User.TABLE_NAME} with the Id ${user.id}")

        //object already exists in DB, update
        if (user.id > 0)
        {
            //create the where statement
            val whereStatement = User.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(user.id.toString() + "")

            //update
            return if (db!!.update(User.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                user.id.toLong()
            else
                -1
        } else
            return db!!.insert(User.TABLE_NAME, null, contentValues)//insert new object in db

    }

    /**
     * Deletes a specific object from the database
     * @param user with specified ID
     * @return successful delete
     */
    fun deleteUser(user: User): Boolean
    {
        if (user.id > 0)
        {
            //create the where statement
            val whereStatement = User.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(user.id.toString() + "")

            //delete
            return db!!.delete(User.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Robot Media Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into robot media
     * @param cursor info from database
     * @return robotMedia converted data
     */
    private fun getRobotMediaFromCursor(cursor: Cursor): RobotMedia
    {
        val id = cursor.getInt(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_ID))
        val teamId = cursor.getInt(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_TEAM_ID))
        val fileUri = cursor.getString(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_FILE_URI))
        val isDraft = cursor.getInt(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_IS_DRAFT)) == 1

        return RobotMedia(
                id,
                teamId,
                fileUri,
                isDraft)
    }

    /**
     * Gets all robot media assigned to a team
     * @param robotMedia if specified, robot media filters by robot media id
     * @param team if specified, filters robot media by team id
     * @param onlyDrafts if true, filters robot media by only drafts
     * @return robotMedia based off given team ID
     */
    fun getRobotMedia(robotMedia: RobotMedia?, team: Team?, onlyDrafts: Boolean): ArrayList<RobotMedia>?
    {
        val robotMediaList = ArrayList<RobotMedia>()

        //insert columns you are going to use here
        val columns = columns

        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (robotMedia != null)
        {
            whereStatement.append(RobotMedia.COLUMN_NAME_ID).append(" = ?")
            whereArgs.add(robotMedia.id.toString())
        }

        if (team != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotMedia.COLUMN_NAME_TEAM_ID).append(" = ?")
            whereArgs.add(team.id.toString())
        }

        if (onlyDrafts)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(RobotMedia.COLUMN_NAME_IS_DRAFT).append(" = 1")
        }

        //select the info from the db
        val cursor = db!!.query(
                RobotMedia.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null, null)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                robotMediaList.add(getRobotMediaFromCursor(cursor))
            }

            cursor.close()

            return robotMediaList
        }

        return null
    }

    /**
     * Saves a specific robotMedia from the database and returns it
     *
     * @param robotMedia with specified ID
     * @return id of the saved robotMedia
     */
    fun setRobotMedia(robotMedia: RobotMedia): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(RobotMedia.COLUMN_NAME_TEAM_ID, robotMedia.teamId)
        contentValues.put(RobotMedia.COLUMN_NAME_FILE_URI, robotMedia.fileUri)
        contentValues.put(RobotMedia.COLUMN_NAME_IS_DRAFT, if (robotMedia.isDraft) "1" else "0")

        //robotMedia already exists in DB, update
        if (robotMedia.id > 0)
        {
            //create the where statement
            val whereStatement = RobotMedia.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(robotMedia.id.toString() + "")

            //update
            return if (db!!.update(RobotMedia.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                robotMedia.id.toLong()
            else
                -1
        } else
            return db!!.insert(RobotMedia.TABLE_NAME, null, contentValues)//insert new robotMedia in db

    }

    /**
     * Deletes a specific robotMedia from the database
     * @param robotMedia with specified ID
     * @return successful delete
     */
    fun deleteRobotMedia(robotMedia: RobotMedia): Boolean
    {
        if (robotMedia.id > 0)
        {
            //create the where statement
            val whereStatement = RobotMedia.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(robotMedia.id.toString() + "")

            //delete
            return db!!.delete(RobotMedia.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Year Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into object
     * @param cursor info from database
     * @return years converted data
     */
    private fun getYearsFromCursor(cursor: Cursor): Year
    {
        val id = cursor.getInt(cursor.getColumnIndex(Year.COLUMN_NAME_ID))
        val serverId = cursor.getInt(cursor.getColumnIndex(Year.COLUMN_NAME_SERVER_ID))
        val name = cursor.getString(cursor.getColumnIndex(Year.COLUMN_NAME_NAME))
        val startDate = Date(cursor.getLong(cursor.getColumnIndex(Year.COLUMN_NAME_START_DATE)))
        val endDate = Date(cursor.getLong(cursor.getColumnIndex(Year.COLUMN_NAME_END_DATE)))
        val fileUri = cursor.getString(cursor.getColumnIndex(Year.COLUMN_NAME_IMAGE_URI))

        return Year(
                id,
                serverId,
                name,
                startDate,
                endDate,
                fileUri)
    }

    /**
     * Gets all year assigned to a team
     * @param year if specified, object filters by year id
     * @return year based off given team ID
     */
    fun getYears(year: Year?): ArrayList<Year>?
    {
        val yearList = ArrayList<Year>()

        //insert columns you are going to use here
        val columns = columns

        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (year != null)
        {
            whereStatement.append(Year.COLUMN_NAME_SERVER_ID).append(" = ?")
            whereArgs.add(year.serverId.toString())
        }

        //select the info from the db
        val cursor = db!!.query(
                Year.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null, null)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                yearList.add(getYearsFromCursor(cursor))
            }

            cursor.close()

            return yearList
        }

        return null
    }

    /**
     * Saves a specific year from the database and returns it
     * @param year with specified ID
     * @return id of the saved year
     */
    fun setYears(year: Year): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(Year.COLUMN_NAME_SERVER_ID, year.serverId)
        contentValues.put(Year.COLUMN_NAME_NAME, year.name)
        contentValues.put(Year.COLUMN_NAME_START_DATE, year.startDate.time)
        contentValues.put(Year.COLUMN_NAME_END_DATE, year.endDate.time)
        contentValues.put(Year.COLUMN_NAME_IMAGE_URI, year.imageUri)

        Log.i("Database Save", "Saving ${Year.TABLE_NAME} with the Id ${year.id}")

        //year already exists in DB, update
        if (year.id!! > 0)
        {
            //create the where statement
            val whereStatement = Year.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(year.id.toString() + "")

            //update
            return if (db!!.update(Year.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                year.id!!.toLong()
            else
                -1
        } else
            return db!!.insert(Year.TABLE_NAME, null, contentValues)//insert new year in db

    }

    /**
     * Deletes a specific year from the database
     * @param year with specified ID
     * @return successful delete
     */
    fun deleteYears(year: Year): Boolean
    {
        if (year.id!! > 0)
        {
            //create the where statement
            val whereStatement = Year.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(year.id.toString() + "")

            //delete
            return db!!.delete(Year.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Event Team List Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return eventeamlist converted data
     */
    private fun getEventTeamListFromCursor(cursor: Cursor): EventTeamList
    {
        val id = cursor.getInt(cursor.getColumnIndex(EventTeamList.COLUMN_NAME_ID))
        val teamId = cursor.getInt(cursor.getColumnIndex(EventTeamList.COLUMN_NAME_TEAM_ID))
        val eventId = cursor.getString(cursor.getColumnIndex(EventTeamList.COLUMN_NAME_EVENT_ID))

        return EventTeamList(
                id,
                teamId,
                eventId)
    }

    /**
     * Gets the event team list data based off an event
     * @param eventTeamList if specified, filters event team list by event team list id
     * @param event if specified, filters event team list by event id
     * @return object based off given team ID
     */
    fun getEventTeamLists(eventTeamList: EventTeamList?, event: Event?): ArrayList<EventTeamList>?
    {
        val eventTeamLists = ArrayList<EventTeamList>()

        //insert columns you are going to use here
        val columns = columns

        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (eventTeamList != null)
        {
            whereStatement.append(EventTeamList.COLUMN_NAME_EVENT_ID).append(" = ?")
            whereArgs.add(eventTeamList.id.toString())
        }

        if (event != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(EventTeamList.COLUMN_NAME_EVENT_ID).append(" = ?")
            whereArgs.add(event.blueAllianceId!!)
        }

        val orderBy = EventTeamList.COLUMN_NAME_TEAM_ID + " DESC"

        //select the info from the db
        val cursor = db!!.query(
                EventTeamList.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null,
                orderBy)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                eventTeamLists.add(getEventTeamListFromCursor(cursor))
            }

            cursor.close()

            return eventTeamLists
        }

        return null
    }

    /**
     * Saves a specific object from the database and returns it
     * @param eventTeamList with specified ID
     * @return id of the saved eventlist
     */
    fun setEventTeamList(eventTeamList: EventTeamList): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(EventTeamList.COLUMN_NAME_TEAM_ID, eventTeamList.teamId)
        contentValues.put(EventTeamList.COLUMN_NAME_EVENT_ID, eventTeamList.eventId)

        Log.i("Database Save", "Saving ${EventTeamList.TABLE_NAME} with the Id ${eventTeamList.id}")

        //robotMedia already exists in DB, update
        if (eventTeamList.id > 0)
        {
            //create the where statement
            val whereStatement = EventTeamList.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(eventTeamList.id.toString() + "")

            //update
            return if (db!!.update(EventTeamList.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                eventTeamList.id.toLong()
            else
                -1
        } else
            return db!!.insert(EventTeamList.TABLE_NAME, null, contentValues)//insert new robotMedia in db

    }

    /**
     * Deletes a specific object from the database
     * @param eventTeamList with specified ID
     * @return successful delete
     */
    fun deleteEventTeamList(eventTeamList: EventTeamList): Boolean
    {
        if (eventTeamList.id > 0)
        {
            //create the where statement
            val whereStatement = EventTeamList.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(eventTeamList.id.toString() + "")

            //delete
            return db!!.delete(EventTeamList.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Checklist Items Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return ChecklistItem converted data
     */
    private fun getChecklistItemFromCursor(cursor: Cursor): ChecklistItem
    {
        val id = cursor.getInt(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_ID))
        val serverId = cursor.getInt(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_SERVER_ID))

        val title = cursor.getString(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_TITLE))
        val description = cursor.getString(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_DESCRIPTION))

        return ChecklistItem(
                id,
                serverId,

                title,
                description)
    }

    /**
     * Gets the ChecklistItem data
     * @param checklistItem if specified, filters checklist items by checklist item id
     * @return object based off given team ID
     */
    fun getChecklistItems(checklistItem: ChecklistItem?): ArrayList<ChecklistItem>?
    {
        val checklistItems = ArrayList<ChecklistItem>()

        //insert columns you are going to use here
        val columns = columns

        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (checklistItem != null)
        {
            whereStatement.append(ChecklistItem.COLUMN_NAME_ID).append(" = ?")
            whereArgs.add(checklistItem.id.toString())
        }

        //select the info from the db
        val cursor = db!!.query(
                ChecklistItem.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null, null)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                checklistItems.add(getChecklistItemFromCursor(cursor))
            }

            cursor.close()

            return checklistItems
        }

        return null
    }

    /**
     * Saves a specific object from the database and returns it
     * @param checklistItem with specified ID
     * @return id of the saved checklistItem
     */
    fun setChecklistItem(checklistItem: ChecklistItem): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(ChecklistItem.COLUMN_NAME_SERVER_ID, checklistItem.serverId.toString())
        contentValues.put(ChecklistItem.COLUMN_NAME_TITLE, checklistItem.title)
        contentValues.put(ChecklistItem.COLUMN_NAME_DESCRIPTION, checklistItem.description)

        Log.i("Database Save", "Saving ${ChecklistItem.TABLE_NAME} with the Id ${checklistItem.id}")

        //robotMedia already exists in DB, update
        if (checklistItem.id > 0)
        {
            //create the where statement
            val whereStatement = ChecklistItem.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(checklistItem.id.toString() + "")

            //update
            return if (db!!.update(ChecklistItem.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                checklistItem.id.toLong()
            else
                -1
        } else
            return db!!.insert(ChecklistItem.TABLE_NAME, null, contentValues)//insert new robotMedia in db

    }

    /**
     * Deletes a specific object from the database
     * @param checklistItem with specified ID
     * @return successful delete
     */
    fun deleteChecklistItem(checklistItem: ChecklistItem): Boolean
    {
        if (checklistItem.id > 0)
        {
            //create the where statement
            val whereStatement = ChecklistItem.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(checklistItem.id.toString() + "")

            //delete
            return db!!.delete(ChecklistItem.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion

    //region Checklist Item Result Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return ChecklistItemResult converted data
     */
    private fun getChecklistItemResultFromCursor(cursor: Cursor): ChecklistItemResult
    {
        val id = cursor.getInt(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_ID))
        val checklistItemId = cursor.getInt(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID))
        val matchId = cursor.getString(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_MATCH_ID))

        val status = cursor.getString(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_STATUS))

        val completedBy = cursor.getString(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY))

        val completedDate = Date(cursor.getLong(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE)))

        val isDraft = cursor.getInt(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_IS_DRAFT)) == 1

        return ChecklistItemResult(
                id,
                checklistItemId,
                matchId,

                status,

                completedBy,

                completedDate,

                isDraft)
    }

    /**
     * Gets the ChecklistItemResult data
     * @param checklistItem if specified, filters checklist item results by checklistItem id
     * @param checklistItemResult if specified, filters checklist item results by checklistItemResult id
     * @param onlyDrafts if true, filters checklist item results by draft
     * @return object based off given team ID
     */
    fun getChecklistItemResults(checklistItem: ChecklistItem?, checklistItemResult: ChecklistItemResult?, onlyDrafts: Boolean): ArrayList<ChecklistItemResult>?
    {
        val checklistItemResults = ArrayList<ChecklistItemResult>()

        //insert columns you are going to use here
        val columns = columns

        val whereStatement = StringBuilder()
        val whereArgs = ArrayList<String>()

        if (checklistItem != null)
        {
            whereStatement.append(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID).append(" = ?")
            whereArgs.add(checklistItem.serverId.toString())
        }

        if (checklistItemResult != null)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ChecklistItemResult.COLUMN_NAME_ID).append(" = ?")
            whereArgs.add(checklistItemResult.id.toString())
        }

        if (onlyDrafts)
        {
            whereStatement.append(if (whereStatement.length > 0) " AND " else "").append(ChecklistItemResult.COLUMN_NAME_IS_DRAFT).append(" = 1")
        }


        val orderBy = ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE + " DESC"

        //select the info from the db
        val cursor = db!!.query(
                ChecklistItemResult.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull<Array<Any>>(whereArgs.toTypedArray()), whereArgs.size, Array<String>::class.java), null, null,
                orderBy)

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                checklistItemResults.add(getChecklistItemResultFromCursor(cursor))
            }

            cursor.close()

            return checklistItemResults
        }

        return null
    }

    /**
     * Saves a specific object from the database and returns it
     * @param checklistItemResult with specified ID
     * @return id of the saved checklistItemResult
     */
    fun setChecklistItemResult(checklistItemResult: ChecklistItemResult): Long
    {
        //set all the values
        val contentValues = ContentValues()
        contentValues.put(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID, checklistItemResult.checklistItemId.toString())
        contentValues.put(ChecklistItemResult.COLUMN_NAME_MATCH_ID, checklistItemResult.matchId)
        contentValues.put(ChecklistItemResult.COLUMN_NAME_STATUS, checklistItemResult.status)
        contentValues.put(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY, checklistItemResult.completedBy)
        contentValues.put(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE, checklistItemResult.completedDate.time.toString())
        contentValues.put(ChecklistItemResult.COLUMN_NAME_IS_DRAFT, if (checklistItemResult.isDraft) 1 else 0)

        Log.i("Database Save", "Saving ${ChecklistItemResult.TABLE_NAME} with the Id ${checklistItemResult.id}")

        //robotMedia already exists in DB, update
        if (checklistItemResult.id > 0)
        {
            //create the where statement
            val whereStatement = ChecklistItemResult.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(checklistItemResult.id.toString() + "")

            //update
            return if (db!!.update(ChecklistItemResult.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                checklistItemResult.id.toLong()
            else
                -1
        } else
            return db!!.insert(ChecklistItemResult.TABLE_NAME, null, contentValues)//insert new robotMedia in db

    }

    /**
     * Deletes a specific object from the database
     * @param checklistItemResult with specified ID
     * @return successful delete
     */
    fun deleteChecklistItemResult(checklistItemResult: ChecklistItemResult): Boolean
    {
        if (checklistItemResult.id > 0)
        {
            //create the where statement
            val whereStatement = ChecklistItemResult.COLUMN_NAME_ID + " = ?"
            val whereArgs = arrayOf(checklistItemResult.id.toString() + "")

            //delete
            return db!!.delete(ChecklistItemResult.TABLE_NAME, whereStatement, whereArgs) >= 1
        }

        return false
    }
    //endregion
}

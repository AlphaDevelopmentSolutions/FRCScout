package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItemResult;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.EventTeamList;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Robot;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfo;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfoKey;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfoKey;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Table;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.User;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition;
import com.alphadevelopmentsolutions.frcscout.Exceptions.UnauthorizedClassException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Database
{
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    public Database(Context context)
    {
        databaseHelper = new DatabaseHelper(context);
    }

    /**
     * Opens the database for usage
     *
     * @return boolean if database was opened successfully
     */
    public boolean open()
    {
        try
        {
            db = databaseHelper.getWritableDatabase();
            return true;
        } catch (SQLException e)
        {
            return false;
        }

    }

    /**
     * Checks if the database is currently open
     *
     * @return boolean if database is open
     */
    public boolean isOpen()
    {
        if(db != null)
            return db.isOpen();

        return false;

    }

    /**
     * Closes the database after usage
     *
     * @return boolean if database was closed successfully
     */
    public boolean close()
    {
        try
        {
            databaseHelper.close();
            return true;
        } catch (SQLException e)
        {
            return false;
        }

    }

    /**
     * Clears a selected table
     * @param tableName table name
     * @param clearDrafts boolean to clear drafts in table
     */
    public void clearTable(String tableName, boolean clearDrafts)
    {
        db.execSQL(String.format("DELETE FROM %s%s", tableName, (clearDrafts) ? "" : " WHERE IsDraft = 0"));
    }

    /**
     * Clears a selected table
     * @param tableName table name
     */
    public void clearTable(String tableName)
    {
        db.execSQL(String.format("DELETE FROM %s", tableName));
    }

    /**
     * Gets the class that called this thread
     * If the class that called this thread is not a child of
     * Table.class, UnauthorizedClassException will be thrown
     * @return class that called thread
     * @throws ClassNotFoundException
     */
    private Class getCallingClass() throws ClassNotFoundException
    {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();

        //skip over the first 2 elements, stack trace works as follows:
        //[0] = Thread
        //[1] = this (usually thread)
        //[2] = direct caller (usually self AKA Database)
        //[3+n] = classes and methods called to get to index <= 2
        //keep iterating through the indexes until we find the class we need
        for(int i = 2; i < stackTraceElements.length; i++)
        {
            //if the current class in the stack trace is not the same as this class
            if(!stackTraceElements[i].getClassName().equals(this.getClass().getName()))
            {
                //set the class
                Class clazz = Class.forName(stackTraceElements[i].getClassName());

                //ensure the superclass of the selected class is from Table.class
                //if not kill, only children of Table.class are allowed to access the database
                if(clazz.getSuperclass() != Table.class)
                    throw new UnauthorizedClassException(Table.class);

                return clazz;
            }
        }

        return null;
    }

    /**
     * Returns all columns inside the an object in string array format
     * @return string array of all columns
     */
    private String[] getColumns()
    {
        ArrayList<String> columns = new ArrayList<>();

        //get all the COLUMN fields from the calling class
        try
        {
            Class clazz = getCallingClass();

            if(clazz != null)
            {
                //retrieve all fields and iterate through finding the COLUMN fields
                for (Field field : clazz.getDeclaredFields())
                {
                    //add the COLUMN field to the columns array
                    if (field.getName().startsWith("COLUMN"))
                        columns.add((String) field.get(field.getName()));
                }
            }
        }
        catch (IllegalAccessException | ClassNotFoundException e)
        {
            e.printStackTrace();
        }

        return Arrays.copyOf(Objects.requireNonNull(columns.toArray()), columns.size(), String[].class);
    }

    //region Event Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return Event object converted data
     */
    private Event getEventFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_ID));
        int yearId = cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_YEAR_ID));
        String blueAllianceId = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_BLUE_ALLIANCE_ID));
        String name = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_NAME));
        String city = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_CITY));
        String stateProvince = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_STATEPROVINCE));
        String country = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_COUNTRY));
        Date startDate = new Date(cursor.getLong(cursor.getColumnIndex(Event.COLUMN_NAME_START_DATE)));
        Date endDate = new Date(cursor.getLong(cursor.getColumnIndex(Event.COLUMN_NAME_END_DATE)));

        return new Event(
                id,
                yearId,
                blueAllianceId,
                name,
                city,
                stateProvince,
                country,
                startDate,
                endDate);
    }

    /**
     * Gets a specific event from the database and returns it
     * @param year if specified, filters events by year id
     * @param event if specified, filters events by event id
     * @return event based off given ID
     */
    public ArrayList<Event> getEvents(@Nullable Year year, @Nullable Event event)
    {
        ArrayList<Event> events = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        //where statement
        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(year != null)
        {
            whereStatement.append(Event.COLUMN_NAME_YEAR_ID).append(" = ?");
            whereArgs.add(String.valueOf(year.getServerId()));
        }

        if(event != null)
        {
            whereStatement
                    .append((whereStatement.length() > 0) ? " AND " : "")
                    .append(Event.COLUMN_NAME_ID).append(" = ?");
            whereArgs.add(String.valueOf(event.getId()));
        }

        //select the info from the db
        Cursor cursor = db.query(
                Event.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                events.add(getEventFromCursor(cursor));
            }

            cursor.close();

            return events;
        }


        return null;
    }

    /**
     * Saves a specific event from the database and returns it
     * @param event with specified ID
     * @return id of the saved event
     */
    public long setEvent(Event event)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(Event.COLUMN_NAME_YEAR_ID, event.getYearId());
        contentValues.put(Event.COLUMN_NAME_BLUE_ALLIANCE_ID, event.getBlueAllianceId());
        contentValues.put(Event.COLUMN_NAME_NAME, event.getName());
        contentValues.put(Event.COLUMN_NAME_CITY, event.getCity());
        contentValues.put(Event.COLUMN_NAME_STATEPROVINCE, event.getStateProvince());
        contentValues.put(Event.COLUMN_NAME_COUNTRY, event.getCountry());
        contentValues.put(Event.COLUMN_NAME_START_DATE, event.getStartDate().getTime());
        contentValues.put(Event.COLUMN_NAME_END_DATE, event.getEndDate().getTime());

        //event already exists in DB, update
        if (event.getId() > 0)
        {
            //create the where statement
            String whereStatement = Event.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {event.getId() + ""};

            //update
            return db.update(Event.TABLE_NAME, contentValues, whereStatement, whereArgs);
        }
        //insert new event in db
        else return db.insert(Event.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific event from the database
     * @param event with specified ID
     * @return successful delete
     */
    public boolean deleteEvent(Event event)
    {
        if (event.getId() > 0)
        {
            //create the where statement
            String whereStatement = Event.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {event.getId() + ""};

            //delete
            return db.delete(Event.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Team Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return Team object converted data
     */
    private Team getTeamFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(Team.COLUMN_NAME_ID));
        String name = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_NAME));
        String city = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_CITY));
        String stateProvince = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_STATEPROVINCE));
        String country = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_COUNTRY));
        int rookieYear = cursor.getInt(cursor.getColumnIndex(Team.COLUMN_NAME_ROOKIE_YEAR));
        String facebookURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_FACEBOOK_URL));
        String twitterURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_TWITTER_URL));
        String instagramURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_INSTAGRAM_URL));
        String youtubeURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_YOUTUBE_URL));
        String websiteURL = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_WEBSITE_URL));
        String imageFileURI = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_IMAGE_FILE_URI));

        return new Team(
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
                imageFileURI);
    }

    /**
     * Gets a specific team from the database and returns it
     * @param event if specified, filters teams by event id
     * @param team if specified, filters teams by team id
     * @return team based off given ID
     */
    public ArrayList<Team> getTeams(@Nullable Event event, @Nullable Match match, @Nullable Team team)
    {
        ArrayList<Team> teams = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(event != null)
        {
            whereStatement.append(Team.COLUMN_NAME_ID + " IN (SELECT " + EventTeamList.COLUMN_NAME_TEAM_ID + " FROM " + EventTeamList.TABLE_NAME + " WHERE " + EventTeamList.COLUMN_NAME_EVENT_ID + " = ?) ");
            whereArgs.add(event.getBlueAllianceId());
        }

        if(match != null)
        {

            whereStatement.append((whereStatement.length() > 0) ? " AND " : "")
                    .append("(" + Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")

                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?) OR ")
                    .append(Team.COLUMN_NAME_ID + " IN (SELECT " + Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID + " FROM " + Match.TABLE_NAME + " WHERE " + Match.COLUMN_NAME_KEY + " = ?))");

            whereArgs.add(match.getKey());
            whereArgs.add(match.getKey());
            whereArgs.add(match.getKey());
            whereArgs.add(match.getKey());
            whereArgs.add(match.getKey());
            whereArgs.add(match.getKey());
        }

        if(team != null)
        {

            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(Team.COLUMN_NAME_ID + " = ? ");
            whereArgs.add(String.valueOf(team.getId()));
        }

        //select the info from the db
        Cursor cursor = db.query(
                Team.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                teams.add(getTeamFromCursor(cursor));
            }

            cursor.close();

            return teams;
        }

        return null;
    }

    /**
     * Saves a specific team from the database and returns it
     * @param team with specified ID
     * @return id of the saved team
     */
    public long setTeam(Team team)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(Team.COLUMN_NAME_NAME, team.getName());
        contentValues.put(Team.COLUMN_NAME_CITY, team.getCity());
        contentValues.put(Team.COLUMN_NAME_STATEPROVINCE, team.getStateProvince());
        contentValues.put(Team.COLUMN_NAME_COUNTRY, team.getCountry());
        contentValues.put(Team.COLUMN_NAME_ROOKIE_YEAR, team.getRookieYear());
        contentValues.put(Team.COLUMN_NAME_FACEBOOK_URL, team.getFacebookURL());
        contentValues.put(Team.COLUMN_NAME_TWITTER_URL, team.getTwitterURL());
        contentValues.put(Team.COLUMN_NAME_INSTAGRAM_URL, team.getInstagramURL());
        contentValues.put(Team.COLUMN_NAME_YOUTUBE_URL, team.getYoutubeURL());
        contentValues.put(Team.COLUMN_NAME_WEBSITE_URL, team.getWebsiteURL());
        contentValues.put(Team.COLUMN_NAME_IMAGE_FILE_URI, team.getImageFileURI());

        //team already exists in DB, update
        if (team.getId() > 0)
        {
            //create the where statement
            String whereStatement = Team.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {team.getId() + ""};

            //insert columns you are going to use here
            String[] columns =
                    {
                            Team.COLUMN_NAME_ID
                    };


            //select the info from the db
            Cursor cursor = db.query(
                    Team.TABLE_NAME,
                    columns,
                    whereStatement,
                    whereArgs,
                    null,
                    null,
                    null);

            if(cursor.getCount() > 0)
                //update
                return db.update(Team.TABLE_NAME, contentValues, whereStatement, whereArgs);

            //record doesn't exist yet, insert
            else
            {
                contentValues.put(Team.COLUMN_NAME_ID, team.getId());
                return db.insert(Team.TABLE_NAME, null, contentValues);
            }
        }
        //insert new team in db
        else
            return db.insert(Team.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific team from the database
     * @param team with specified ID
     * @return successful delete
     */
    public boolean deleteTeam(Team team)
    {
        if (team.getId() > 0)
        {
            //create the where statement
            String whereStatement = Team.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {team.getId() + ""};

            //delete
            return db.delete(Team.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Robot Logic

    /**
     * Gets a specific robot from the database and returns it
     *
     * @param robot with specified ID
     * @return robot based off given ID
     */
    public Robot getRobot(Robot robot)
    {
        //insert columns you are going to use here
        String[] columns =
                {
                        Robot.COLUMN_NAME_NAME,
                        Robot.COLUMN_NAME_TEAM_NUMBER
                };

        //where statement
        String whereStatement = Robot.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {robot.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                Robot.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            //move to the first result in the set
            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex(Robot.COLUMN_NAME_NAME));
            int teamNumber = cursor.getInt(cursor.getColumnIndex(Robot.COLUMN_NAME_TEAM_NUMBER));

            cursor.close();

            return new Robot(robot.getId(), name, teamNumber);
        }


        return null;
    }

    /**
     * Saves a specific robot from the database and returns it
     *
     * @param robot with specified ID
     * @return id of the saved robot
     */
    public long setRobot(Robot robot)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(Robot.COLUMN_NAME_NAME, robot.getName());
        contentValues.put(Robot.COLUMN_NAME_TEAM_NUMBER, robot.getTeamNumber());

        //robot already exists in DB, update
        if (robot.getId() > 0)
        {
            //create the where statement
            String whereStatement = Robot.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {robot.getId() + ""};

            //update
            return db.update(Robot.TABLE_NAME, contentValues, whereStatement, whereArgs);
        }
        //insert new robot in db
        else return db.insert(Robot.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific robot from the database
     *
     * @param robot with specified ID
     * @return successful delete
     */
    public boolean deleteRobot(Robot robot)
    {
        if (robot.getId() > 0)
        {
            //create the where statement
            String whereStatement = Robot.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {robot.getId() + ""};

            //delete
            return db.delete(Robot.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Match Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return Match object converted data
     */
    private Match getMatchFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_ID));
        Date date = new Date(cursor.getLong(cursor.getColumnIndex(Match.COLUMN_NAME_DATE)));
        String eventId = cursor.getString(cursor.getColumnIndex(Match.COLUMN_NAME_EVENT_ID));
        String key = cursor.getString(cursor.getColumnIndex(Match.COLUMN_NAME_KEY));
        Match.Type matchType = Match.Type.getTypeFromString(cursor.getString(cursor.getColumnIndex(Match.COLUMN_NAME_MATCH_TYPE)));
        int setNumber = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_SET_NUMBER));
        int matchNumber = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_MATCH_NUMBER));

        int blueAllianceTeamOneId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID));
        int blueAllianceTeamTwoId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID));
        int blueAllianceTeamThreeId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID));

        int redAllianceTeamOne = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID));
        int redAllianceTeamTwo = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID));
        int redAllianceTeamThree = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID));

        int blueAllianceScore = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE));
        int redAllianceScore = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_SCORE));

        return new Match(
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
                redAllianceScore);
    }

    /**
     * Gets all matches in the database
     * @param event if specified, filters matches by event id
     * @param match if specified, filters matches by match id
     * @param team if specified, filters matches by team id
     * @return all events inside database
     */
    public ArrayList<Match> getMatches(@Nullable Event event, @Nullable Match match, @Nullable Team team)
    {
        ArrayList<Match> matches = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        //where statement
        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(event != null)
        {
            whereStatement.append(Match.COLUMN_NAME_EVENT_ID).append(" = ?");
            whereArgs.add(event.getBlueAllianceId());
        }

        if(match != null)
        {
            whereStatement
                    .append((whereStatement.length() > 0) ? " AND " : "")
                    .append(Match.COLUMN_NAME_KEY).append(" = ?");
            whereArgs.add(match.getKey());
        }

        if(team != null)
        {
            whereStatement
                    .append((whereStatement.length() > 0) ? " AND " : "")
                    .append(team.getId()).append(" IN (")

                    .append(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID).append(", ")
                    .append(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID).append(", ")
                    .append(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID).append(", ")

                    .append(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID).append(", ")
                    .append(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID).append(", ")
                    .append(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID).append(")");
        }

        //select the info from the db
        Cursor cursor = db.query(
                Match.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
                matches.add(getMatchFromCursor(cursor));


            cursor.close();

            return matches;
        }


        return null;
    }

    /**
     * Saves a specific match from the database and returns it
     *
     * @param match with specified ID
     * @return id of the saved match
     */
    public long setMatch(Match match)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(Match.COLUMN_NAME_DATE, match.getDate().getTime());
        contentValues.put(Match.COLUMN_NAME_EVENT_ID, match.getEventId());
        contentValues.put(Match.COLUMN_NAME_KEY, match.getKey());
        contentValues.put(Match.COLUMN_NAME_MATCH_TYPE, match.getMatchType().name());
        contentValues.put(Match.COLUMN_NAME_SET_NUMBER, match.getSetNumber());
        contentValues.put(Match.COLUMN_NAME_MATCH_NUMBER, match.getMatchNumber());

        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID, match.getBlueAllianceTeamOneId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID, match.getBlueAllianceTeamTwoId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID, match.getBlueAllianceTeamThreeId());

        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID, match.getRedAllianceTeamOneId());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID, match.getRedAllianceTeamTwoId());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID, match.getRedAllianceTeamThreeId());

        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE, match.getBlueAllianceScore());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_SCORE, match.getRedAllianceScore());


        //Match already exists in DB, update
        if (match.getId() > 0)
        {
            //create the where statement
            String whereStatement = Match.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {match.getId() + ""};

            //update
            if(db.update(Match.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return match.getId();
            else
                return -1;
        }
        //insert new Match in db
        else return db.insert(Match.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific match from the database
     *
     * @param match with specified ID
     * @return successful delete
     */
    public boolean deleteMatch(Match match)
    {
        if (match.getId() > 0)
        {
            //create the where statement
            String whereStatement = Match.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {match.getId() + ""};

            //delete
            return db.delete(Match.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Scout Card Logic
    
    /**
     * Takes in a cursor with info pulled from database and converts it into a scout card
     * @param cursor info from database
     * @return scoutcard converted data
     */
    private ScoutCard getScoutCardFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_ID));
        String matchId = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_MATCH_ID));
        int teamId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TEAM_ID));
        String eventId = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_EVENT_ID));
        String allianceColor = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR));
        String completedBy = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_BY));

        int preGameStartingLevel = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_LEVEL));
        StartingPosition preGameStartingPosition = StartingPosition.getPositionFromString(cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_POSITION)));
        StartingPiece preGameStartingPiece = StartingPiece.getPieceFromString(cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_PIECE)));

        boolean autonomousExitHabitat = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT)) == 1;
        int autonomousHatchPanelsPickedUp = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP));
        int autonomousHatchPanelsSecuredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS));
        int autonomousHatchPanelsSecured = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED));
        int autonomousCargoPickedUp = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP));
        int autonomousCargoStoredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS));
        int autonomousCargoStored = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED));

        int teleopHatchPanelsPickedUp = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP));
        int teleopHatchPanelsSecuredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS));
        int teleopHatchPanelsSecured = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED));
        int teleopCargoPickedUp = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_CARGO_PICKED_UP));
        int teleopCargoStoredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS));
        int teleopCargoStored = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED));

        int endGameReturnedToHabitat = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT));
        int endGameReturnedToHabitatAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS));

        int defenseRating = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_DEFENSE_RATING));
        int offenseRating = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_OFFENSE_RATING));
        int driveRating = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_DRIVE_RATING));
        String notes = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_NOTES));
        
        Date completedDate = new Date(cursor.getLong(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_DATE)));
        boolean isDraft = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_IS_DRAFT)) == 1;

        return new ScoutCard(
                id,
                matchId,
                teamId,
                eventId,
                allianceColor,
                completedBy,

                preGameStartingLevel,
                preGameStartingPosition,
                preGameStartingPiece,

                autonomousExitHabitat,
                autonomousHatchPanelsPickedUp,
                autonomousHatchPanelsSecuredAttempts,
                autonomousHatchPanelsSecured,
                autonomousCargoPickedUp,
                autonomousCargoStoredAttempts,
                autonomousCargoStored,

                teleopHatchPanelsPickedUp,
                teleopHatchPanelsSecuredAttempts,
                teleopHatchPanelsSecured,
                teleopCargoPickedUp,
                teleopCargoStoredAttempts,
                teleopCargoStored,

                endGameReturnedToHabitat,
                endGameReturnedToHabitatAttempts,

                defenseRating,
                offenseRating,
                driveRating,
                notes,
                completedDate,
                isDraft);
    }

    /**
     * Gets all scout cards for a match at an event
     * @param event if specified, filters scout cards by event id
     * @param match if specified, filters scout cards by match id
     * @param team if specified, filters scout cards by team id
     * @param scoutCard if specified, filters scout cards by scout card id
     * @param onlyDrafts  if true, filters scout cards by draft
     * @return scoutcard based off given info
     */
    public ArrayList<ScoutCard> getScoutCards(@Nullable Event event, @Nullable Match match, @Nullable Team team, @Nullable ScoutCard scoutCard, boolean onlyDrafts)
    {
        ArrayList<ScoutCard> scoutCards = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        //where statement
        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(event != null)
        {
            whereStatement.append(ScoutCard.COLUMN_NAME_EVENT_ID).append(" = ?");
            whereArgs.add(event.getBlueAllianceId());
        }

        if(match != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(ScoutCard.COLUMN_NAME_MATCH_ID).append(" = ?");
            whereArgs.add(match.getKey());
        }

        if(team != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(ScoutCard.COLUMN_NAME_TEAM_ID).append(" = ?");
            whereArgs.add(String.valueOf(team.getId()));
        }

        if(scoutCard != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(ScoutCard.COLUMN_NAME_ID).append(" = ?");
            whereArgs.add(String.valueOf(scoutCard.getId()));
        }

        if(onlyDrafts)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(ScoutCard.COLUMN_NAME_IS_DRAFT).append(" = 1");
        }

        String orderBy = ScoutCard.COLUMN_NAME_MATCH_ID + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                ScoutCard.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                orderBy);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                scoutCards.add(getScoutCardFromCursor(cursor));
            }

            cursor.close();

            return scoutCards;
        }

        return null;
    }

    /**
     * Saves a specific scoutCard from the database and returns it
     *
     * @param scoutCard with specified ID
     * @return id of the saved scoutCard
     */
    public long setScoutCard(ScoutCard scoutCard)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScoutCard.COLUMN_NAME_MATCH_ID, scoutCard.getMatchId());
        contentValues.put(ScoutCard.COLUMN_NAME_TEAM_ID, scoutCard.getTeamId());
        contentValues.put(ScoutCard.COLUMN_NAME_EVENT_ID, scoutCard.getEventId());
        contentValues.put(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR, scoutCard.getAllianceColor());
        contentValues.put(ScoutCard.COLUMN_NAME_COMPLETED_BY, scoutCard.getCompletedBy());

        
        contentValues.put(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_LEVEL, scoutCard.getPreGameStartingLevel());
        contentValues.put(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_POSITION, scoutCard.getPreGameStartingPosition().name());
        contentValues.put(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_PIECE, scoutCard.getPreGameStartingPiece().name());
        
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT, scoutCard.getAutonomousExitHabitat() ? 1 : 0);
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP, scoutCard.getAutonomousHatchPanelsPickedUp());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS, scoutCard.getAutonomousHatchPanelsSecuredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED, scoutCard.getAutonomousHatchPanelsSecured());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP, scoutCard.getAutonomousCargoPickedUp());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS, scoutCard.getAutonomousCargoStoredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED, scoutCard.getAutonomousCargoStored());

        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP, scoutCard.getTeleopHatchPanelsPickedUp());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS, scoutCard.getTeleopHatchPanelsSecuredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED, scoutCard.getTeleopHatchPanelsSecured());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_PICKED_UP, scoutCard.getTeleopCargoPickedUp());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS, scoutCard.getTeleopCargoStoredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED, scoutCard.getTeleopCargoStored());
        
        contentValues.put(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT, scoutCard.getEndGameReturnedToHabitat());
        contentValues.put(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS, scoutCard.getEndGameReturnedToHabitatAttempts());

        contentValues.put(ScoutCard.COLUMN_NAME_DEFENSE_RATING, scoutCard.getDefenseRating());
        contentValues.put(ScoutCard.COLUMN_NAME_OFFENSE_RATING, scoutCard.getOffenseRating());
        contentValues.put(ScoutCard.COLUMN_NAME_DRIVE_RATING, scoutCard.getDriveRating());
        contentValues.put(ScoutCard.COLUMN_NAME_NOTES, scoutCard.getNotes());
        
        contentValues.put(ScoutCard.COLUMN_NAME_COMPLETED_DATE, scoutCard.getCompletedDate().getTime());
        contentValues.put(ScoutCard.COLUMN_NAME_IS_DRAFT, scoutCard.isDraft() ? "1" : "0");

        //scoutCard already exists in DB, update
        if (scoutCard.getId() > 0)
        {
            //create the where statement
            String whereStatement = ScoutCard.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {scoutCard.getId() + ""};

            //update
            if(db.update(ScoutCard.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return scoutCard.getId();
            else
                return -1;
        }
        //insert new scoutCard in db
        else return db.insert(ScoutCard.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific scoutCard from the database
     *
     * @param scoutCard with specified ID
     * @return successful delete
     */
    public boolean deleteScoutCard(ScoutCard scoutCard)
    {
        if (scoutCard.getId() > 0)
        {
            //create the where statement
            String whereStatement = ScoutCard.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {scoutCard.getId() + ""};

            //delete
            return db.delete(ScoutCard.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Scout Card Info Keys Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into a pit card
     * @param cursor info from database
     * @return pitcard converted data
     */
    private ScoutCardInfoKey getScoutCardInfoKeyFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_ID));
        int yearId = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID));

        String keyState = cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_KEY_STATE));
        String keyName = cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_KEY_NAME));

        int sortOrder = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER));
        int minValue = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_MIN_VALUE));
        int maxValue = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_MAX_VALUE));

        boolean nullZeros = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_NULL_ZEROS)) == 1;
        boolean includeInStats = cursor.getInt(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_INCLUDE_IN_STATS)) == 1;

        ScoutCardInfoKey.DataTypes dataType = ScoutCardInfoKey.DataTypes.parseString(cursor.getString(cursor.getColumnIndex(ScoutCardInfoKey.COLUMN_NAME_DATA_TYPE)));

        return new ScoutCardInfoKey(
                id,
                yearId,

                keyState,
                keyName,

                sortOrder,
                minValue,
                maxValue,

                nullZeros,
                includeInStats,

                dataType);
    }

    /**
     * Gets all robot info keys
     * @param year if specified, filters by year id
     * @param scoutCardInfoKey if specified, filters by scoutCardInfoKey id
     * @return object based off given team ID
     */
    public ArrayList<ScoutCardInfoKey> getScoutCardInfoKeys(@Nullable Year year, @Nullable ScoutCardInfoKey scoutCardInfoKey)
    {
        ArrayList<ScoutCardInfoKey> scoutCardInfoKeys = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        //where statement
        StringBuilder whereStatement =  new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(year != null)
        {
            whereStatement.append(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID + " = ? ");
            whereArgs.add(String.valueOf(year.getServerId()));
        }

        if(scoutCardInfoKey != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(ScoutCardInfoKey.COLUMN_NAME_ID + " = ?");
            whereArgs.add(String.valueOf(scoutCardInfoKey.getId()));
        }


        String orderBy = ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER + " ASC";

        //select the info from the db
        Cursor cursor = db.query(
                ScoutCardInfoKey.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                orderBy);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                scoutCardInfoKeys.add(getScoutCardInfoKeyFromCursor(cursor));
            }

            cursor.close();

            return scoutCardInfoKeys;
        }


        return null;
    }

    /**
     * Saves a specific object from the database and returns it
     *
     * @param scoutCardInfoKey with specified ID
     * @return id of the saved scoutCardInfoKey
     */
    public long setScoutCardInfoKey(ScoutCardInfoKey scoutCardInfoKey)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_YEAR_ID, scoutCardInfoKey.getYearId());
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_KEY_STATE, scoutCardInfoKey.getKeyState());
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_KEY_NAME, scoutCardInfoKey.getKeyName());
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_SORT_ORDER, scoutCardInfoKey.getSortOrder());
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_MIN_VALUE, scoutCardInfoKey.getMinValue());
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_MAX_VALUE, scoutCardInfoKey.getMaxValue());
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_NULL_ZEROS, scoutCardInfoKey.isNullZeros() ? 1 : 0);
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_INCLUDE_IN_STATS, scoutCardInfoKey.isIncludeInStats() ? 1 : 0);
        contentValues.put(ScoutCardInfoKey.COLUMN_NAME_DATA_TYPE, scoutCardInfoKey.getDataType().name());


        //scoutCardInfoKey already exists in DB, update
        if (scoutCardInfoKey.getId() > 0)
        {
            //create the where statement
            String whereStatement = ScoutCardInfoKey.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {scoutCardInfoKey.getId() + ""};

            //update
            if(db.update(ScoutCardInfoKey.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return scoutCardInfoKey.getId();
            else
                return -1;
        }
        //insert new scoutCard in db
        else return db.insert(ScoutCardInfoKey.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific scoutCard from the database
     *
     * @param scoutCardInfoKey with specified ID
     * @return successful delete
     */
    public boolean deleteScoutCardInfoKey(ScoutCardInfoKey scoutCardInfoKey)
    {
        if (scoutCardInfoKey.getId() > 0)
        {
            //create the where statement
            String whereStatement = ScoutCardInfoKey.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {scoutCardInfoKey.getId() + ""};

            //delete
            return db.delete(ScoutCardInfoKey.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Robot Info Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into a pit card
     * @param cursor info from database
     * @return pitcard converted data
     */
    private RobotInfo getRobotInfoFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_ID));
        int yearId = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_YEAR_ID));
        String eventId = cursor.getString(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_EVENT_ID));
        int teamId = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_TEAM_ID));

        String propertyState = cursor.getString(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_PROPERTY_STATE));
        String propertyKey = cursor.getString(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_PROPERTY_KEY));
        String propertyValue = cursor.getString(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_PROPERTY_VALUE));

        boolean isDraft = cursor.getInt(cursor.getColumnIndex(RobotInfo.COLUMN_NAME_IS_DRAFT)) == 1;


        return new RobotInfo(
                id,
                yearId,
                eventId,
                teamId,

                propertyState,
                propertyKey,
                propertyValue,

                isDraft);
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
    public ArrayList<RobotInfo> getRobotInfo(@Nullable Year year, @Nullable Event event, @Nullable Team team, @Nullable RobotInfoKey robotInfoKey, @Nullable RobotInfo robotInfo, boolean onlyDrafts)
    {
        ArrayList<RobotInfo> robotInfos = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        //where statement
        StringBuilder whereStatement =  new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(year != null)
        {
            whereStatement.append(RobotInfo.COLUMN_NAME_YEAR_ID + " = ? ");
            whereArgs.add(String.valueOf(year.getServerId()));
        }

        if(event != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotInfo.COLUMN_NAME_EVENT_ID + " = ? ");
            whereArgs.add(event.getBlueAllianceId());
        }

        if(team != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotInfo.COLUMN_NAME_TEAM_ID + " = ? ");
            whereArgs.add(String.valueOf(team.getId()));
        }

        if(robotInfoKey != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotInfo.COLUMN_NAME_PROPERTY_STATE + " = ? AND ").append(RobotInfo.COLUMN_NAME_PROPERTY_KEY + " = ? ");
            whereArgs.add(robotInfoKey.getKeyState());
            whereArgs.add(robotInfoKey.getKeyName());
        }

        if(robotInfo != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotInfo.COLUMN_NAME_ID + " = ? ");
            whereArgs.add(String.valueOf(robotInfo.getId()));
        }

        if(onlyDrafts)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotInfo.COLUMN_NAME_IS_DRAFT + " = 1 ");
        }

        String orderBy = RobotInfo.COLUMN_NAME_ID + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                RobotInfo.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                orderBy);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                robotInfos.add(getRobotInfoFromCursor(cursor));
            }

            cursor.close();

            return robotInfos;
        }


        return null;
    }

    /**
     * Saves a specific object from the database and returns it
     *
     * @param robotInfo with specified ID
     * @return id of the saved robotInfo
     */
    public long setRobotInfo(RobotInfo robotInfo)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(RobotInfo.COLUMN_NAME_YEAR_ID, robotInfo.getYearId());
        contentValues.put(RobotInfo.COLUMN_NAME_EVENT_ID, robotInfo.getEventId());
        contentValues.put(RobotInfo.COLUMN_NAME_TEAM_ID, robotInfo.getTeamId());

        contentValues.put(RobotInfo.COLUMN_NAME_PROPERTY_STATE, robotInfo.getPropertyState());
        contentValues.put(RobotInfo.COLUMN_NAME_PROPERTY_KEY, robotInfo.getPropertyKey());
        contentValues.put(RobotInfo.COLUMN_NAME_PROPERTY_VALUE, robotInfo.getPropertyValue());

        contentValues.put(RobotInfo.COLUMN_NAME_IS_DRAFT, robotInfo.isDraft() ? "1" : "0");

        //try and update first
        //if update fails, no record was in the database so add a new record

            //create the where statement
            String whereStatement =
                    RobotInfo.COLUMN_NAME_YEAR_ID + " = ? AND "  +
                    RobotInfo.COLUMN_NAME_EVENT_ID + " = ? AND " +
                    RobotInfo.COLUMN_NAME_TEAM_ID + " = ? AND " +
                    RobotInfo.COLUMN_NAME_PROPERTY_STATE + " = ? AND " +
                    RobotInfo.COLUMN_NAME_PROPERTY_KEY + " = ? ";
            String[] whereArgs =
                    {
                            String.valueOf(robotInfo.getYearId()),
                            robotInfo.getEventId(),
                            String.valueOf(robotInfo.getTeamId()),

                            robotInfo.getPropertyState(),
                            robotInfo.getPropertyKey()
                    };

            //update
            if(db.update(RobotInfo.TABLE_NAME, contentValues, whereStatement, whereArgs) > 0)
                return robotInfo.getId();
            else
                return db.insert(RobotInfo.TABLE_NAME, null, contentValues);
    }

    /**
     * Deletes a specific scoutCard from the database
     *
     * @param robotInfo with specified ID
     * @return successful delete
     */
    public boolean deleteRobotInfo(RobotInfo robotInfo)
    {
        if (robotInfo.getId() > 0)
        {
            //create the where statement
            String whereStatement = RobotInfo.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {robotInfo.getId() + ""};

            //delete
            return db.delete(RobotInfo.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Robot Info Keys Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into a pit card
     * @param cursor info from database
     * @return pitcard converted data
     */
    private RobotInfoKey getRobotInfoKeyFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_ID));
        int yearId = cursor.getInt(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_YEAR_ID));
        int sortOrder = cursor.getInt(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_SORT_ORDER));

        String keyState = cursor.getString(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_KEY_STATE));
        String keyName = cursor.getString(cursor.getColumnIndex(RobotInfoKey.COLUMN_NAME_KEY_NAME));


        return new RobotInfoKey(
                id,
                yearId,

                keyState,
                keyName,

                sortOrder);
    }

    /**
     * Gets all robot info keys
     * @param year if specified, filters by year id
     * @param robotInfoKey if specified, filters by robotInfoKey id
     * @return object based off given team ID
     */
    public ArrayList<RobotInfoKey> getRobotInfoKeys(@Nullable Year year, @Nullable RobotInfoKey robotInfoKey)
    {
        ArrayList<RobotInfoKey> robotInfoKeys = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        //where statement
        StringBuilder whereStatement =  new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(year != null)
        {
            whereStatement.append(RobotInfoKey.COLUMN_NAME_YEAR_ID + " = ? ");
            whereArgs.add(String.valueOf(year.getServerId()));
        }

        if(robotInfoKey != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotInfoKey.COLUMN_NAME_ID + " = ?");
            whereArgs.add(String.valueOf(robotInfoKey.getId()));
        }


        String orderBy = RobotInfoKey.COLUMN_NAME_SORT_ORDER + " ASC";

        //select the info from the db
        Cursor cursor = db.query(
                RobotInfoKey.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                orderBy);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                robotInfoKeys.add(getRobotInfoKeyFromCursor(cursor));
            }

            cursor.close();

            return robotInfoKeys;
        }


        return null;
    }

    /**
     * Saves a specific object from the database and returns it
     *
     * @param robotInfoKey with specified ID
     * @return id of the saved robotInfoKey
     */
    public long setRobotInfoKey(RobotInfoKey robotInfoKey)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(RobotInfoKey.COLUMN_NAME_YEAR_ID, robotInfoKey.getYearId());
        contentValues.put(RobotInfoKey.COLUMN_NAME_SORT_ORDER, robotInfoKey.getSortOrder());
        contentValues.put(RobotInfoKey.COLUMN_NAME_KEY_STATE, robotInfoKey.getKeyState());
        contentValues.put(RobotInfoKey.COLUMN_NAME_KEY_NAME, robotInfoKey.getKeyName());

        //robotInfoKey already exists in DB, update
        if (robotInfoKey.getId() > 0)
        {
            //create the where statement
            String whereStatement = RobotInfoKey.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {robotInfoKey.getId() + ""};

            //update
            if(db.update(RobotInfoKey.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return robotInfoKey.getId();
            else
                return -1;
        }
        //insert new scoutCard in db
        else return db.insert(RobotInfoKey.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific scoutCard from the database
     *
     * @param robotInfoKey with specified ID
     * @return successful delete
     */
    public boolean deleteRobotInfoKey(RobotInfoKey robotInfoKey)
    {
        if (robotInfoKey.getId() > 0)
        {
            //create the where statement
            String whereStatement = RobotInfoKey.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {robotInfoKey.getId() + ""};

            //delete
            return db.delete(RobotInfoKey.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion
    
    //region User Logic

    /**
     * Gets all objects in the database
     * @param user if specified, filter by user id
     * @return all objects inside database
     */
    public ArrayList<User> getUsers(@Nullable User user)
    {
        ArrayList<User> users = new ArrayList<>();

        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(user != null)
        {
            whereStatement.append(User.COLUMN_NAME_ID + " = ?");
            whereArgs.add(String.valueOf(user.getId()));
        }

        //insert columns you are going to use here
        String[] columns = getColumns();

        //select the info from the db
        Cursor cursor = db.query(
                User.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                int id = cursor.getInt(cursor.getColumnIndex(User.COLUMN_NAME_ID));
                String firstName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME_FIRST_NAME));
                String lastName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME_LAST_NAME));

                users.add(new User(id, firstName, lastName));
            }

            cursor.close();

            return users;
        }


        return null;
    }

    /**
     * Saves a specific scoutCard from the database and returns it
     * @param user with specified ID
     * @return id of the saved user
     */
    public long setUser(User user)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(User.COLUMN_NAME_FIRST_NAME, user.getFirstName());
        contentValues.put(User.COLUMN_NAME_LAST_NAME, user.getLastName());

        //object already exists in DB, update
        if (user.getId() > 0)
        {
            //create the where statement
            String whereStatement = User.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {user.getId() + ""};

            //update
            if(db.update(User.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return user.getId();
            else
                return -1;
        }
        //insert new object in db
        else return db.insert(User.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific object from the database
     * @param user with specified ID
     * @return successful delete
     */
    public boolean deleteUser(User user)
    {
        if (user.getId() > 0)
        {
            //create the where statement
            String whereStatement = User.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {user.getId() + ""};

            //delete
            return db.delete(User.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Robot Media Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into robot media
     * @param cursor info from database
     * @return robotMedia converted data
     */
    private RobotMedia getRobotMediaFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_ID));
        int teamId = cursor.getInt(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_TEAM_ID));
        String fileUri = cursor.getString(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_FILE_URI));
        boolean isDraft = cursor.getInt(cursor.getColumnIndex(RobotMedia.COLUMN_NAME_IS_DRAFT)) == 1;

        return new RobotMedia(
                id,
                teamId,
                fileUri,
                isDraft);
    }

    /**
     * Gets all robot media assigned to a team
     * @param robotMedia if specified, robot media filters by robot media id
     * @param team if specified, filters robot media by team id
     * @param onlyDrafts if true, filters robot media by only drafts
     * @return robotMedia based off given team ID
     */
    public ArrayList<RobotMedia> getRobotMedia(@Nullable RobotMedia robotMedia, @Nullable Team team, boolean onlyDrafts)
    {
        ArrayList<RobotMedia> robotMediaList = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(robotMedia != null)
        {
            whereStatement.append(RobotMedia.COLUMN_NAME_ID).append(" = ?");
            whereArgs.add(String.valueOf(robotMedia.getId()));
        }

        if(team != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotMedia.COLUMN_NAME_TEAM_ID).append(" = ?");
            whereArgs.add(String.valueOf(team.getId()));
        }

        if(onlyDrafts)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(RobotMedia.COLUMN_NAME_IS_DRAFT).append(" = 1");
        }

        //select the info from the db
        Cursor cursor = db.query(
                RobotMedia.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                robotMediaList.add(getRobotMediaFromCursor(cursor));
            }

            cursor.close();

            return robotMediaList;
        }

        return null;
    }

    /**
     * Saves a specific robotMedia from the database and returns it
     *
     * @param robotMedia with specified ID
     * @return id of the saved robotMedia
     */
    public long setRobotMedia(RobotMedia robotMedia)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(RobotMedia.COLUMN_NAME_TEAM_ID, robotMedia.getTeamId());
        contentValues.put(RobotMedia.COLUMN_NAME_FILE_URI, robotMedia.getFileUri());
        contentValues.put(RobotMedia.COLUMN_NAME_IS_DRAFT, robotMedia.isDraft() ? "1" : "0");

        //robotMedia already exists in DB, update
        if (robotMedia.getId() > 0)
        {
            //create the where statement
            String whereStatement = RobotMedia.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {robotMedia.getId() + ""};

            //update
            if(db.update(RobotMedia.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return robotMedia.getId();
            else
                return -1;
        }
        //insert new robotMedia in db
        else return db.insert(RobotMedia.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific robotMedia from the database
     * @param robotMedia with specified ID
     * @return successful delete
     */
    public boolean deleteRobotMedia(RobotMedia robotMedia)
    {
        if (robotMedia.getId() > 0)
        {
            //create the where statement
            String whereStatement = RobotMedia.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {robotMedia.getId() + ""};

            //delete
            return db.delete(RobotMedia.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Year Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into object
     * @param cursor info from database
     * @return years converted data
     */
    private Year getYearsFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(Year.COLUMN_NAME_ID));
        int serverId = cursor.getInt(cursor.getColumnIndex(Year.COLUMN_NAME_SERVER_ID));
        String name = cursor.getString(cursor.getColumnIndex(Year.COLUMN_NAME_NAME));
        Date startDate = new Date(cursor.getLong(cursor.getColumnIndex(Year.COLUMN_NAME_START_DATE)));
        Date endDate = new Date(cursor.getLong(cursor.getColumnIndex(Year.COLUMN_NAME_END_DATE)));
        String fileUri = cursor.getString(cursor.getColumnIndex(Year.COLUMN_NAME_IMAGE_URI));

        return new Year(
                id,
                serverId,
                name,
                startDate,
                endDate,
                fileUri);
    }

    /**
     * Gets all year assigned to a team
     * @param year if specified, object filters by year id
     * @return year based off given team ID
     */
    public ArrayList<Year> getYears(@Nullable Year year)
    {
        ArrayList<Year> yearList = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(year != null)
        {
            whereStatement.append(Year.COLUMN_NAME_SERVER_ID).append(" = ?");
            whereArgs.add(String.valueOf(year.getServerId()));
        }

        //select the info from the db
        Cursor cursor = db.query(
                Year.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                yearList.add(getYearsFromCursor(cursor));
            }

            cursor.close();

            return yearList;
        }

        return null;
    }

    /**
     * Saves a specific year from the database and returns it
     * @param year with specified ID
     * @return id of the saved year
     */
    public long setYears(Year year)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(Year.COLUMN_NAME_SERVER_ID, year.getServerId());
        contentValues.put(Year.COLUMN_NAME_NAME, year.getName());
        contentValues.put(Year.COLUMN_NAME_START_DATE, year.getStartDate().getTime());
        contentValues.put(Year.COLUMN_NAME_END_DATE, year.getEndDate().getTime());
        contentValues.put(Year.COLUMN_NAME_IMAGE_URI, year.getImageUri());

        //year already exists in DB, update
        if (year.getId() > 0)
        {
            //create the where statement
            String whereStatement = Year.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {year.getId() + ""};

            //update
            if(db.update(Year.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return year.getId();
            else
                return -1;
        }
        //insert new year in db
        else return db.insert(Year.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific year from the database
     * @param year with specified ID
     * @return successful delete
     */
    public boolean deleteYears(Year year)
    {
        if (year.getId() > 0)
        {
            //create the where statement
            String whereStatement = Year.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {year.getId() + ""};

            //delete
            return db.delete(Year.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Event Team List Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return eventeamlist converted data
     */
    private EventTeamList getEventTeamListFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(EventTeamList.COLUMN_NAME_ID));
        int teamId = cursor.getInt(cursor.getColumnIndex(EventTeamList.COLUMN_NAME_TEAM_ID));
        String eventId = cursor.getString(cursor.getColumnIndex(EventTeamList.COLUMN_NAME_EVENT_ID));

        return new EventTeamList(
                id,
                teamId,
                eventId);
    }

    /**
     * Gets the event team list data based off an event
     * @param eventTeamList if specified, filters event team list by event team list id
     * @param event if specified, filters event team list by event id
     * @return object based off given team ID
     */
    public ArrayList<EventTeamList> getEventTeamLists(@Nullable EventTeamList eventTeamList, @Nullable Event event)
    {
        ArrayList<EventTeamList> eventTeamLists = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(eventTeamList != null)
        {
            whereStatement.append(EventTeamList.COLUMN_NAME_EVENT_ID).append(" = ?");
            whereArgs.add(String.valueOf(eventTeamList.getId()));
        }

        if(event != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(EventTeamList.COLUMN_NAME_EVENT_ID).append(" = ?");
            whereArgs.add(event.getBlueAllianceId());
        }

        String orderBy = EventTeamList.COLUMN_NAME_TEAM_ID + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                EventTeamList.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                orderBy);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                eventTeamLists.add(getEventTeamListFromCursor(cursor));
            }

            cursor.close();

            return eventTeamLists;
        }

        return null;
    }

    /**
     * Saves a specific object from the database and returns it
     * @param eventTeamList with specified ID
     * @return id of the saved eventlist
     */
    public long setEventTeamList(EventTeamList eventTeamList)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(EventTeamList.COLUMN_NAME_TEAM_ID, eventTeamList.getTeamId());
        contentValues.put(EventTeamList.COLUMN_NAME_EVENT_ID, eventTeamList.getEventId());

        //robotMedia already exists in DB, update
        if (eventTeamList.getId() > 0)
        {
            //create the where statement
            String whereStatement = EventTeamList.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {eventTeamList.getId() + ""};

            //update
            if(db.update(EventTeamList.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return eventTeamList.getId();
            else
                return -1;
        }
        //insert new robotMedia in db
        else return db.insert(EventTeamList.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific object from the database
     * @param eventTeamList with specified ID
     * @return successful delete
     */
    public boolean deleteEventTeamList(EventTeamList eventTeamList)
    {
        if (eventTeamList.getId() > 0)
        {
            //create the where statement
            String whereStatement = EventTeamList.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {eventTeamList.getId() + ""};

            //delete
            return db.delete(EventTeamList.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Checklist Items Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return ChecklistItem converted data
     */
    private ChecklistItem getChecklistItemFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_ID));
        int serverId = cursor.getInt(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_SERVER_ID));

        String title = cursor.getString(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndex(ChecklistItem.COLUMN_NAME_DESCRIPTION));

        return new ChecklistItem(
                id,
                serverId,

                title,
                description);
    }

    /**
     * Gets the ChecklistItem data
     * @param checklistItem if specified, filters checklist items by checklist item id
     * @return object based off given team ID
     */
    public ArrayList<ChecklistItem> getChecklistItems(@Nullable ChecklistItem checklistItem)
    {
        ArrayList<ChecklistItem> checklistItems = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(checklistItem != null)
        {
            whereStatement.append(ChecklistItem.COLUMN_NAME_ID).append(" = ?");
            whereArgs.add(String.valueOf(checklistItem.getId()));
        }

        //select the info from the db
        Cursor cursor = db.query(
                ChecklistItem.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                checklistItems.add(getChecklistItemFromCursor(cursor));
            }

            cursor.close();

            return checklistItems;
        }

        return null;
    }

    /**
     * Saves a specific object from the database and returns it
     * @param checklistItem with specified ID
     * @return id of the saved checklistItem
     */
    public long setChecklistItem(ChecklistItem checklistItem)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(ChecklistItem.COLUMN_NAME_SERVER_ID, String.valueOf(checklistItem.getServerId()));
        contentValues.put(ChecklistItem.COLUMN_NAME_TITLE, checklistItem.getTitle());
        contentValues.put(ChecklistItem.COLUMN_NAME_DESCRIPTION, checklistItem.getDescription());

        //robotMedia already exists in DB, update
        if (checklistItem.getId() > 0)
        {
            //create the where statement
            String whereStatement = ChecklistItem.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {checklistItem.getId() + ""};

            //update
            if(db.update(ChecklistItem.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return checklistItem.getId();
            else
                return -1;
        }
        //insert new robotMedia in db
        else return db.insert(ChecklistItem.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific object from the database
     * @param checklistItem with specified ID
     * @return successful delete
     */
    public boolean deleteChecklistItem(ChecklistItem checklistItem)
    {
        if (checklistItem.getId() > 0)
        {
            //create the where statement
            String whereStatement = ChecklistItem.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {checklistItem.getId() + ""};

            //delete
            return db.delete(ChecklistItem.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Checklist Item Result Logic

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return ChecklistItemResult converted data
     */
    private ChecklistItemResult getChecklistItemResultFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_ID));
        int checklistItemId = cursor.getInt(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID));
        String matchId = cursor.getString(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_MATCH_ID));

        String status = cursor.getString(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_STATUS));

        String completedBy = cursor.getString(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY));

        Date completedDate = new Date(cursor.getLong(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE)));

        boolean isDraft = cursor.getInt(cursor.getColumnIndex(ChecklistItemResult.COLUMN_NAME_IS_DRAFT)) == 1;

        return new ChecklistItemResult(
                id,
                checklistItemId,
                matchId,

                status,

                completedBy,

                completedDate,

                isDraft);
    }

    /**
     * Gets the ChecklistItemResult data
     * @param checklistItem if specified, filters checklist item results by checklistItem id
     * @param checklistItemResult if specified, filters checklist item results by checklistItemResult id
     * @param onlyDrafts if true, filters checklist item results by draft
     * @return object based off given team ID
     */
    public ArrayList<ChecklistItemResult> getChecklistItemResults(@Nullable ChecklistItem checklistItem, @Nullable ChecklistItemResult checklistItemResult, boolean onlyDrafts)
    {
        ArrayList<ChecklistItemResult> checklistItemResults = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getColumns();

        StringBuilder whereStatement = new StringBuilder();
        ArrayList<String> whereArgs = new ArrayList<>();

        if(checklistItem != null)
        {
            whereStatement.append(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID).append(" = ?");
            whereArgs.add(String.valueOf(checklistItem.getServerId()));
        }

        if(checklistItemResult != null)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(ChecklistItemResult.COLUMN_NAME_ID).append(" = ?");
            whereArgs.add(String.valueOf(checklistItemResult.getId()));
        }

        if(onlyDrafts)
        {
            whereStatement.append((whereStatement.length() > 0) ? " AND " : "").append(ChecklistItemResult.COLUMN_NAME_IS_DRAFT).append(" = 1");
        }


        String orderBy = ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                ChecklistItemResult.TABLE_NAME,
                columns,
                whereStatement.toString(),
                Arrays.copyOf(Objects.requireNonNull(whereArgs.toArray()), whereArgs.size(), String[].class),
                null,
                null,
                orderBy);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                checklistItemResults.add(getChecklistItemResultFromCursor(cursor));
            }

            cursor.close();

            return checklistItemResults;
        }

        return null;
    }

    /**
     * Saves a specific object from the database and returns it
     * @param checklistItemResult with specified ID
     * @return id of the saved checklistItemResult
     */
    public long setChecklistItemResult(ChecklistItemResult checklistItemResult)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID, String.valueOf(checklistItemResult.getChecklistItemId()));
        contentValues.put(ChecklistItemResult.COLUMN_NAME_MATCH_ID, checklistItemResult.getMatchId());
        contentValues.put(ChecklistItemResult.COLUMN_NAME_STATUS, checklistItemResult.getStatus());
        contentValues.put(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY, checklistItemResult.getCompletedBy());
        contentValues.put(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE, String.valueOf(checklistItemResult.getCompletedDate().getTime()));
        contentValues.put(ChecklistItemResult.COLUMN_NAME_IS_DRAFT, checklistItemResult.isDraft() ? 1 : 0);

        //robotMedia already exists in DB, update
        if (checklistItemResult.getId() > 0)
        {
            //create the where statement
            String whereStatement = ChecklistItemResult.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {checklistItemResult.getId() + ""};

            //update
            if(db.update(ChecklistItemResult.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return checklistItemResult.getId();
            else
                return -1;
        }
        //insert new robotMedia in db
        else return db.insert(ChecklistItemResult.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific object from the database
     * @param checklistItemResult with specified ID
     * @return successful delete
     */
    public boolean deleteChecklistItemResult(ChecklistItemResult checklistItemResult)
    {
        if (checklistItemResult.getId() > 0)
        {
            //create the where statement
            String whereStatement = ChecklistItemResult.COLUMN_NAME_ID + " = ?";
            String[] whereArgs = {checklistItemResult.getId() + ""};

            //delete
            return db.delete(ChecklistItemResult.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion
}

package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

public class Database
{
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Context context;

    public Database(Context context)
    {
        databaseHelper = new DatabaseHelper(context);
        this.context = context;
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

    public void clear()
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(Event.TABLE_NAME);
        tableNames.add(Team.TABLE_NAME);
        tableNames.add(Robot.TABLE_NAME);
        tableNames.add(Match.TABLE_NAME);
        tableNames.add(User.TABLE_NAME);
        tableNames.add(RobotMedia.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName);
    }

    public void clearEvents()
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(Event.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName);
    }

    public void clearUsers()
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(User.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName);
    }

    public void clearScoutCards(boolean clearDrafts)
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(ScoutCard.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName + ((clearDrafts) ? "" : " WHERE IsDraft = 0"));
    }

    public void clearTeams()
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(Team.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName);
    }

    public void clearPitCards(boolean clearDrafts)
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(PitCard.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName + ((clearDrafts) ? "" : " WHERE IsDraft = 0"));
    }

    public void clearEventTeamList()
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(EventTeamList.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName);
    }

    public void clearMatches()
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(Match.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName);
    }

    public void clearRobotMedia(boolean clearDrafts)
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(RobotMedia.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName + ((clearDrafts) ? "" : " WHERE IsDraft = 0"));
    }



    //region Event Logic

    /**
     * Returns all columns inside the events table in string array format
     * @return string array of all columns
     */
    private String[] getEventsColumns()
    {
        return new String[]
                {
                        Event.COLUMN_NAME_ID,
                        Event.COLUMN_NAME_BLUE_ALLIANCE_ID,
                        Event.COLUMN_NAME_NAME,
                        Event.COLUMN_NAME_CITY,
                        Event.COLUMN_NAME_STATEPROVINCE,
                        Event.COLUMN_NAME_COUNTRY,
                        Event.COLUMN_NAME_START_DATE,
                        Event.COLUMN_NAME_END_DATE
                };
    }

    /**
     * Takes in a cursor with info pulled from database and converts it into an object
     * @param cursor info from database
     * @return Event object converted data
     */
    private Event getEventFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_ID));
        String blueAllianceId = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_BLUE_ALLIANCE_ID));
        String name = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_NAME));
        String city = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_CITY));
        String stateProvince = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_STATEPROVINCE));
        String country = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_COUNTRY));
        Date startDate = new Date(cursor.getLong(cursor.getColumnIndex(Event.COLUMN_NAME_START_DATE)));
        Date endDate = new Date(cursor.getLong(cursor.getColumnIndex(Event.COLUMN_NAME_END_DATE)));

        return new Event(
                id,
                blueAllianceId,
                name,
                city,
                stateProvince,
                country,
                startDate,
                endDate);
    }

    /**
     * Gets all events in the database
     *
     * @return all events inside database
     */
    public ArrayList<Event> getEvents()
    {
        ArrayList<Event> events = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getEventsColumns();

        //select the info from the db
        Cursor cursor = db.query(
                Event.TABLE_NAME,
                columns,
                null,
                null,
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
     * Gets a specific event from the database and returns it
     *
     * @param event with specified ID
     * @return event based off given ID
     */
    public Event getEvent(Event event)
    {
        //insert columns you are going to use here
        String[] columns = getEventsColumns();

        //where statement
        String whereStatement = Event.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {event.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                Event.TABLE_NAME,
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

            Event databaseEvent = getEventFromCursor(cursor);

            cursor.close();

            return databaseEvent;
        }


        return null;
    }

    /**
     * Saves a specific event from the database and returns it
     *
     * @param event with specified ID
     * @return id of the saved event
     */
    public long setEvent(Event event)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
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
            String whereArgs[] = {event.getId() + ""};

            //update
            return db.update(Event.TABLE_NAME, contentValues, whereStatement, whereArgs);
        }
        //insert new event in db
        else return db.insert(Event.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific event from the database
     *
     * @param event with specified ID
     * @return successful delete
     */
    public boolean deleteEvent(Event event)
    {
        if (event.getId() > 0)
        {
            //create the where statement
            String whereStatement = Event.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {event.getId() + ""};

            //delete
            return db.delete(Event.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Team Logic

    /**
     * Returns all columns inside the Teams table in string array format
     * @return string array of all columns
     */
    private String[] getTeamColumns()
    {
        return new String[]
                {
                        Team.COLUMN_NAME_ID,
                        Team.COLUMN_NAME_NAME,
                        Team.COLUMN_NAME_CITY,
                        Team.COLUMN_NAME_STATEPROVINCE,
                        Team.COLUMN_NAME_COUNTRY,
                        Team.COLUMN_NAME_ROOKIE_YEAR,
                        Team.COLUMN_NAME_FACEBOOK_URL,
                        Team.COLUMN_NAME_TWITTER_URL,
                        Team.COLUMN_NAME_INSTAGRAM_URL,
                        Team.COLUMN_NAME_YOUTUBE_URL,
                        Team.COLUMN_NAME_WEBSITE_URL,
                        Team.COLUMN_NAME_IMAGE_FILE_URI
                };
    }

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
     * Gets all teams in the database
     * @return all teams inside database
     */
    public ArrayList<Team> getTeams()
    {
        ArrayList<Team> teams = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getTeamColumns();

        //select the info from the db
        Cursor cursor = db.query(
                Team.TABLE_NAME,
                columns,
                null,
                null,
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
     * Gets all teams in the database at a specific event
     * @return all teams inside database
     */
    public ArrayList<Team> getTeamsAtEvent(ArrayList<EventTeamList> eventTeamLists)
    {
        ArrayList<Team> teams = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getTeamColumns();

        //where statement
        StringBuilder whereStatement = new StringBuilder();
        String[] whereArgs = new String[eventTeamLists.size()];

        for(int i = 0; i < eventTeamLists.size(); i++)
        {
            whereStatement.append(Team.COLUMN_NAME_ID + " = ?");

            if(i + 1 < eventTeamLists.size())
                whereStatement.append(" OR ");
            else
                whereStatement.append(" ");



            whereArgs[i]  = String.valueOf(eventTeamLists.get(i).getTeamId());
        }

        //select the info from the db
        Cursor cursor = db.query(
                Team.TABLE_NAME,
                columns,
                whereStatement.toString(),
                whereArgs,
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
     * Gets a specific team from the database and returns it
     *
     * @param team with specified ID
     * @return team based off given ID
     */
    public Team getTeam(Team team)
    {
        //insert columns you are going to use here
        String[] columns = getTeamColumns();

        //where statement
        String whereStatement = Team.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {team.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                Team.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {

            if(cursor.getCount() > 0)
            {
                //move to the first result in the set
                cursor.moveToFirst();

                Team databaseTeam = getTeamFromCursor(cursor);

                cursor.close();

                return databaseTeam;

            }
        }


        return null;
    }

    /**
     * Saves a specific team from the database and returns it
     *
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
            String whereArgs[] = {team.getId() + ""};

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
     *
     * @param team with specified ID
     * @return successful delete
     */
    public boolean deleteTeam(Team team)
    {
        if (team.getId() > 0)
        {
            //create the where statement
            String whereStatement = Team.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {team.getId() + ""};

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
            String whereArgs[] = {robot.getId() + ""};

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
            String whereArgs[] = {robot.getId() + ""};

            //delete
            return db.delete(Robot.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Match Logic

    /**
     * Returns all columns inside the match table in string array format
     * @return string array of all columns
     */
    private String[] getMatchColumns()
    {
        return new String[]
                {
                        Match.COLUMN_NAME_ID,
                        Match.COLUMN_NAME_DATE,
                        Match.COLUMN_NAME_EVENT_ID,
                        Match.COLUMN_NAME_KEY,
                        Match.COLUMN_NAME_MATCH_TYPE,
                        Match.COLUMN_NAME_SET_NUMBER,
                        Match.COLUMN_NAME_MATCH_NUMBER,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE,
                        Match.COLUMN_NAME_RED_ALLIANCE_SCORE,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID,
                };
    }

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
     * Gets all events in the database
     * @return all events inside database
     */
    public ArrayList<Match> getMatches(Team team, Event event)
    {
        ArrayList<Match> matches = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getMatchColumns();

        //where statement
        String whereStatement = Match.COLUMN_NAME_EVENT_ID + " = ? AND (" +
                Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID + " = ? OR " +
                Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID + " = ? OR " +
                Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID + " = ? OR " +

                Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID + " = ? OR " +
                Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID + " = ? OR " +
                Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID + " = ? )";
        String[] whereArgs = {event.getBlueAllianceId(),
                String.valueOf(team.getId()),
                String.valueOf(team.getId()),
                String.valueOf(team.getId()),
                String.valueOf(team.getId()),
                String.valueOf(team.getId()),
                String.valueOf(team.getId()),
        };

        //select the info from the db
        Cursor cursor = db.query(
                Match.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while (cursor.moveToNext())
            {

                matches.add(getMatchFromCursor(cursor));
            }

            cursor.close();

            return matches;
        }


        return null;
    }

    /**
     * Gets a specific match from the database and returns it
     *
     * @param match with specified ID
     * @return match based off given ID
     */
    public Match getMatch(Match match)
    {
        //insert columns you are going to use here
        String[] columns = getMatchColumns();


        //where statement
        String whereStatement = Match.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {match.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                Match.TABLE_NAME,
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

            Match databaseMatch = getMatchFromCursor(cursor);

            cursor.close();

            return databaseMatch;
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
            String whereArgs[] = {match.getId() + ""};

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
            String whereArgs[] = {match.getId() + ""};

            //delete
            return db.delete(Match.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Scout Card Logic

    /**
     * Returns all columns inside the scout card table in string array format
     * @return string array of all columns
     */
    private String[] getScoutCardColumns()
    {
        return new String[]
            {
                    ScoutCard.COLUMN_NAME_ID,
                    ScoutCard.COLUMN_NAME_MATCH_ID,
                    ScoutCard.COLUMN_NAME_TEAM_ID,
                    ScoutCard.COLUMN_NAME_EVENT_ID,
                    ScoutCard.COLUMN_NAME_ALLIANCE_COLOR,
                    ScoutCard.COLUMN_NAME_COMPLETED_BY,

                    ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_LEVEL,
                    ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_POSITION,
                    ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_PIECE,

                    ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT,
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP,
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS,
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED,
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP,
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS,
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED,

                    ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP,
                    ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS,
                    ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED,
                    ScoutCard.COLUMN_NAME_TELEOP_CARGO_PICKED_UP,
                    ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS,
                    ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED,

                    ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT,
                    ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS,

                    ScoutCard.COLUMN_NAME_DEFENSE_RATING,
                    ScoutCard.COLUMN_NAME_OFFENSE_RATING,
                    ScoutCard.COLUMN_NAME_DRIVE_RATING,
                    ScoutCard.COLUMN_NAME_NOTES,
                    ScoutCard.COLUMN_NAME_COMPLETED_BY,

                    ScoutCard.COLUMN_NAME_COMPLETED_DATE,
                    ScoutCard.COLUMN_NAME_IS_DRAFT
            };    
    }
    
    
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
     * Gets all scout cards assigned to a team at an event
     * @param team with specified ID
     * @param event with specified ID
     * @return scoutcard based off given team ID
     */
    public ArrayList<ScoutCard> getScoutCards(Team team, Event event, boolean onlyDrafts)
    {
        ArrayList<ScoutCard> scoutCards = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getScoutCardColumns();

        //where statement
        String whereStatement = ScoutCard.COLUMN_NAME_TEAM_ID + " = ? AND " + ScoutCard.COLUMN_NAME_EVENT_ID + " = ? " + ((onlyDrafts) ? " AND " + ScoutCard.COLUMN_NAME_IS_DRAFT + " = 1" : "");
        String[] whereArgs = {team.getId() + "", event.getBlueAllianceId()};
        String orderBy = ScoutCard.COLUMN_NAME_MATCH_ID + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                ScoutCard.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
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
     * Gets all scout cards for a match at an event
     * @param match with specified ID
     * @param event with specified ID
     * @return scoutcard based off given team ID
     */
    public ArrayList<ScoutCard> getScoutCards(Match match, Event event, boolean onlyDrafts)
    {
        ArrayList<ScoutCard> scoutCards = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getScoutCardColumns();

        //where statement
        String whereStatement = ScoutCard.COLUMN_NAME_MATCH_ID + " = ? AND " + ScoutCard.COLUMN_NAME_EVENT_ID + " = ? " + ((onlyDrafts) ? " AND " + ScoutCard.COLUMN_NAME_IS_DRAFT + " = 1" : "");
        String[] whereArgs = {match.getKey() + "", event.getBlueAllianceId()};
        String orderBy = ScoutCard.COLUMN_NAME_MATCH_ID + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                ScoutCard.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
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
     * Gets a specific object from the database and returns it
     *
     * @param scoutCard with specified ID
     * @return scoutcard based off given ID
     */
    public ScoutCard getScoutCard(ScoutCard scoutCard)
    {
        //insert columns you are going to use here
        String[] columns = getScoutCardColumns();

        //where statement
        String whereStatement = ScoutCard.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {scoutCard.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                ScoutCard.TABLE_NAME,
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

            ScoutCard databaseScoutCard = getScoutCardFromCursor(cursor);
            cursor.close();

            return databaseScoutCard;
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
            String whereArgs[] = {scoutCard.getId() + ""};

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
            String whereArgs[] = {scoutCard.getId() + ""};

            //delete
            return db.delete(ScoutCard.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Pit Card Logic

    /**
     * Returns all columns inside the scout card table in string array format
     * @return string array of all columns
     */
    private String[] getPitCardColumns()
    {
        return new String[]
                {
                        PitCard.COLUMN_NAME_ID,
                        PitCard.COLUMN_NAME_TEAM_ID,
                        PitCard.COLUMN_NAME_EVENT_ID,

                        PitCard.COLUMN_NAME_DRIVE_STYLE,
                        PitCard.COLUMN_NAME_ROBOT_WEIGHT,
                        PitCard.COLUMN_NAME_ROBOT_LENGTH,
                        PitCard.COLUMN_NAME_ROBOT_WIDTH,
                        PitCard.COLUMN_NAME_ROBOT_HEIGHT,

                        PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT,
                        PitCard.COLUMN_NAME_AUTO_HATCH,
                        PitCard.COLUMN_NAME_AUTO_CARGO,

                        PitCard.COLUMN_NAME_TELEOP_HATCH,
                        PitCard.COLUMN_NAME_TELEOP_CARGO,

                        PitCard.COLUMN_NAME_RETURN_TO_HABITAT,

                        PitCard.COLUMN_NAME_NOTES,

                        PitCard.COLUMN_NAME_COMPLETED_BY,
                        PitCard.COLUMN_NAME_IS_DRAFT
                };
    }


    /**
     * Takes in a cursor with info pulled from database and converts it into a pit card
     * @param cursor info from database
     * @return pitcard converted data
     */
    private PitCard getPitCardFromCursor(Cursor cursor)
    {
        int id = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_ID));
        int teamId = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_TEAM_ID));
        String eventId = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_EVENT_ID));

        String driveStyle = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_DRIVE_STYLE));
        String robotWeight = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_ROBOT_WEIGHT));
        String robotLength = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_ROBOT_LENGTH));
        String robotWidth = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_ROBOT_WIDTH));
        String robotHeight = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_ROBOT_HEIGHT));

        String autoExitHabitat = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT));
        String autoHatch = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_HATCH));
        String autoCargo = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_CARGO));

        String teleopHatch = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_HATCH));
        String teleopCargo = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_CARGO));

        String returnToHabitat = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_RETURN_TO_HABITAT));

        String notes = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_NOTES));
        String completedBy = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_COMPLETED_BY));
        boolean isDraft = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_IS_DRAFT)) == 1;


        return new PitCard(
                id,
                teamId,
                eventId,

                driveStyle,
                robotWeight,
                robotLength,
                robotWidth,
                robotHeight,

                autoExitHabitat,
                autoHatch,
                autoCargo,

                teleopHatch,
                teleopCargo,
                returnToHabitat,

                notes,

                completedBy,
                isDraft);
    }

    /**
     * Gets all pit cards assigned to a team
     *
     * @param team with specified ID
     * @return object based off given team ID
     */
    public ArrayList<PitCard> getPitCards(Team team, Event event, boolean onlyDrafts)
    {
        ArrayList<PitCard> pitCards = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getPitCardColumns();

        //where statement
        String whereStatement = PitCard.COLUMN_NAME_TEAM_ID + " = ? AND " + ScoutCard.COLUMN_NAME_EVENT_ID + " = ? " + ((onlyDrafts) ? " AND " + PitCard.COLUMN_NAME_IS_DRAFT + " = 1" : "");
        String[] whereArgs = {team.getId() + "", event.getBlueAllianceId()};
        String orderBy = PitCard.COLUMN_NAME_ID + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                PitCard.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
                null,
                null,
                orderBy);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                pitCards.add(getPitCardFromCursor(cursor));
            }

            cursor.close();

            return pitCards;
        }


        return null;
    }

    /**
     * Gets a specific object from the database and returns it
     *
     * @param pitCard with specified ID
     * @return pitCard based off given ID
     */
    public PitCard getPitCard(PitCard pitCard)
    {
        //insert columns you are going to use here
        String[] columns = getPitCardColumns();

        //where statement
        String whereStatement = PitCard.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {pitCard.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                PitCard.TABLE_NAME,
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

            return getPitCardFromCursor(cursor);
        }


        return null;
    }

    /**
     * Saves a specific object from the database and returns it
     *
     * @param pitCard with specified ID
     * @return id of the saved pitCard
     */
    public long setPitCard(PitCard pitCard)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(PitCard.COLUMN_NAME_TEAM_ID, pitCard.getTeamId());
        contentValues.put(PitCard.COLUMN_NAME_EVENT_ID, pitCard.getEventId());

        contentValues.put(PitCard.COLUMN_NAME_DRIVE_STYLE, pitCard.getDriveStyle());
        contentValues.put(PitCard.COLUMN_NAME_ROBOT_WEIGHT, pitCard.getRobotWeight());
        contentValues.put(PitCard.COLUMN_NAME_ROBOT_LENGTH, pitCard.getRobotLength());
        contentValues.put(PitCard.COLUMN_NAME_ROBOT_WIDTH, pitCard.getRobotWidth());
        contentValues.put(PitCard.COLUMN_NAME_ROBOT_HEIGHT, pitCard.getRobotHeight());

        contentValues.put(PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT, pitCard.getAutoExitHabitat());
        contentValues.put(PitCard.COLUMN_NAME_AUTO_HATCH, pitCard.getAutoHatch());
        contentValues.put(PitCard.COLUMN_NAME_AUTO_CARGO, pitCard.getAutoCargo());

        contentValues.put(PitCard.COLUMN_NAME_TELEOP_HATCH, pitCard.getTeleopHatch());
        contentValues.put(PitCard.COLUMN_NAME_TELEOP_CARGO, pitCard.getTeleopCargo());

        contentValues.put(PitCard.COLUMN_NAME_RETURN_TO_HABITAT, pitCard.getReturnToHabitat());

        contentValues.put(PitCard.COLUMN_NAME_NOTES, pitCard.getNotes());

        contentValues.put(PitCard.COLUMN_NAME_COMPLETED_BY, pitCard.getCompletedBy());
        contentValues.put(PitCard.COLUMN_NAME_IS_DRAFT, pitCard.isDraft() ? "1" : "0");

        //pitCard already exists in DB, update
        if (pitCard.getId() > 0)
        {
            //create the where statement
            String whereStatement = ScoutCard.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {pitCard.getId() + ""};

            //update
            if(db.update(PitCard.TABLE_NAME, contentValues, whereStatement, whereArgs) == 1)
                return pitCard.getId();
            else
                return -1;
        }
        //insert new scoutCard in db
        else return db.insert(PitCard.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific scoutCard from the database
     *
     * @param pitCard with specified ID
     * @return successful delete
     */
    public boolean deletePitCard(PitCard pitCard)
    {
        if (pitCard.getId() > 0)
        {
            //create the where statement
            String whereStatement = PitCard.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {pitCard.getId() + ""};

            //delete
            return db.delete(PitCard.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion
    
    //region User Logic

    /**
     * Gets all objects in the database
     * @return all objects inside database
     */
    public ArrayList<User> getUsers()
    {
        ArrayList<User> users = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns =
                {
                        User.COLUMN_NAME_ID,
                        User.COLUMN_NAME_FIRST_NAME,
                        User.COLUMN_NAME_LAST_NAME
                };

        //select the info from the db
        Cursor cursor = db.query(
                User.TABLE_NAME,
                columns,
                null,
                null,
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
     * Gets a specific object from the database and returns it
     *
     * @param user with specified ID
     * @return user based off given ID
     */
    public User getUser(User user)
    {
        //insert columns you are going to use here
        String[] columns =
                {
                        User.COLUMN_NAME_FIRST_NAME,
                        User.COLUMN_NAME_LAST_NAME
                };

        //where statement
        String whereStatement = User.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {user.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                User.TABLE_NAME,
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

            String firstName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME_FIRST_NAME));
            String lastName = cursor.getString(cursor.getColumnIndex(User.COLUMN_NAME_LAST_NAME));

            cursor.close();

            return new User(
                    user.getId(),
                    firstName,
                    lastName);
        }


        return null;
    }

    /**
     * Saves a specific scoutCard from the database and returns it
     *
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
            String whereArgs[] = {user.getId() + ""};

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
            String whereArgs[] = {user.getId() + ""};

            //delete
            return db.delete(User.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Robot Media Logic

    /**
     * Returns all columns inside the robot media table in string array format
     * @return string array of all columns
     */
    private String[] getRobotMediaColumns()
    {
        return new String[]
                {
                        RobotMedia.COLUMN_NAME_ID,
                        RobotMedia.COLUMN_NAME_TEAM_ID,
                        RobotMedia.COLUMN_NAME_FILE_URI,
                        RobotMedia.COLUMN_NAME_IS_DRAFT
                };
    }


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
     * @param team with specified ID
     * @return robotMedia based off given team ID
     */
    public ArrayList<RobotMedia> getRobotMedia(Team team, boolean onlyDrafts)
    {
        ArrayList<RobotMedia> robotMedia = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getRobotMediaColumns();

        //where statement
        String whereStatement = RobotMedia.COLUMN_NAME_TEAM_ID + " = ?" + ((onlyDrafts) ? " AND " + RobotMedia.COLUMN_NAME_IS_DRAFT + " = 1" : "");
        String[] whereArgs = {team.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                RobotMedia.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
                null,
                null,
                null);

        //make sure the cursor isn't null, else we die
        if (cursor != null)
        {
            while(cursor.moveToNext())
            {
                robotMedia.add(getRobotMediaFromCursor(cursor));
            }

            cursor.close();

            return robotMedia;
        }

        return null;
    }

    /**
     * Gets a specific object from the database and returns it
     *
     * @param robotMedia with specified ID
     * @return robotMedia based off given ID
     */
    public RobotMedia getRobotMedia(RobotMedia robotMedia)
    {
        //insert columns you are going to use here
        String[] columns = getRobotMediaColumns();

        //where statement
        String whereStatement = RobotMedia.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {robotMedia.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                RobotMedia.TABLE_NAME,
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

            RobotMedia databaseRobotMedia = getRobotMediaFromCursor(cursor);
            cursor.close();

            return databaseRobotMedia;
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
            String whereArgs[] = {robotMedia.getId() + ""};

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
            String whereArgs[] = {robotMedia.getId() + ""};

            //delete
            return db.delete(RobotMedia.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion

    //region Event Team List Logic

    /**
     * Returns all columns inside the event team list table in string array format
     * @return string array of all columns
     */
    private String[] getEventTeamListColumns()
    {
        return new String[]
                {
                        EventTeamList.COLUMN_NAME_ID,
                        EventTeamList.COLUMN_NAME_TEAM_ID,
                        EventTeamList.COLUMN_NAME_EVENT_ID
                };
    }


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
     * @param event with specified ID
     * @return object based off given team ID
     */
    public ArrayList<EventTeamList> getEventTeamLists(Event event)
    {
        ArrayList<EventTeamList> eventTeamLists = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns = getEventTeamListColumns();

        //where statement
        String whereStatement = EventTeamList.COLUMN_NAME_EVENT_ID + " = ?";
        String[] whereArgs = {event.getBlueAllianceId() + ""};
        String orderBy = EventTeamList.COLUMN_NAME_TEAM_ID + " DESC";

        //select the info from the db
        Cursor cursor = db.query(
                EventTeamList.TABLE_NAME,
                columns,
                whereStatement,
                whereArgs,
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
     * Gets a specific object from the database and returns it
     * @param eventTeamList with specified ID
     * @return eventTeamList based off given ID
     */
    public EventTeamList getEventTeamList(EventTeamList eventTeamList)
    {
        //insert columns you are going to use here
        String[] columns = getEventTeamListColumns();

        //where statement
        String whereStatement = EventTeamList.COLUMN_NAME_ID + " = ?";
        String[] whereArgs = {eventTeamList.getId() + ""};

        //select the info from the db
        Cursor cursor = db.query(
                EventTeamList.TABLE_NAME,
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

            EventTeamList databaseEventTeamList = getEventTeamListFromCursor(cursor);
            cursor.close();

            return databaseEventTeamList;
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
            String whereArgs[] = {eventTeamList.getId() + ""};

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
            String whereArgs[] = {eventTeamList.getId() + ""};

            //delete
            return db.delete(EventTeamList.TABLE_NAME, whereStatement, whereArgs) >= 1;
        }

        return false;
    }
    //endregion
}

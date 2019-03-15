package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.alphadevelopmentsolutions.frcscout.R;

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

    public void clearPitCards(boolean clearDrafts)
    {
        ArrayList<String> tableNames = new ArrayList<>();

        tableNames.add(PitCard.TABLE_NAME);

        for(String tableName : tableNames)
            db.execSQL("DELETE FROM " + tableName + ((clearDrafts) ? "" : " WHERE IsDraft = 0"));
    }

    //region Event Logic

    /**
     * Gets all events in the database
     *
     * @return all events inside database
     */
    public ArrayList<Event> getEvents()
    {
        ArrayList<Event> events = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns =
                {
                        Event.COLUMN_NAME_ID,
                        Event.COLUMN_NAME_BLUE_ALLIANCE_ID,
                        Event.COLUMN_NAME_NAME,
                        Event.COLUMN_NAME_CITY,
                        Event.COLUMN_NAME_STATEPROVINCE,
                        Event.COLUMN_NAME_COUNTRY
                };

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

                int id = cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_ID));
                String blueAllianceId = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_BLUE_ALLIANCE_ID));
                String name = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_NAME));
                String city = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_CITY));
                String stateProvince = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_STATEPROVINCE));
                String country = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_COUNTRY));

                events.add(new Event(id, blueAllianceId, name, city, stateProvince, country, new Date(), new Date()));
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
        String[] columns =
                {
                        Event.COLUMN_NAME_BLUE_ALLIANCE_ID,
                        Event.COLUMN_NAME_NAME,
                        Event.COLUMN_NAME_CITY,
                        Event.COLUMN_NAME_STATEPROVINCE,
                        Event.COLUMN_NAME_COUNTRY,
                        Event.COLUMN_NAME_START_DATE,
                        Event.COLUMN_NAME_END_DATE
                };

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

            String blueAllianceId = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_BLUE_ALLIANCE_ID));
            String name = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_NAME));
            String city = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_CITY));
            String stateProvince = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_STATEPROVINCE));
            String country = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_COUNTRY));
            Date startDate = new Date(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_START_DATE)));
            Date endDate = new Date(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_END_DATE)));

            cursor.close();

            return new Event(event.getId(), blueAllianceId, name, city, stateProvince, country, startDate, endDate);
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
     * Gets all teams in the database
     *
     * @return all teams inside database
     */
    public ArrayList<Team> getTeams()
    {
        ArrayList<Team> teams = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns =
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

                teams.add(new Team(id, name, city, stateProvince, country, rookieYear, facebookURL, twitterURL, instagramURL, youtubeURL, websiteURL, imageFileURI));
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
        String[] columns =
                {
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

                cursor.close();

                return new Team(team.getId(), name, city, stateProvince, country, rookieYear, facebookURL, twitterURL, instagramURL, youtubeURL, websiteURL, imageFileURI);
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
     * Gets a specific match from the database and returns it
     *
     * @param match with specified ID
     * @return match based off given ID
     */
    public Match getMatch(Match match)
    {
        //insert columns you are going to use here
        String[] columns =
                {
                        Match.COLUMN_NAME_DATE,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID,
                        Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE,
                        Match.COLUMN_NAME_RED_ALLIANCE_SCORE,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID,
                        Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID
                };

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

            Date date = new Date(cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_DATE)));
            int blueAllianceTeamOneId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID));
            int blueAllianceTeamTwoId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID));
            int blueAllianceTeamThreeId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID));
            int blueAllianceTeamOneScoutCardId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID));
            int blueAllianceTeamTwoScoutCardId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID));
            int blueAllianceTeamThreeScoutCardId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID));
            int score = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE));
            int opponentScore = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_SCORE));
            int redAllianceTeamOne = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID));
            int redAllianceTeamTwo = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID));
            int redAllianceTeamThree = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID));
            int redAllianceTeamOneScoutCardId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID));
            int redAllianceTeamTwoScoutCardId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID));
            int redAllianceTeamThreeScoutCardId = cursor.getInt(cursor.getColumnIndex(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID));

            cursor.close();

            return new Match(match.getId(), date, blueAllianceTeamOneId, blueAllianceTeamTwoId, blueAllianceTeamThreeId, blueAllianceTeamOneScoutCardId, blueAllianceTeamTwoScoutCardId, blueAllianceTeamThreeScoutCardId, score, opponentScore, redAllianceTeamOne, redAllianceTeamTwo, redAllianceTeamThree, redAllianceTeamOneScoutCardId, redAllianceTeamTwoScoutCardId, redAllianceTeamThreeScoutCardId);
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
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID, match.getBlueAllianceTeamOneId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID, match.getBlueAllianceTeamTwoId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID, match.getBlueAllianceTeamThreeId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID, match.getBlueAllianceTeamOneScoutCardId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID, match.getBlueAllianceTeamTwoScoutCardId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID, match.getBlueAllianceTeamThreeScoutCardId());
        contentValues.put(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE, match.getBlueAllianceScore());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_SCORE, match.getRedAllianceScore());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID, match.getRedAllianceTeamOneId());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID, match.getRedAllianceTeamTwoId());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID, match.getRedAllianceTeamThreeId());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID, match.getRedAllianceTeamOneScoutCardId());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID, match.getRedAllianceTeamTwoScoutCardId());
        contentValues.put(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID, match.getRedAllianceTeamThreeScoutCardId());

        //Match already exists in DB, update
        if (match.getId() > 0)
        {
            //create the where statement
            String whereStatement = Match.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {match.getId() + ""};

            //update
            return db.update(Match.TABLE_NAME, contentValues, whereStatement, whereArgs);
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
     * Gets all scout cards assigned to a team
     *
     * @param team with specified ID
     * @return scoutcard based off given team ID
     */
    public ArrayList<ScoutCard> getScoutCards(Team team, boolean onlyDrafts)
    {
        ArrayList<ScoutCard> scoutCards = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns =
                {
                        ScoutCard.COLUMN_NAME_ID,
                        ScoutCard.COLUMNS_NAME_MATCH_ID,
                        ScoutCard.COLUMN_NAME_TEAM_ID,
                        ScoutCard.COLUMN_NAME_EVENT_ID,
                        ScoutCard.COLUMN_NAME_ALLIANCE_COLOR,
                        ScoutCard.COLUMN_NAME_COMPLETED_BY,
                        ScoutCard.COLUMN_NAME_BLUE_ALLIANCE_FINAL_SCORE,
                        ScoutCard.COLUMN_NAME_RED_ALLIANCE_FINAL_SCORE,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED,
                        ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED,
                        ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETED,
                        ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT,
                        ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_NOTES,
                        ScoutCard.COLUMN_NAME_COMPLETED_DATE,
                        ScoutCard.COLUMN_NAME_IS_DRAFT
                };

        //where statement
        String whereStatement = ScoutCard.COLUMN_NAME_TEAM_ID + " = ?" + ((onlyDrafts) ? " AND " + ScoutCard.COLUMN_NAME_IS_DRAFT + " = 1" : "");
        String[] whereArgs = {team.getId() + ""};

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
            while(cursor.moveToNext())
            {

                int id = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_ID));
                int matchId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMNS_NAME_MATCH_ID));
                int teamId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TEAM_ID));
                String eventId = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_EVENT_ID));
                AllianceColor teamColor = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR)).equals(AllianceColor.RED.name()) ?  AllianceColor.RED : AllianceColor.BLUE;
                String completedBy = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_BY));
                int blueAllianceFinalScore = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_BLUE_ALLIANCE_FINAL_SCORE));
                int redAllianceFinalScore = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_RED_ALLIANCE_FINAL_SCORE));
                String autonomousExitHabitat = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT));
                int autonomousHatchPanelsSecured = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED));
                int autonomousHatchPanelsSecuredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS));
                int autonomousCargoStored = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED));
                int autonomousCargoStoredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS));
                int teleopHatchPanelsSecured = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED));
                int teleopHatchPanelsSecuredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS));
                int teleopCargoStored = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED));
                int teleopCargoStoredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS));
                int teleopRocketsCompleted = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETED));
                String endGameReturnedToHabitat = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT));
                String endGameReturnedToHabitatAttempts = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS));
                String notes = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_NOTES));
                Date completedDate = new Date(cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_DATE)));
                boolean isDraft = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_IS_DRAFT)) == 1;

                scoutCards.add(new ScoutCard(
                        id,
                        matchId,
                        teamId,
                        eventId,
                        teamColor,
                        completedBy,
                        blueAllianceFinalScore,
                        redAllianceFinalScore,
                        autonomousExitHabitat,
                        autonomousHatchPanelsSecured,
                        autonomousHatchPanelsSecuredAttempts,
                        autonomousCargoStored,
                        autonomousCargoStoredAttempts,
                        teleopHatchPanelsSecured,
                        teleopHatchPanelsSecuredAttempts,
                        teleopCargoStored,
                        teleopCargoStoredAttempts,
                        teleopRocketsCompleted,
                        endGameReturnedToHabitat,
                        endGameReturnedToHabitatAttempts,
                        notes,
                        completedDate,
                        isDraft));
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
        String[] columns =
                {
                        ScoutCard.COLUMN_NAME_ID,
                        ScoutCard.COLUMNS_NAME_MATCH_ID,
                        ScoutCard.COLUMN_NAME_TEAM_ID,
                        ScoutCard.COLUMN_NAME_EVENT_ID,
                        ScoutCard.COLUMN_NAME_ALLIANCE_COLOR,
                        ScoutCard.COLUMN_NAME_COMPLETED_BY,
                        ScoutCard.COLUMN_NAME_BLUE_ALLIANCE_FINAL_SCORE,
                        ScoutCard.COLUMN_NAME_RED_ALLIANCE_FINAL_SCORE,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED,
                        ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED,
                        ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED,
                        ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETED,
                        ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT,
                        ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS,
                        ScoutCard.COLUMN_NAME_NOTES,
                        ScoutCard.COLUMN_NAME_COMPLETED_DATE,
                        ScoutCard.COLUMN_NAME_IS_DRAFT
                };

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

            int matchId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMNS_NAME_MATCH_ID));
            int teamId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TEAM_ID));
            String eventId = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_EVENT_ID));
            AllianceColor teamColor = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR)).equals(AllianceColor.RED.name()) ?  AllianceColor.RED : AllianceColor.BLUE;
            String completedBy = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_BY));
            int blueAllianceFinalScore = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_BLUE_ALLIANCE_FINAL_SCORE));
            int redAllianceFinalScore = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_RED_ALLIANCE_FINAL_SCORE));
            String autonomousExitHabitat = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT));
            int autonomousHatchPanelsSecured = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED));
            int autonomousHatchPanelsSecuredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS));
            int autonomousCargoStored = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED));
            int autonomousCargoStoredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS));
            int teleopHatchPanelsSecured = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED));
            int teleopHatchPanelsSecuredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS));
            int teleopCargoStored = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED));
            int teleopCargoStoredAttempts = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS));
            int teleopRocketsCompleted = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETED));
            String endGameReturnedToHabitat = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT));
            String endGameReturnedToHabitatAttempts = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS));
            String notes = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_NOTES));
            Date completedDate = new Date(cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_DATE)));
            boolean isDraft = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_IS_DRAFT)) == 1;
            cursor.close();

            return new ScoutCard(
                    scoutCard.getId(),
                    matchId,
                    teamId,
                    eventId,
                    teamColor,
                    completedBy,
                    blueAllianceFinalScore,
                    redAllianceFinalScore,
                    autonomousExitHabitat,
                    autonomousHatchPanelsSecured,
                    autonomousHatchPanelsSecuredAttempts,
                    autonomousCargoStored,
                    autonomousCargoStoredAttempts,
                    teleopHatchPanelsSecured,
                    teleopHatchPanelsSecuredAttempts,
                    teleopCargoStored,
                    teleopCargoStoredAttempts,
                    teleopRocketsCompleted,
                    endGameReturnedToHabitat,
                    endGameReturnedToHabitatAttempts,
                    notes,
                    completedDate,
                    isDraft);
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
        contentValues.put(ScoutCard.COLUMNS_NAME_MATCH_ID, scoutCard.getMatchId());
        contentValues.put(ScoutCard.COLUMN_NAME_TEAM_ID, scoutCard.getTeamId());
        contentValues.put(ScoutCard.COLUMN_NAME_EVENT_ID, scoutCard.getEventId());
        contentValues.put(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR, scoutCard.getAllianceColor().name());
        contentValues.put(ScoutCard.COLUMN_NAME_BLUE_ALLIANCE_FINAL_SCORE, scoutCard.getBlueAllianceFinalScore());
        contentValues.put(ScoutCard.COLUMN_NAME_RED_ALLIANCE_FINAL_SCORE, scoutCard.getRedAllianceFinalScore());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT, scoutCard.isAutonomousExitHabitat());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED, scoutCard.getAutonomousHatchPanelsSecured());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS, scoutCard.getAutonomousHatchPanelsSecuredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED, scoutCard.getAutonomousCargoStored());
        contentValues.put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS, scoutCard.getAutonomousCargoStoredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED, scoutCard.getTeleopHatchPanelsSecured());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS, scoutCard.getTeleopHatchPanelsSecuredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED, scoutCard.getTeleopCargoStored());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS, scoutCard.getTeleopCargoStoredAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETED, scoutCard.getTeleopRocketsCompleted());
        contentValues.put(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT, scoutCard.getEndGameReturnedToHabitat());
        contentValues.put(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS, scoutCard.getEndGameReturnedToHabitatAttempts());
        contentValues.put(ScoutCard.COLUMN_NAME_NOTES, scoutCard.getNotes());
        contentValues.put(ScoutCard.COLUMN_NAME_COMPLETED_BY, scoutCard.getCompletedBy());
        contentValues.put(ScoutCard.COLUMN_NAME_COMPLETED_DATE, scoutCard.getCompletedDate().getTime());
        contentValues.put(ScoutCard.COLUMN_NAME_IS_DRAFT, scoutCard.isDraft() ? "1" : "0");

        //scoutCard already exists in DB, update
        if (scoutCard.getId() > 0)
        {
            //create the where statement
            String whereStatement = ScoutCard.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {scoutCard.getId() + ""};

            //update
            return db.update(ScoutCard.TABLE_NAME, contentValues, whereStatement, whereArgs);
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
     * Gets all pit cards assigned to a team
     *
     * @param team with specified ID
     * @return object based off given team ID
     */
    public ArrayList<PitCard> getPitCards(Team team, boolean onlyDrafts)
    {
        ArrayList<PitCard> pitCards = new ArrayList<>();

        //insert columns you are going to use here
        String[] columns =
                {
                        PitCard.COLUMN_NAME_ID,
                        PitCard.COLUMN_NAME_TEAM_ID,
                        PitCard.COLUMN_NAME_EVENT_ID,
                        PitCard.COLUMN_NAME_DRIVE_STYLE,
                        PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT,
                        PitCard.COLUMN_NAME_AUTO_HATCH,
                        PitCard.COLUMN_NAME_AUTO_CARGO,
                        PitCard.COLUMN_NAME_TELEOP_HATCH,
                        PitCard.COLUMN_NAME_TELEOP_CARGO,
                        PitCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETE,
                        PitCard.COLUMN_NAME_RETURN_TO_HABITAT,
                        PitCard.COLUMN_NAME_NOTES,
                        PitCard.COLUMN_NAME_COMPLETED_BY,
                        PitCard.COLUMN_NAME_IS_DRAFT
                };

        //where statement
        String whereStatement = PitCard.COLUMN_NAME_TEAM_ID + " = ? " + ((onlyDrafts) ? " AND " + PitCard.COLUMN_NAME_IS_DRAFT + " = 1" : "");
        String[] whereArgs = {team.getId() + ""};

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
            while(cursor.moveToNext())
            {

                int id = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_ID));
                int teamId = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_TEAM_ID));
                String eventId = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_EVENT_ID));
                String driveStyle = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_DRIVE_STYLE));
                String autoExitHabitat = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT));
                String autoHatch = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_HATCH));
                String autoCargo = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_CARGO));
                String teleopHatch = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_HATCH));
                String teleopCargo = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_CARGO));
                String teleopRocketsCompleted = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETE));
                String returnToHabitat = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_RETURN_TO_HABITAT));
                String notes = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_NOTES));
                String completedBy = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_COMPLETED_BY));
                boolean isDraft = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_IS_DRAFT)) == 1;


                pitCards.add(new PitCard(
                        id,
                        teamId,
                        eventId,
                        driveStyle,
                        autoExitHabitat,
                        autoHatch,
                        autoCargo,
                        teleopHatch,
                        teleopCargo,
                        teleopRocketsCompleted,
                        returnToHabitat,
                        notes,
                        completedBy,
                        isDraft));
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
        String[] columns =
                {
                        PitCard.COLUMN_NAME_ID,
                        PitCard.COLUMN_NAME_TEAM_ID,
                        PitCard.COLUMN_NAME_EVENT_ID,
                        PitCard.COLUMN_NAME_DRIVE_STYLE,
                        PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT,
                        PitCard.COLUMN_NAME_AUTO_HATCH,
                        PitCard.COLUMN_NAME_AUTO_CARGO,
                        PitCard.COLUMN_NAME_TELEOP_HATCH,
                        PitCard.COLUMN_NAME_TELEOP_CARGO,
                        PitCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETE,
                        PitCard.COLUMN_NAME_RETURN_TO_HABITAT,
                        PitCard.COLUMN_NAME_NOTES,
                        PitCard.COLUMN_NAME_COMPLETED_BY,
                        PitCard.COLUMN_NAME_IS_DRAFT
                };

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

            int id = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_ID));
            int teamId = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_TEAM_ID));
            String eventId = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_EVENT_ID));
            String driveStyle = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_DRIVE_STYLE));
            String autoExitHabitat = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT));
            String autoHatch = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_HATCH));
            String autoCargo = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_AUTO_CARGO));
            String teleopHatch = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_HATCH));
            String teleopCargo = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_CARGO));
            String teleopRocketsCompleted = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETE));
            String returnToHabitat = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_RETURN_TO_HABITAT));
            String notes = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_NOTES));
            String completedBy = cursor.getString(cursor.getColumnIndex(PitCard.COLUMN_NAME_COMPLETED_BY));
            boolean isDraft = cursor.getInt(cursor.getColumnIndex(PitCard.COLUMN_NAME_IS_DRAFT)) == 1;


            return new PitCard(
                    id,
                    teamId,
                    eventId,
                    driveStyle,
                    autoExitHabitat,
                    autoHatch,
                    autoCargo,
                    teleopHatch,
                    teleopCargo,
                    teleopRocketsCompleted,
                    returnToHabitat,
                    notes,
                    completedBy,
                    isDraft);
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
        contentValues.put(PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT, pitCard.getAutoExitHabitat());
        contentValues.put(PitCard.COLUMN_NAME_AUTO_HATCH, pitCard.getAutoHatch());
        contentValues.put(PitCard.COLUMN_NAME_AUTO_CARGO, pitCard.getAutoCargo());
        contentValues.put(PitCard.COLUMN_NAME_TELEOP_HATCH, pitCard.getTeleopHatch());
        contentValues.put(PitCard.COLUMN_NAME_TELEOP_CARGO, pitCard.getTeleopCargo());
        contentValues.put(PitCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETE, pitCard.getTeleopRocketsComplete());
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
            return db.update(PitCard.TABLE_NAME, contentValues, whereStatement, whereArgs);
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
            return db.update(User.TABLE_NAME, contentValues, whereStatement, whereArgs);
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
}

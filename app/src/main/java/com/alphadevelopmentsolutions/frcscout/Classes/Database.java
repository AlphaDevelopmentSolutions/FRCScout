package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.Date;

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
     * @return boolean if database was opened successfully
     */
    public boolean open()
    {
        try
        {
            db = databaseHelper.getWritableDatabase();
            return true;
        }
        catch(SQLException e)
        {
            return false;
        }

    }

    /**
     * Checks if the database is currently open
     * @return boolean if database is open
     */
    public boolean isOpen()
    {
        return db == null;

    }

    /**
     * Closes the database after usage
     * @return boolean if database was closed successfully
     */
    public boolean close()
    {
        try
        {
            databaseHelper.close();
            return true;
        }
        catch(SQLException e)
        {
            return false;
        }

    }

    //region Event Logic
    /**
     * Gets a specific event from the database and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public Event getEvent(Event event)
    {
        //insert columns you are going to use here
        String[] columns =
        {
            Event.COLUMN_NAME_NAME,
            Event.COLUMN_NAME_CITY,
            Event.COLUMN_NAME_STATEPROVINCE,
            Event.COLUMN_NAME_COUNTRY,
            Event.COLUMN_NAME_STARTDATE,
            Event.COLUMN_NAME_ENDDATE
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
        if(cursor != null)
        {
            //move to the first result in the set
            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_NAME));
            String city = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_CITY));
            String stateProvince = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_STATEPROVINCE));
            String country = cursor.getString(cursor.getColumnIndex(Event.COLUMN_NAME_COUNTRY));
            Date startDate = new Date(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_STARTDATE)));
            Date endDate = new Date(cursor.getInt(cursor.getColumnIndex(Event.COLUMN_NAME_ENDDATE)));

            cursor.close();

            return new Event(event.getId(), name, city, stateProvince, country, startDate, endDate);
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
        contentValues.put(Event.COLUMN_NAME_NAME, event.getName());
        contentValues.put(Event.COLUMN_NAME_CITY, event.getCity());
        contentValues.put(Event.COLUMN_NAME_STATEPROVINCE, event.getStateProvince());
        contentValues.put(Event.COLUMN_NAME_COUNTRY, event.getCountry());
        contentValues.put(Event.COLUMN_NAME_STARTDATE, event.getStartDate().getTime());
        contentValues.put(Event.COLUMN_NAME_ENDDATE, event.getEndDate().getTime());

        //event already exists in DB, update
        if(event.getId() > 0)
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
     * @param event with specified ID
     * @return successful delete
     */
    public boolean deleteEvent(Event event)
    {
        if(event.getId() > 0)
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
     * Gets a specific team from the database and returns it
     * @param team with specified ID
     * @return team based off given ID
     */
    public Team getTeam(Team team)
    {
        //insert columns you are going to use here
        String[] columns =
                {
                        Team.COLUMN_NAME_NAME,
                        Team.COLUMN_NAME_NUMBER,
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
        if(cursor != null)
        {
            //move to the first result in the set
            cursor.moveToFirst();

            String name = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_NAME));
            int number = cursor.getInt(cursor.getColumnIndex(Team.COLUMN_NAME_NUMBER));
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

            return new Team(team.getId(), name, number, city, stateProvince, country, rookieYear, facebookURL, twitterURL, instagramURL, youtubeURL, websiteURL, imageFileURI);
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
        contentValues.put(Team.COLUMN_NAME_NUMBER, team.getCity());
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
        if(team.getId() > 0)
        {
            //create the where statement
            String whereStatement = Team.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {team.getId() + ""};

            //update
            return db.update(Team.TABLE_NAME, contentValues, whereStatement, whereArgs);
        }
        //insert new team in db
        else return db.insert(Team.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific team from the database
     * @param team with specified ID
     * @return successful delete
     */
    public boolean deleteTeam(Team team)
    {
        if(team.getId() > 0)
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
        if(cursor != null)
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
        if(robot.getId() > 0)
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
     * @param robot with specified ID
     * @return successful delete
     */
    public boolean deleteRobot(Robot robot)
    {
        if(robot.getId() > 0)
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

    //region Scout Card Logic
    /**
     * Gets a specific scoutCard from the database and returns it
     * @param scoutCard with specified ID
     * @return scoutCard based off given ID
     */
    public ScoutCard getScoutCard(ScoutCard scoutCard)
    {
        //insert columns you are going to use here
        String[] columns =
                {
                        ScoutCard.COLUMN_NAME_TEAM_ID,
                        ScoutCard.COLUMN_NAME_COMPLETED_BY,
                        ScoutCard.COLUMN_NAME_COMPLETED_DATE,
                        ScoutCard.COLUMN_NAME_PARTNER_ONE_ID,
                        ScoutCard.COLUMN_NAME_PARTNER_TWO_ID,
                        ScoutCard.COLUMN_NAME_ALLIANCE_COLOR,
                        ScoutCard.COLUMN_NAME_SCORE,
                        ScoutCard.COLUMN_NAME_OPPONENT_SCORE,
                        ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_ONE,
                        ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_TWO,
                        ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_THREE
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
        if(cursor != null)
        {
            //move to the first result in the set
            cursor.moveToFirst();

            int teamId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_TEAM_ID));
            String completedBy = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_BY));
            Date completedDate = new Date(cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_COMPLETED_DATE)));
            int partnerOneId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_PARTNER_ONE_ID));
            int partnerTwoId = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_PARTNER_TWO_ID));
            AllianceColor allianceColor = cursor.getString(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR)) == AllianceColor.BLUE.name() ? AllianceColor.BLUE : AllianceColor.RED;
            int score = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_SCORE));
            int opponentScore = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_OPPONENT_SCORE));
            int opponentAlliancePartnerOne = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_ONE));
            int opponentAlliancePartnerTwo = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_TWO));
            int opponentAlliancePartnerThree = cursor.getInt(cursor.getColumnIndex(ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_THREE));

            cursor.close();

            return new ScoutCard(scoutCard.getId(), completedBy, completedDate, teamId, partnerOneId, partnerTwoId, allianceColor, score, opponentScore, opponentAlliancePartnerOne, opponentAlliancePartnerTwo, opponentAlliancePartnerThree);
        }


        return null;
    }

    /**
     * Saves a specific scoutCard from the database and returns it
     * @param scoutCard with specified ID
     * @return id of the saved scoutCard
     */
    public long setScoutCard(ScoutCard scoutCard)
    {
        //set all the values
        ContentValues contentValues = new ContentValues();
        contentValues.put(ScoutCard.COLUMN_NAME_TEAM_ID, scoutCard.getTeamId());
        contentValues.put(ScoutCard.COLUMN_NAME_COMPLETED_BY, scoutCard.getCompletedBy());
        contentValues.put(ScoutCard.COLUMN_NAME_COMPLETED_DATE, scoutCard.getCompletedDate().getTime());
        contentValues.put(ScoutCard.COLUMN_NAME_COMPLETED_DATE, scoutCard.getCompletedBy());
        contentValues.put(ScoutCard.COLUMN_NAME_PARTNER_ONE_ID, scoutCard.getPartnerOneId());
        contentValues.put(ScoutCard.COLUMN_NAME_PARTNER_TWO_ID, scoutCard.getPartnerTwoId());
        contentValues.put(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR, scoutCard.getAllianceColor().name());
        contentValues.put(ScoutCard.COLUMN_NAME_SCORE, scoutCard.getScore());
        contentValues.put(ScoutCard.COLUMN_NAME_OPPONENT_SCORE, scoutCard.getOpponentScore());
        contentValues.put(ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_ONE, scoutCard.getOpponentAlliancePartnerOne());
        contentValues.put(ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_TWO, scoutCard.getOpponentAlliancePartnerTwo());
        contentValues.put(ScoutCard.COLUMN_NAME_OPPONENT_ALLIANCE_PARTNER_THREE, scoutCard.getOpponentAlliancePartnerThree());

        //ScoutCard already exists in DB, update
        if(scoutCard.getId() > 0)
        {
            //create the where statement
            String whereStatement = ScoutCard.COLUMN_NAME_ID + " = ?";
            String whereArgs[] = {scoutCard.getId() + ""};

            //update
            return db.update(ScoutCard.TABLE_NAME, contentValues, whereStatement, whereArgs);
        }
        //insert new ScoutCard in db
        else return db.insert(ScoutCard.TABLE_NAME, null, contentValues);

    }

    /**
     * Deletes a specific scoutCard from the database
     * @param scoutCard with specified ID
     * @return successful delete
     */
    public boolean deleteScoutCard(ScoutCard scoutCard)
    {
        if(scoutCard.getId() > 0)
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
}

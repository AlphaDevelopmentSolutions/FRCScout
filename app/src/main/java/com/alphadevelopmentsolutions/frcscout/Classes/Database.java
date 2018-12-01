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

    Database(Context context)
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
                        Team.COLUMN_NAME_ROOKIEYEAR,
                        Team.COLUMN_NAME_WEBSITE
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
            int number = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_NUMBER)));
            String city = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_CITY));
            String stateProvince = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_STATEPROVINCE));
            String country = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_COUNTRY));
            int rookieYear = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_ROOKIEYEAR)));
            String website = cursor.getString(cursor.getColumnIndex(Team.COLUMN_NAME_WEBSITE));

            cursor.close();

            return new Team(team.getId(), name, number, city, stateProvince, country, rookieYear, website);
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
        contentValues.put(Team.COLUMN_NAME_ROOKIEYEAR, team.getRookieYear());
        contentValues.put(Team.COLUMN_NAME_WEBSITE, team.getWebsite());

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
                        Robot.COLUMN_NAME_TEAMNUMBER
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
            int teamNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Robot.COLUMN_NAME_TEAMNUMBER)));

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
        contentValues.put(Robot.COLUMN_NAME_TEAMNUMBER, robot.getTeamNumber());

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
}

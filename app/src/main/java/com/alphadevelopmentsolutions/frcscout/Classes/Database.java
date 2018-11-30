package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

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
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public Event getEvent(Event event)
    {
        return null;
    }

    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public boolean setEvent(Event event)
    {
        return false;
    }

    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public boolean deleteEvent(Event event)
    {
        return false;
    }
    //endregion

    //region Team Logic
    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public Team getTeam(Team team)
    {
        return null;
    }

    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public boolean setTeam(Team team)
    {
        return false;
    }

    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public boolean deleteTeam(Team team)
    {
        return false;
    }
    //endregion

    //region Robot Logic
    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public Robot getRobot(Robot robot)
    {
        return null;
    }

    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public boolean setRobot(Robot robot)
    {
        return false;
    }

    /**
     * Gets a specific event from the databse and returns it
     * @param event with specified ID
     * @return event based off given ID
     */
    public boolean deleteRobot(Robot robot)
    {
        return false;
    }
    //endregion
}

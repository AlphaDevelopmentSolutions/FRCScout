package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.util.ArrayList;

public class EventTeamList extends Table
{

    public static final String TABLE_NAME = "event_team_list";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_EVENT_ID = "EventId";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TEAM_ID + " INTEGER," +
                    COLUMN_NAME_EVENT_ID + " TEXT)";

    private int id;
    private int teamId;
    private String eventId;

    public EventTeamList(
            int id,
            int teamId,
            String eventId)
    {
        this.id = id;
        this.teamId = teamId;
        this.eventId = eventId;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public EventTeamList(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public int getTeamId()
    {
        return teamId;
    }

    public String getEventId()
    {
        return eventId;
    }

    @Override
    public String toString()
    {
        return "";
    }


    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }


    //endregion

    //region Load, Save & Delete

    /**
     * Loads the object from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean load(Database database)
    {
        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            ArrayList<EventTeamList> eventTeamLists = getEventTeamList(this, null, database);
            EventTeamList eventTeamList = (eventTeamLists.size() > 0 ) ? eventTeamLists.get(0) : null;

            if (eventTeamList != null)
            {
                setTeamId(eventTeamList.getTeamId());
                setEventId(eventTeamList.getEventId());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the object into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved object
     */
    public int save(Database database)
    {
        int id = -1;

        //try to open the DB if it is not open
        if(!database.isOpen())
            database.open();

        if(database.isOpen())
            id = (int) database.setEventTeamList(this);

        //set the id if the save was successful
        if(id > 0)
            setId(id);

        return getId();
    }

    /**
     * Deletes the object from the database
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean delete(Database database)
    {
        boolean successful = false;

        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            successful = database.deleteEventTeamList(this);

        }

        return successful;
    }

    /**
     * Clears all data from the classes table
     * @param database used to clear table
     */
    public static void clearTable(Database database)
    {
        database.clearTable(TABLE_NAME);
    }

    /**
     * Returns arraylist of event team list with specified filters from database
     * @param eventTeamList if specified, filters event team list by eventTeamList id
     * @param event if specified, filters event team list by event id
     * @param database used to load event team list
     * @return arraylist of event team list
     */
    public static ArrayList<EventTeamList> getEventTeamList(@Nullable EventTeamList eventTeamList, @Nullable Event event, @NonNull Database database)
    {
        return database.getEventTeamLists(eventTeamList, event);
    }

    //endregion
}

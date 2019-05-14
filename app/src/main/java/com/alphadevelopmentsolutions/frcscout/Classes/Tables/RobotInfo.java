package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.util.ArrayList;

public class RobotInfo extends Table
{

    public static final String TABLE_NAME = "robot_info";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_YEAR_ID = "YearId";
    public static final String COLUMN_NAME_EVENT_ID = "EventId";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";

    public static final String COLUMN_NAME_PROPERTY_STATE = "PropertyState";
    public static final String COLUMN_NAME_PROPERTY_KEY = "PropertyKey";
    public static final String COLUMN_NAME_PROPERTY_VALUE = "PropertyValue";

    public static final String COLUMN_NAME_IS_DRAFT = "IsDraft";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_YEAR_ID + " INTEGER," +
                    COLUMN_NAME_EVENT_ID + " TEXT," +
                    COLUMN_NAME_TEAM_ID + " INTEGER," +

                    COLUMN_NAME_PROPERTY_STATE + " TEXT," +
                    COLUMN_NAME_PROPERTY_KEY + " TEXT," +
                    COLUMN_NAME_PROPERTY_VALUE + " TEXT," +

                    COLUMN_NAME_IS_DRAFT + " INTEGER)";

    private int id;
    private int yearId;
    private String eventId;
    private int teamId;

    private String propertyState;
    private String propertyKey;
    private String propertyValue;

    private boolean isDraft;

    public RobotInfo(
            int id,
            int yearId,
            String eventId,
            int teamId,

            String propertyState,
            String propertyKey,
            String propertyValue,

            boolean isDraft)
    {
        this.id = id;
        this.yearId = yearId;
        this.eventId = eventId;
        this.teamId = teamId;

        this.propertyState = propertyState;
        this.propertyKey = propertyKey;
        this.propertyValue = propertyValue;

        this.isDraft = isDraft;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public RobotInfo(int id)
    {
        this.id = id;
    }

    //region Getters


    public int getId()
    {
        return id;
    }

    public int getYearId()
    {
        return yearId;
    }

    public String getEventId()
    {
        return eventId;
    }

    public int getTeamId()
    {
        return teamId;
    }

    public String getPropertyState()
    {
        return propertyState;
    }

    public String getPropertyKey()
    {
        return propertyKey;
    }

    public String getPropertyValue()
    {
        return propertyValue;
    }

    public boolean isDraft()
    {
        return isDraft;
    }

    @Override
    public String toString()
    {
        return "Team " + getTeamId() + " - Robot Info";
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setYearId(int yearId)
    {
        this.yearId = yearId;
    }

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
    }

    public void setPropertyState(String propertyState)
    {
        this.propertyState = propertyState;
    }

    public void setPropertyKey(String propertyKey)
    {
        this.propertyKey = propertyKey;
    }

    public void setPropertyValue(String propertyValue)
    {
        this.propertyValue = propertyValue;
    }

    public void setDraft(boolean draft)
    {
        isDraft = draft;
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
            ArrayList<RobotInfo> robotInfoList = getRobotInfo(null, null, null, this, false, database);
            RobotInfo robotInfo = (robotInfoList.size() > 0 ) ? robotInfoList.get(0) : null;

            if (robotInfo != null)
            {
                setYearId(robotInfo.getYearId());
                setEventId(robotInfo.getEventId());
                setTeamId(robotInfo.getTeamId());

                setPropertyState(robotInfo.getPropertyState());
                setPropertyKey(robotInfo.getPropertyKey());
                setPropertyValue(robotInfo.getPropertyValue());

                setDraft(robotInfo.isDraft());
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
            id = (int) database.setRobotInfo(this);

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
            successful = database.deleteRobotInfo(this);

        }

        return successful;
    }

    /**
     * Clears all data from the classes table
     * @param database used to clear table
     * @param clearDrafts boolean if you want to include drafts in the clear
     */
    public static void clearTable(Database database, boolean clearDrafts)
    {
        database.clearTable(TABLE_NAME, clearDrafts);
    }

    /**
     * Returns arraylist of pit cards with specified filters from database
     * @param year if specified, filters by year id
     * @param event if specified, filters by event id
     * @param team if specified, filters by team id
     * @param robotInfo if specified, filters by robotinfo id
     * @param onlyDrafts if true, filters by draft
     * @param database used to load
     * @return arraylist of robotInfo
     */
    public static ArrayList<RobotInfo> getRobotInfo(@Nullable Year year, @Nullable Event event, @Nullable Team team, @Nullable RobotInfo robotInfo, boolean onlyDrafts, @NonNull Database database)
    {
        return database.getRobotInfo(year, event, team, robotInfo, onlyDrafts);
    }

    //endregion
}

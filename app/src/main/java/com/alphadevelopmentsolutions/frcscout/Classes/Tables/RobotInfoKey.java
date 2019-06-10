package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.util.ArrayList;

public class RobotInfoKey extends Table
{
    public static final String TABLE_NAME = "robot_info_keys";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_YEAR_ID = "YearId";
    public static final String COLUMN_NAME_SORT_ORDER = "SortOrder";
    public static final String COLUMN_NAME_KEY_STATE = "KeyState";
    public static final String COLUMN_NAME_KEY_NAME = "KeyName";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_YEAR_ID + " INTEGER," +
                    COLUMN_NAME_SORT_ORDER + " INTEGER," +
                    COLUMN_NAME_KEY_STATE + " TEXT," +
                    COLUMN_NAME_KEY_NAME + " TEXT)";

    private int id;
    private int yearId;
    private int sortOrder;
    private String keyState;
    private String keyName;

    public RobotInfoKey(
            int id,
            int yearId,
            String keyState,
            String keyName,
            int sortOrder)
    {
        this.id = id;
        this.yearId = yearId;
        this.sortOrder = sortOrder;
        this.keyState = keyState;
        this.keyName = keyName;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public RobotInfoKey(int id)
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

    public int getSortOrder()
    {
        return sortOrder;
    }

    public String getKeyState()
    {
        return keyState;
    }

    public String getKeyName()
    {
        return keyName;
    }

    @Override
    public String toString()
    {
        return null;
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

    public void setSortOrder(int sortOrder)
    {
        this.sortOrder = sortOrder;
    }

    public void setKeyState(String keyState)
    {
        this.keyState = keyState;
    }

    public void setKeyName(String keyName)
    {
        this.keyName = keyName;
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
            ArrayList<RobotInfoKey> robotInfoKeyList = getRobotInfoKeys(null,this, database);
            RobotInfoKey robotInfoKey = (robotInfoKeyList.size() > 0 ) ? robotInfoKeyList.get(0) : null;

            if (robotInfoKey != null)
            {
                setYearId(robotInfoKey.getYearId());
                setSortOrder(robotInfoKey.getSortOrder());
                setKeyState(robotInfoKey.getKeyState());
                setKeyName(robotInfoKey.getKeyName());
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
            id = (int) database.setRobotInfoKey(this);

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
            successful = database.deleteRobotInfoKey(this);

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
     * Returns arraylist of pit cards with specified filters from database
     * @param year if specified, filters by year id
     * @param robotInfoKey if specified, filters by robotInfoKey id
     * @param database used to load
     * @return arraylist of robotInfoKeys
     */
    public static ArrayList<RobotInfoKey> getRobotInfoKeys(@Nullable Year year, @Nullable RobotInfoKey robotInfoKey, @NonNull Database database)
    {
        return database.getRobotInfoKeys(year, robotInfoKey);
    }

    //endregion
}

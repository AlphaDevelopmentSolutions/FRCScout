package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.util.ArrayList;

public class ScoutCardInfoKey extends Table
{
    public static final String TABLE_NAME = "scout_card_info_keys";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_YEAR_ID = "YearId";
    public static final String COLUMN_NAME_KEY_STATE = "KeyState";
    public static final String COLUMN_NAME_KEY_NAME = "KeyName";
    public static final String COLUMN_NAME_SORT_ORDER = "SortOrder";
    public static final String COLUMN_NAME_MIN_VALUE = "MinValue";
    public static final String COLUMN_NAME_MAX_VALUE = "MaxValue";
    public static final String COLUMN_NAME_NULL_ZEROS = "NullZeros";
    public static final String COLUMN_NAME_INCLUDE_IN_STATS = "IncludeInStats";
    public static final String COLUMN_NAME_DATA_TYPE = "DataType";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_YEAR_ID + " INTEGER," +
                    COLUMN_NAME_KEY_STATE + " TEXT," +
                    COLUMN_NAME_KEY_NAME + " TEXT," +
                    COLUMN_NAME_SORT_ORDER + " INTEGER," +
                    COLUMN_NAME_MIN_VALUE + " INTEGER," +
                    COLUMN_NAME_MAX_VALUE + " INTEGER," +
                    COLUMN_NAME_NULL_ZEROS + " INTEGER," +
                    COLUMN_NAME_INCLUDE_IN_STATS + " INTEGER," +
                    COLUMN_NAME_DATA_TYPE + " TEXT)";

    //java does not allow nulls, create a unique val that will never be used
    //in a real life scenario. THIS VAL IS THE LOWEST AN INT CAN BE
    public static final int INT_NULL_VALUE = -2147483648;

    public enum DataTypes
    {
        BOOL,
        INT,
        TEXT;

        /**
         * Parses the string into a datatype format
         * @param type string of type to parse
         * @return datatype
         */
        public static DataTypes parseString(String type)
        {
            if(type.toUpperCase().equals(BOOL.name()))
                return BOOL;
            else if(type.toUpperCase().equals(INT.name()))
                return INT;
            else if(type.toUpperCase().equals(TEXT.name()))
                return TEXT;

            return BOOL;
        }
    }

    private int id;
    private int yearId;
    private String keyState;
    private String keyName;
    private int sortOrder;
    private int minValue;
    private int maxValue;
    private boolean nullZeros;
    private boolean includeInStats;
    private DataTypes dataType;

    public ScoutCardInfoKey(
            int id,
            int yearId,
            String keyState,
            String keyName,
            int sortOrder,
            int minValue,
            int maxValue,
            boolean nullZeros,
            boolean includeInStats,
            DataTypes dataType)
    {
        this.id = id;
        this.yearId = yearId;
        this.keyState = keyState;
        this.keyName = keyName;
        this.sortOrder = sortOrder;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.nullZeros = nullZeros;
        this.includeInStats = includeInStats;
        this.dataType = dataType;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public ScoutCardInfoKey(int id)
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

    public int getMinValue()
    {
        return minValue;
    }

    public int getMaxValue()
    {
        return maxValue;
    }

    public boolean isNullZeros()
    {
        return nullZeros;
    }

    public boolean isIncludeInStats()
    {
        return includeInStats;
    }

    public DataTypes getDataType()
    {
        return dataType;
    }

    @Override
    public String toString()
    {
        return getKeyState() + " " + getKeyName();
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

    public void setMinValue(int minValue)
    {
        this.minValue = minValue;
    }

    public void setMaxValue(int maxValue)
    {
        this.maxValue = maxValue;
    }

    public void setNullZeros(boolean nullZeros)
    {
        this.nullZeros = nullZeros;
    }

    public void setIncludeInStats(boolean includeInStats)
    {
        this.includeInStats = includeInStats;
    }

    public void setDataType(DataTypes dataType)
    {
        this.dataType = dataType;
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
            ArrayList<ScoutCardInfoKey> scoutCardInfoKeys = getScoutCardInfoKeys(null,this, database);
            ScoutCardInfoKey scoutCardInfoKey = (scoutCardInfoKeys.size() > 0 ) ? scoutCardInfoKeys.get(0) : null;

            if (scoutCardInfoKey != null)
            {
                setYearId(scoutCardInfoKey.getYearId());
                setSortOrder(scoutCardInfoKey.getSortOrder());
                setKeyState(scoutCardInfoKey.getKeyState());
                setKeyName(scoutCardInfoKey.getKeyName());
                setMinValue(scoutCardInfoKey.getMinValue());
                setMaxValue(scoutCardInfoKey.getMinValue());
                setNullZeros(scoutCardInfoKey.isNullZeros());
                setIncludeInStats(scoutCardInfoKey.isIncludeInStats());
                setDataType(scoutCardInfoKey.getDataType());
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
            id = (int) database.setScoutCardInfoKey(this);

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
            successful = database.deleteScoutCardInfoKey(this);

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
     * @param scoutCardInfoKey if specified, filters by scoutCardInfoKey id
     * @param database used to load
     * @return arraylist of robotInfoKeys
     */
    public static ArrayList<ScoutCardInfoKey> getScoutCardInfoKeys(@Nullable Year year, @Nullable ScoutCardInfoKey scoutCardInfoKey, @NonNull Database database)
    {
        return database.getScoutCardInfoKeys(year, scoutCardInfoKey);
    }

    //endregion
}

package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class Years extends Table
{

    public static final String TABLE_NAME = "years";
    public static final String COLUMN_NAME_ID = "LocalId";
    public static final String COLUMN_NAME_SERVER_ID = "Id";
    public static final String COLUMN_NAME_NAME = "Name";
    public static final String COLUMN_NAME_START_DATE = "StartDate";
    public static final String COLUMN_NAME_END_DATE = "EndDate";
    public static final String COLUMN_NAME_IMAGE_URI = "ImageUri";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_SERVER_ID + " INTEGER," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_START_DATE + " INTEGER," +
                    COLUMN_NAME_END_DATE + " INTEGER," +
                    COLUMN_NAME_IMAGE_URI + " TEXT)";

    private int id;
    private int serverId;
    private String name;
    private Date startDate;
    private Date endDate;
    private String imageUri;

    public Years(
            int id,
            int serverId,
            String name,
            Date startDate,
            Date endDate,
            String imageUri)
    {
        this.id = id;
        this.serverId = serverId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.imageUri = imageUri;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public Years(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public int getServerId()
    {
        return serverId;
    }

    public String getName()
    {
        return name;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public String getImageUri()
    {
        return imageUri;
    }

    /**
     * Gets a bitmap version of the object
     * @return bitmap version of object
     */
    public Bitmap getImageBitmap()
    {
        return BitmapFactory.decodeFile(this.getImageUri());
    }

    //endregion

    //region Setters


    public void setId(int id)
    {
        this.id = id;
    }

    public void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    public void setImageUri(String imageUri)
    {
        this.imageUri = imageUri;
    }

    @Override
    public String toString()
    {
        return serverId + " - " + name;
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
            ArrayList<Years> yearsArrayList = getYears(this, database);
            Years year = (yearsArrayList.size() > 0 ) ? yearsArrayList.get(0) : null;

            if (year != null)
            {
                setName(year.getName());
                setStartDate(year.getStartDate());
                setEndDate(year.getEndDate());
                setImageUri(year.getImageUri());
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
            id = (int) database.setYears(this);

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
            successful = database.deleteYears(this);

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
     * Returns arraylist of years with specified filters from database
     * @param year if specified, filters years by year id
     * @param database used to load robot media
     * @return ArrayList of years
     */
    public static ArrayList<Years> getYears(@Nullable Years year, @NonNull Database database)
    {
        return database.getYears(year);
    }

    //endregion
}

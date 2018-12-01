package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;

import java.util.Date;

public class Event
{

    public static final String TABLE_NAME = "events";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_NAME = "Name";
    public static final String COLUMN_NAME_CITY = "City";
    public static final String COLUMN_NAME_STATEPROVINCE = "StateProvince";
    public static final String COLUMN_NAME_COUNTRY = "Country";
    public static final String COLUMN_NAME_STARTDATE = "StartDate";
    public static final String COLUMN_NAME_ENDDATE = "EndDate";

    private int id;

    private String name;
    private String city;
    private String stateProvince;
    private String country;

    private Date startDate;
    private Date endDate;

    Event(
        int id,
        String name,
        String city,
        String stateProvince,
        String country,
        Date startDate,
        Date endDate)
    {
        this.id = id;
        this.name = name;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    Event(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public String getCity()
    {
        return city;
    }

    public String getStateProvince()
    {
        return stateProvince;
    }

    public String getCountry()
    {
        return country;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setStateProvince(String stateProvince)
    {
        this.stateProvince = stateProvince;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

    //endregion

    //region Load, Save & Delete

    /**
     * Loads the event from the database and populates all values
     * @param context used for opening the DB
     * @return boolean if successful
     */
    public boolean load(Context context)
    {
        Database database = new Database(context);
        if(database.open())
        {
            Event event = database.getEvent(this);
            database.close();

            if (event != null)
            {
                setName(event.getName());
                setCity(event.getCity());
                setStateProvince(event.getStateProvince());
                setCountry(event.getCountry());
                setStartDate(event.getStartDate());
                setEndDate(event.getEndDate());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the event into the database
     * @param context used for opening the DB
     * @return int id of the saved event
     */
    public int save(Context context)
    {
        int id = -1;
        Database database = new Database(context);

        if(database.open())
        {
            id = (int) database.setEvent(this);
            database.close();
        }

        return id;
    }

    /**
     * Deletes the event from the database
     * @param context used for opening the DB
     * @return boolean if successful
     */
    public boolean delete(Context context)
    {
        boolean successful = false;
        Database database = new Database(context);

        if(database.open())
        {
            successful = database.deleteEvent(this);
            database.close();
        }

        return successful;
    }

    //endregion
}

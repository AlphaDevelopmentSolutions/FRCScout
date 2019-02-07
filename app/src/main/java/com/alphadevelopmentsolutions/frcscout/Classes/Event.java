package com.alphadevelopmentsolutions.frcscout.Classes;

import java.util.Date;

public class Event
{

    public static final String TABLE_NAME = "events";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_NAME = "Name";
    public static final String COLUMN_NAME_CITY = "City";
    public static final String COLUMN_NAME_STATEPROVINCE = "StateProvince";
    public static final String COLUMN_NAME_COUNTRY = "Country";
    public static final String COLUMN_NAME_START_DATE = "StartDate";
    public static final String COLUMN_NAME_END_DATE = "EndDate";

    private int id;

    private String name;
    private String city;
    private String stateProvince;
    private String country;

    private Date startDate;
    private Date endDate;

    public Event(
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

    /**
     * Used for loading
     * @param id to load
     */
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
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean load(Database database)
    {
        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            Event event = database.getEvent(this);

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
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved event
     */
    public int save(Database database)
    {
        int id = -1;

        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            id = (int) database.setEvent(this);
        }

        return id;
    }

    /**
     * Deletes the event from the database
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
            successful = database.deleteEvent(this);
        }

        return successful;
    }

    //endregion
}

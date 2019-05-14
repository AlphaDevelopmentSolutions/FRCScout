package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.util.ArrayList;
import java.util.Date;

public class Event extends Table
{

    public static final String TABLE_NAME = "events";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_YEAR_ID = "YearId";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_ID = "BlueAllianceId";
    public static final String COLUMN_NAME_NAME = "Name";
    public static final String COLUMN_NAME_CITY = "City";
    public static final String COLUMN_NAME_STATEPROVINCE = "StateProvince";
    public static final String COLUMN_NAME_COUNTRY = "Country";
    public static final String COLUMN_NAME_START_DATE = "StartDate";
    public static final String COLUMN_NAME_END_DATE = "EndDate";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_YEAR_ID + " INTEGER," +
                    COLUMN_NAME_BLUE_ALLIANCE_ID + " TEXT," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_CITY + " TEXT," +
                    COLUMN_NAME_STATEPROVINCE + " TEXT," +
                    COLUMN_NAME_COUNTRY + " TEXT," +
                    COLUMN_NAME_START_DATE + " INTEGER," +
                    COLUMN_NAME_END_DATE + " INTEGER)";

    private int id;
    private int yearId;

    private String blueAllianceId;
    private String name;
    private String city;
    private String stateProvince;
    private String country;

    private Date startDate;
    private Date endDate;

    public Event(
            int id,
            int yearId,
            String blueAllianceId,
            String name,
            String city,
            String stateProvince,
            String country,
            Date startDate,
            Date endDate)
    {
        this.id = id;
        this.yearId = yearId;
        this.blueAllianceId = blueAllianceId;
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
    public Event(int id, Database database)
    {
        this.id = id;

        load(database);
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

    public String getBlueAllianceId()
    {
        return blueAllianceId;
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

    /**
     * Gets matches from a specific event
     * @param match if specified, filters matches by match id
     * @param team if specified, filters matches by team id
     * @param database used to get matches
     * @return arraylist of matches
     */
    public ArrayList<Match> getMatches(@Nullable Match match, @Nullable Team team, @NonNull Database database)
    {
        return Match.getMatches(this, match, team, database);
    }

    /**
     * Gets teams from a specific event
     * @param match if specified, filters matches by match id
     * @param team if specified, filters teams by team id
     * @param database used to get teams
     * @return arraylist of teams
     */
    public ArrayList<Team> getTeams(@Nullable Match match, @Nullable Team team, @NonNull Database database)
    {
        return Team.getTeams(this, match, team, database);
    }

    /**
     * Gets event team list from a specific event
     * @param eventTeamList if specified, filters event team list by eventteamlist id
     * @param database used to get event team list
     * @return arraylist of teams
     */
    public ArrayList<EventTeamList> getEventTeamList(@Nullable EventTeamList eventTeamList, Database database)
    {
        return EventTeamList.getEventTeamList(eventTeamList, this, database);
    }


    @Override
    public String toString()
    {
        return getName();
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

    public void setBlueAllianceId(String blueAllianceId)
    {
        this.blueAllianceId = blueAllianceId;
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
            ArrayList<Event> events = getEvents(null, this, database);
            Event event = (events.size() > 0 ) ? events.get(0) : null;

            if (event != null)
            {
                setYearId(event.getYearId());
                setBlueAllianceId(event.getBlueAllianceId());
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
        if(!database.isOpen())
            database.open();

        if(database.isOpen())
            id = (int) database.setEvent(this);

        //set the id if the save was successful
        if(id > 0)
            setId(id);

        return getId();
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

    /**
     * Clears all data from the classes table
     * @param database used to clear table
     */
    public static void clearTable(Database database)
    {
        database.clearTable(TABLE_NAME);
    }

    /**
     * Returns arraylist of events with specified filters from database
     * @param year if specified, filters events by year id
     * @param event if specified, filters events by event id
     * @param database used to load events
     * @return arraylist of events
     */
    public static ArrayList<Event> getEvents(@Nullable Year year, @Nullable Event event, Database database)
    {
        return database.getEvents(year, event);
    }

    //endregion
}

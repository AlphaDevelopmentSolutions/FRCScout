package com.alphadevelopmentsolutions.frcscout.Classes;

public class EventTeamList
{

    public static final String TABLE_NAME = "event_team_list";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_EVENT_ID = "EventId";

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
            EventTeamList eventTeamList = database.getEventTeamList(this);


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
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            id = (int) database.setEventTeamList(this);

        }

        return id;
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

    //endregion
}

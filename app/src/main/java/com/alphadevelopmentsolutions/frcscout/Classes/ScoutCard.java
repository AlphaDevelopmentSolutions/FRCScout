package com.alphadevelopmentsolutions.frcscout.Classes;

import java.util.Date;

public class ScoutCard
{

    public static final String TABLE_NAME = "scout_cards";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMNS_NAME_MATCH_ID = "MatchId";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_COMPLETED_BY = "CompletedBy";
    public static final String COLUMN_NAME_COMPLETED_DATE = "CompletedDate";

    private int id;
    private int matchId;
    private int teamId;
    private String completedBy;
    private Date completedDate;

    public ScoutCard(
            int id,
            int matchId,
            int teamId,
            String completedBy,
            Date completedDate)
    {
        this.id = id;
        this.matchId = matchId;
        this.teamId = teamId;
        this.completedBy = completedBy;
        this.completedDate = completedDate;
    }

    /**
     * Used for loading
     * @param id to load
     */
    ScoutCard(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public int getMatchId()
    {
        return matchId;
    }

    public int getTeamId()
    {
        return teamId;
    }

    public String getCompletedBy()
    {
        return completedBy;
    }

    public Date getCompletedDate()
    {
        return completedDate;
    }


    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMatchId(int matchId)
    {
        this.matchId = matchId;
    }

    public void setTeamId(int teamId)
    {
        this.teamId = teamId;
    }

    public void setCompletedBy(String completedBy)
    {
        this.completedBy = completedBy;
    }

    public void setCompletedDate(Date completedDate)
    {
        this.completedDate = completedDate;
    }


    //endregion

    //region Load, Save & Delete

    /**
     * Loads the ScoutCard from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean load(Database database)
    {
        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            ScoutCard scoutCard = database.getScoutCard(this);
            database.close();

            if (scoutCard != null)
            {
                setMatchId(scoutCard.getMatchId());
                setTeamId(scoutCard.getTeamId());
                setCompletedBy(scoutCard.getCompletedBy());
                setCompletedDate(scoutCard.getCompletedDate());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the ScoutCard into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved ScoutCard
     */
    public int save(Database database)
    {
        int id = -1;

        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            id = (int) database.setScoutCard(this);
            database.close();
        }

        return id;
    }

    /**
     * Deletes the ScoutCard from the database
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
            successful = database.deleteScoutCard(this);
            database.close();
        }

        return successful;
    }

    //endregion
}

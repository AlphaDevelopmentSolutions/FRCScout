package com.alphadevelopmentsolutions.frcscout.Classes;

import java.util.Date;

public class Match
{

    public static final String TABLE_NAME = "matches";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_DATE = "Date";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID = "BlueAllianceTeamOneId";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID = "BlueAllianceTeamTwoId";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID = "BlueAllianceTeamThreeId";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID = "BlueAllianceTeamOneScoutCardId";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID = "BlueAllianceTeamTwoScoutCardId";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID = "BlueAllianceTeamThreeScoutCardId";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_SCORE = "BlueAllianceScore";
    public static final String COLUMN_NAME_RED_ALLIANCE_SCORE = "RedAllianceScore";
    public static final String COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID = "RedAllianceTeamOneId";
    public static final String COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID = "RedAllianceTeamTwoId";
    public static final String COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID = "RedAllianceTeamThreeId";
    public static final String COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID = "RedAllianceTeamOneScoutCardId";
    public static final String COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID = "RedAllianceTeamTwoScoutCardId";
    public static final String COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID = "RedAllianceTeamThreeScoutCardId";

    private int id;
    private Date date;
    private int blueAllianceTeamOneId;
    private int blueAllianceTeamTwoId;
    private int blueAllianceTeamThreeId;
    private int blueAllianceTeamOneScoutCardId;
    private int blueAllianceTeamTwoScoutCardId;
    private int blueAllianceTeamThreeScoutCardId;
    private int blueAllianceScore;
    private int redAllianceScore;
    private int redAllianceTeamOneId;
    private int redAllianceTeamTwoId;
    private int redAllianceTeamThreeId;
    private int redAllianceTeamOneScoutCardId;
    private int redAllianceTeamTwoScoutCardId;
    private int redAllianceTeamThreeScoutCardId;


    public Match(
            int id,
            Date date,
            int blueAllianceTeamOneId,
            int blueAllianceTeamTwoId,
            int blueAllianceTeamThreeId,
            int blueAllianceTeamOneScoutCardId,
            int blueAllianceTeamTwoScoutCardId,
            int blueAllianceTeamThreeScoutCardId,
            int blueAllianceScore,
            int redAllianceScore,
            int redAllianceTeamOneId,
            int redAllianceTeamTwoId,
            int redAllianceTeamThreeId,
            int redAllianceTeamOneScoutCardId,
            int redAllianceTeamTwoScoutCardId,
            int redAllianceTeamThreeScoutCardId)
    {
        this.id = id;
        this.date = date;
        this.blueAllianceTeamOneId = blueAllianceTeamOneId;
        this.blueAllianceTeamTwoId = blueAllianceTeamTwoId;
        this.blueAllianceTeamThreeId = blueAllianceTeamThreeId;
        this.blueAllianceTeamOneScoutCardId = blueAllianceTeamOneScoutCardId;
        this.blueAllianceTeamTwoScoutCardId = blueAllianceTeamTwoScoutCardId;
        this.blueAllianceTeamThreeScoutCardId = blueAllianceTeamThreeScoutCardId;
        this.blueAllianceScore = blueAllianceScore;
        this.redAllianceScore = redAllianceScore;
        this.redAllianceTeamOneId = redAllianceTeamOneId;
        this.redAllianceTeamTwoId = redAllianceTeamTwoId;
        this.redAllianceTeamThreeId = redAllianceTeamThreeId;
        this.redAllianceTeamOneScoutCardId = redAllianceTeamOneScoutCardId;
        this.redAllianceTeamTwoScoutCardId = redAllianceTeamTwoScoutCardId;
        this.redAllianceTeamThreeScoutCardId = redAllianceTeamThreeScoutCardId;
    }

    /**
     * Used for loading
     * @param id to load
     */
    Match(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public Date getDate()
    {
        return date;
    }

    public int getBlueAllianceTeamOneId()
    {
        return blueAllianceTeamOneId;
    }

    public int getBlueAllianceTeamTwoId()
    {
        return blueAllianceTeamTwoId;
    }

    public int getBlueAllianceTeamThreeId()
    {
        return blueAllianceTeamThreeId;
    }

    public int getBlueAllianceTeamOneScoutCardId()
    {
        return blueAllianceTeamOneScoutCardId;
    }

    public int getBlueAllianceTeamTwoScoutCardId()
    {
        return blueAllianceTeamTwoScoutCardId;
    }

    public int getBlueAllianceTeamThreeScoutCardId()
    {
        return blueAllianceTeamThreeScoutCardId;
    }

    public int getBlueAllianceScore()
    {
        return blueAllianceScore;
    }

    public int getRedAllianceScore()
    {
        return redAllianceScore;
    }

    public int getRedAllianceTeamOneId()
    {
        return redAllianceTeamOneId;
    }

    public int getRedAllianceTeamTwoId()
    {
        return redAllianceTeamTwoId;
    }

    public int getRedAllianceTeamThreeId()
    {
        return redAllianceTeamThreeId;
    }

    public int getRedAllianceTeamOneScoutCardId()
    {
        return redAllianceTeamOneScoutCardId;
    }

    public int getRedAllianceTeamTwoScoutCardId()
    {
        return redAllianceTeamTwoScoutCardId;
    }

    public int getRedAllianceTeamThreeScoutCardId()
    {
        return redAllianceTeamThreeScoutCardId;
    }

    /**
     * Returns whether the current team in question won
     * @param allianceColor color of the alliance to check win status
     * @return boolean if team won
     */
    public boolean teamWon(String allianceColor)
    {
        if(allianceColor.equals(AllianceColor.RED.name()))
            return getRedAllianceScore() > getBlueAllianceScore();
        else
            return getBlueAllianceScore() > getRedAllianceScore();
    }

    /**
     * Returns whether the current team in question lost
     * @param allianceColor color of the alliance to check lose status
     * @return boolean if team lost
     */
    public boolean teamLost(String allianceColor)
    {
        if(allianceColor.equals(AllianceColor.RED.name()))
            return getRedAllianceScore() < getBlueAllianceScore();
        else
            return getBlueAllianceScore() < getRedAllianceScore();
    }

    /**
     * Returns whether the current team in question tied
     * @return boolean if team tied
     */
    public boolean teamTied()
    {
        return getRedAllianceScore() == getBlueAllianceScore();
    }

    /**
     * Returns the outcome of the match
     * @param allianceColor to check
     * @return outcome of the game for a allianceColor
     */
    public String getOutcomeStatus(String allianceColor)
    {
        if(teamWon(allianceColor))
            return GameScoreStatus.WIN.name();
        else if(teamLost(allianceColor))
            return GameScoreStatus.LOSE.name();
        else
            return GameScoreStatus.TIE.name();
    }

    /**
     * Finds the alliance color of the teamnumber sent in the param
     * @param teamNumber to find
     * @return AllianceColor color of the alliance
     */
    public AllianceColor getTeamAllianceColor(int teamNumber)
    {
        if(blueAllianceTeamOneId == teamNumber ||
            blueAllianceTeamTwoId == teamNumber ||
            blueAllianceTeamThreeId == teamNumber) return AllianceColor.BLUE;

        else if(redAllianceTeamOneId == teamNumber ||
                redAllianceTeamTwoId == teamNumber ||
                redAllianceTeamThreeId == teamNumber) return AllianceColor.RED;

        //default to blue
        return AllianceColor.BLUE;
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setBlueAllianceTeamOneId(int blueAllianceTeamOneId)
    {
        this.blueAllianceTeamOneId = blueAllianceTeamOneId;
    }

    public void setBlueAllianceTeamTwoId(int blueAllianceTeamTwoId)
    {
        this.blueAllianceTeamTwoId = blueAllianceTeamTwoId;
    }

    public void setBlueAllianceTeamThreeId(int blueAllianceTeamThreeId)
    {
        this.blueAllianceTeamThreeId = blueAllianceTeamThreeId;
    }

    public void setBlueAllianceTeamOneScoutCardId(int blueAllianceTeamOneScoutCardId)
    {
        this.blueAllianceTeamOneScoutCardId = blueAllianceTeamOneScoutCardId;
    }

    public void setBlueAllianceTeamTwoScoutCardId(int blueAllianceTeamTwoScoutCardId)
    {
        this.blueAllianceTeamTwoScoutCardId = blueAllianceTeamTwoScoutCardId;
    }

    public void setBlueAllianceTeamThreeScoutCardId(int blueAllianceTeamThreeScoutCardId)
    {
        this.blueAllianceTeamThreeScoutCardId = blueAllianceTeamThreeScoutCardId;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public void setBlueAllianceScore(int blueAllianceScore)
    {
        this.blueAllianceScore = blueAllianceScore;
    }

    public void setRedAllianceScore(int redAllianceScore)
    {
        this.redAllianceScore = redAllianceScore;
    }

    public void setRedAllianceTeamOneId(int redAllianceTeamOneId)
    {
        this.redAllianceTeamOneId = redAllianceTeamOneId;
    }

    public void setRedAllianceTeamTwoId(int redAllianceTeamTwoId)
    {
        this.redAllianceTeamTwoId = redAllianceTeamTwoId;
    }

    public void setRedAllianceTeamThreeId(int redAllianceTeamThreeId)
    {
        this.redAllianceTeamThreeId = redAllianceTeamThreeId;
    }

    public void setRedAllianceTeamOneScoutCardId(int redAllianceTeamOneScoutCardId)
    {
        this.redAllianceTeamOneScoutCardId = redAllianceTeamOneScoutCardId;
    }

    public void setRedAllianceTeamTwoScoutCardId(int redAllianceTeamTwoScoutCardId)
    {
        this.redAllianceTeamTwoScoutCardId = redAllianceTeamTwoScoutCardId;
    }

    public void setRedAllianceTeamThreeScoutCardId(int redAllianceTeamThreeScoutCardId)
    {
        this.redAllianceTeamThreeScoutCardId = redAllianceTeamThreeScoutCardId;
    }

    //endregion

    //region Load, Save & Delete

    /**
     * Loads the Match from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean load(Database database)
    {
        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            Match match = database.getMatch(this);

            if (match != null)
            {
                setDate(match.getDate());
                setBlueAllianceTeamOneId(match.getBlueAllianceTeamOneId());
                setBlueAllianceTeamTwoId(match.getBlueAllianceTeamTwoId());
                setBlueAllianceTeamThreeId(match.getBlueAllianceTeamThreeId());
                setBlueAllianceTeamOneScoutCardId(match.getBlueAllianceTeamOneScoutCardId());
                setBlueAllianceTeamTwoScoutCardId(match.getBlueAllianceTeamTwoScoutCardId());
                setBlueAllianceTeamThreeScoutCardId(match.getBlueAllianceTeamThreeScoutCardId());
                setBlueAllianceScore(match.getBlueAllianceScore());
                setRedAllianceScore(match.getRedAllianceScore());
                setRedAllianceTeamOneId(match.getRedAllianceTeamOneId());
                setRedAllianceTeamTwoId(match.getRedAllianceTeamTwoId());
                setRedAllianceTeamThreeId(match.getRedAllianceTeamThreeId());
                setRedAllianceTeamOneScoutCardId(match.getRedAllianceTeamOneScoutCardId());
                setRedAllianceTeamTwoScoutCardId(match.getRedAllianceTeamTwoScoutCardId());
                setRedAllianceTeamThreeScoutCardId(match.getRedAllianceTeamThreeScoutCardId());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the Match into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved Match
     */
    public int save(Database database)
    {
        int id = -1;

        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            id = (int) database.setMatch(this);

        }

        return id;
    }

    /**
     * Deletes the Match from the database
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
            successful = database.deleteMatch(this);

        }

        return successful;
    }

    //endregion
}

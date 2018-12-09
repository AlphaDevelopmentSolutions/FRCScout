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
    public static final String COLUMN_NAME_BLUE_ALLIANCE_SCORE = "BlueAllianceScore";
    public static final String COLUMN_NAME_RED_ALLIANCE_SCORE = "RedAllianceScore";
    public static final String COLUMN_NAME_OPPONENT_ALLIANCE_TEAM_ONE_ID = "RedAllianceTeamOneId";
    public static final String COLUMN_NAME_OPPONENT_ALLIANCE_TEAM_TWO_ID = "RedAllianceTeamTwoId";
    public static final String COLUMN_NAME_OPPONENT_ALLIANCE_TEAM_THREE_ID = "RedAllianceTeamThreeId";

    private int id;
    private Date date;
    private int blueAllianceTeamOneId;
    private int blueAllianceTeamTwoId;
    private int blueAllianceTeamThreeId;
    private int blueAllianceScore;
    private int redAllianceScore;
    private int redAllianceTeamOneId;
    private int redAllianceTeamTwoId;
    private int redAllianceTeamThreeId;


    public Match(
            int id,
            Date date,
            int blueAllianceTeamOneId,
            int blueAllianceTeamTwoId,
            int blueAllianceTeamThreeId,
            int blueAllianceScore,
            int redAllianceScore,
            int redAllianceTeamOneId,
            int redAllianceTeamTwoId,
            int redAllianceTeamThreeId)
    {
        this.id = id;
        this.date = date;
        this.blueAllianceTeamOneId = blueAllianceTeamOneId;
        this.blueAllianceTeamTwoId = blueAllianceTeamTwoId;
        this.blueAllianceTeamThreeId = blueAllianceTeamThreeId;
        this.blueAllianceScore = blueAllianceScore;
        this.redAllianceScore = redAllianceScore;
        this.redAllianceTeamOneId = redAllianceTeamOneId;
        this.redAllianceTeamTwoId = redAllianceTeamTwoId;
        this.redAllianceTeamThreeId = redAllianceTeamThreeId;
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

    /**
     * Returns whether the current team in question won
     * @param allianceColor color of the alliance to check win status
     * @return boolean if team won
     */
    public boolean teamWon(AllianceColor allianceColor)
    {
        if(allianceColor == AllianceColor.RED) return getRedAllianceScore() > getBlueAllianceScore();
        else return getBlueAllianceScore() > getRedAllianceScore();
    }

    /**
     * Returns whether the current team in question lost
     * @param allianceColor color of the alliance to check lose status
     * @return boolean if team lost
     */
    public boolean teamLost(AllianceColor allianceColor)
    {
        if(allianceColor == AllianceColor.RED) return getRedAllianceScore() < getBlueAllianceScore();
        else return getBlueAllianceScore() < getRedAllianceScore();
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
    public String getOutcomeStatus(AllianceColor allianceColor)
    {
        if(teamWon(allianceColor)) return GameScoreStatuses.WIN;
        else if(teamLost(allianceColor)) return GameScoreStatuses.LOSE;
        else return GameScoreStatuses.TIE;
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
            database.close();

            if (match != null)
            {
                setBlueAllianceTeamOneId(match.getBlueAllianceTeamOneId());
                setDate(match.getDate());
                setBlueAllianceTeamTwoId(match.getBlueAllianceTeamTwoId());
                setBlueAllianceTeamThreeId(match.getBlueAllianceTeamThreeId());
                setBlueAllianceScore(match.getBlueAllianceScore());
                setRedAllianceScore(match.getRedAllianceScore());
                setRedAllianceTeamOneId(match.getRedAllianceTeamOneId());
                setRedAllianceTeamTwoId(match.getRedAllianceTeamTwoId());
                setRedAllianceTeamThreeId(match.getRedAllianceTeamThreeId());
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
            database.close();
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
            database.close();
        }

        return successful;
    }

    //endregion
}
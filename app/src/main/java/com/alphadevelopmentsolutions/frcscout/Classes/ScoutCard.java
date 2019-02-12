package com.alphadevelopmentsolutions.frcscout.Classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoutCard
{

    public static final String TABLE_NAME = "scout_cards";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMNS_NAME_MATCH_ID = "MatchId";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_EVENT_ID = "EventId";
    public static final String COLUMN_NAME_COMPLETED_BY = "CompletedBy";
    public static final String COLUMN_NAME_BLUE_ALLIANCE_FINAL_SCORE = "BlueAllianceFinalScore";
    public static final String COLUMN_NAME_RED_ALLIANCE_FINAL_SCORE = "RedAllianceFinalScore";
    public static final String COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT = "AutonomousExitHabitat";
    public static final String COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED = "AutonomousHatchPanelsSecured";
    public static final String COLUMN_NAME_AUTONOMOUS_CARGO_STORED = "AutonomousCargoStored";
    public static final String COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED = "TeleopHatchPanelsSecured";
    public static final String COLUMN_NAME_TELEOP_CARGO_STORED = "TeleopCargoStored";
    public static final String COLUMN_NAME_TELEOP_ROCKETS_COMPLETED = "TeleopRocketsCompleted";
    public static final String COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT = "EndGameReturnedToHabitat";
    public static final String COLUMN_NAME_NOTES = "Notes";
    public static final String COLUMN_NAME_COMPLETED_DATE = "CompletedDate";

    private int id;
    private int matchId;
    private int teamId;
    private String eventId;
    private String completedBy;
    private int blueAllianceFinalScore;
    private int redAllianceFinalScore;
    private boolean autonomousExitHabitat;
    private int autonomousHatchPanelsSecured;
    private int autonomousCargoStored;
    private int teleopHatchPanelsSecured;
    private int teleopCargoStored;
    private int teleopRocketsCompleted;
    private String endGameReturnedToHabitat;
    private String notes;
    private Date completedDate;

    public ScoutCard(
            int id,
            int matchId,
            int teamId,
            String eventId,
            String completedBy,
            int blueAllianceFinalScore,
            int redAllianceFinalScore,
            boolean autonomousExitHabitat,
            int autonomousHatchPanelsSecured,
            int autonomousCargoStored,
            int teleopHatchPanelsSecured,
            int teleopCargoStored,
            int teleopRocketsCompleted,
            String endGameReturnedToHabitat,
            String notes,
            Date completedDate)
    {
        this.id = id;
        this.matchId = matchId;
        this.teamId = teamId;
        this.eventId = eventId;
        this.blueAllianceFinalScore = blueAllianceFinalScore;
        this.redAllianceFinalScore = redAllianceFinalScore;
        this.autonomousExitHabitat = autonomousExitHabitat;
        this.autonomousHatchPanelsSecured = autonomousHatchPanelsSecured;
        this.autonomousCargoStored = autonomousCargoStored;
        this.teleopHatchPanelsSecured = teleopHatchPanelsSecured;
        this.teleopCargoStored = teleopCargoStored;
        this.teleopRocketsCompleted = teleopRocketsCompleted;
        this.endGameReturnedToHabitat = endGameReturnedToHabitat;
        this.notes = notes;
        this.completedBy = completedBy;
        this.completedDate = completedDate;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public ScoutCard(int id)
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

    public String getEventId()
    {
        return eventId;
    }

    public int getBlueAllianceFinalScore()
    {
        return blueAllianceFinalScore;
    }

    public int getRedAllianceFinalScore()
    {
        return redAllianceFinalScore;
    }

    public boolean isAutonomousExitHabitat()
    {
        return autonomousExitHabitat;
    }

    public int getAutonomousHatchPanelsSecured()
    {
        return autonomousHatchPanelsSecured;
    }

    public int getAutonomousCargoStored()
    {
        return autonomousCargoStored;
    }

    public int getTeleopHatchPanelsSecured()
    {
        return teleopHatchPanelsSecured;
    }

    public int getTeleopCargoStored()
    {
        return teleopCargoStored;
    }

    public int getTeleopRocketsCompleted()
    {
        return teleopRocketsCompleted;
    }

    public String getEndGameReturnedToHabitat()
    {
        return endGameReturnedToHabitat;
    }

    public String getCompletedBy()
    {
        return completedBy;
    }

    public Date getCompletedDate()
    {
        return completedDate;
    }

    public String getCompletedDateForSQL()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

        return simpleDateFormat.format(completedDate);
    }

    public String getNotes()
    {
        return notes;
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

    public void setEventId(String eventId)
    {
        this.eventId = eventId;
    }

    public void setBlueAllianceFinalScore(int blueAllianceFinalScore)
    {
        this.blueAllianceFinalScore = blueAllianceFinalScore;
    }

    public void setRedAllianceFinalScore(int redAllianceFinalScore)
    {
        this.redAllianceFinalScore = redAllianceFinalScore;
    }

    public void setAutonomousExitHabitat(boolean autonomousExitHabitat)
    {
        this.autonomousExitHabitat = autonomousExitHabitat;
    }

    public void setAutonomousHatchPanelsSecured(int autonomousHatchPanelsSecured)
    {
        this.autonomousHatchPanelsSecured = autonomousHatchPanelsSecured;
    }

    public void setAutonomousCargoStored(int autonomousCargoStored)
    {
        this.autonomousCargoStored = autonomousCargoStored;
    }

    public void setTeleopHatchPanelsSecured(int teleopHatchPanelsSecured)
    {
        this.teleopHatchPanelsSecured = teleopHatchPanelsSecured;
    }

    public void setTeleopCargoStored(int teleopCargoStored)
    {
        this.teleopCargoStored = teleopCargoStored;
    }

    public void setTeleopRocketsCompleted(int teleopRocketsCompleted)
    {
        this.teleopRocketsCompleted = teleopRocketsCompleted;
    }

    public void setEndGameReturnedToHabitat(String endGameReturnedToHabitat)
    {
        this.endGameReturnedToHabitat = endGameReturnedToHabitat;
    }

    public void setCompletedBy(String completedBy)
    {
        this.completedBy = completedBy;
    }

    public void setCompletedDate(Date completedDate)
    {
        this.completedDate = completedDate;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
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


            if (scoutCard != null)
            {
                setMatchId(scoutCard.getMatchId());
                setTeamId(scoutCard.getTeamId());
                setEventId(scoutCard.getEventId());
                setBlueAllianceFinalScore(scoutCard.getBlueAllianceFinalScore());
                setRedAllianceFinalScore(scoutCard.getRedAllianceFinalScore());
                setAutonomousExitHabitat(scoutCard.isAutonomousExitHabitat());
                setAutonomousHatchPanelsSecured(scoutCard.getAutonomousHatchPanelsSecured());
                setAutonomousCargoStored(scoutCard.getAutonomousCargoStored());
                setTeleopHatchPanelsSecured(scoutCard.getTeleopHatchPanelsSecured());
                setTeleopCargoStored(scoutCard.getTeleopCargoStored());
                setTeleopRocketsCompleted(scoutCard.getTeleopRocketsCompleted());
                setEndGameReturnedToHabitat(scoutCard.getEndGameReturnedToHabitat());
                setNotes(scoutCard.getNotes());
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

        }

        return successful;
    }

    //endregion
}

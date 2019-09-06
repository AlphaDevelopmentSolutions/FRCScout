package com.alphadevelopmentsolutions.frcscout.Classes;

import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ScoutCard
{

    public static final String TABLE_NAME = "scout_cards";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_MATCH_ID = "MatchId";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_EVENT_ID = "EventId";
    public static final String COLUMN_NAME_ALLIANCE_COLOR = "AllianceColor";
    public static final String COLUMN_NAME_COMPLETED_BY = "CompletedBy";

    public static final String COLUMN_NAME_PRE_GAME_STARTING_LEVEL = "PreGameStartingLevel";
    public static final String COLUMN_NAME_PRE_GAME_STARTING_POSITION = "PreGameStartingPosition";
    public static final String COLUMN_NAME_PRE_GAME_STARTING_PIECE = "PreGameStartingPiece";

    public static final String COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT = "AutonomousExitHabitat";
    public static final String COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP = "AutonomousHatchPanelsPickedUp";
    public static final String COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED = "AutonomousHatchPanelsSecured";
    public static final String COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS = "AutonomousHatchPanelsSecuredAttempts";
    public static final String COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP = "AutonomousCargoPickedUp";
    public static final String COLUMN_NAME_AUTONOMOUS_CARGO_STORED = "AutonomousCargoStored";
    public static final String COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS = "AutonomousCargoStoredAttempts";

    public static final String COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP = "TeleopHatchPanelsPickedUp";
    public static final String COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED = "TeleopHatchPanelsSecured";
    public static final String COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS = "TeleopHatchPanelsSecuredAttempts";
    public static final String COLUMN_NAME_TELEOP_CARGO_PICKED_UP = "TeleopCargoPickedUp";
    public static final String COLUMN_NAME_TELEOP_CARGO_STORED = "TeleopCargoStored";
    public static final String COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS = "TeleopCargoStoredAttempts";

    public static final String COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT = "EndGameReturnedToHabitat";
    public static final String COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS = "EndGameReturnedToHabitatAttempts";

    public static final String COLUMN_NAME_DEFENSE_RATING = "DefenseRating";
    public static final String COLUMN_NAME_OFFENSE_RATING = "OffenseRating";
    public static final String COLUMN_NAME_DRIVE_RATING = "DriveRating";
    public static final String COLUMN_NAME_NOTES = "Notes";

    public static final String COLUMN_NAME_COMPLETED_DATE = "CompletedDate";
    public static final String COLUMN_NAME_IS_DRAFT = "IsDraft";

    private int id;
    private String matchId;
    private int teamId;
    private String eventId;
    private String allianceColor;
    private String completedBy;

    private int preGameStartingLevel;
    private StartingPosition preGameStartingPosition;
    private StartingPiece preGameStartingPiece;

    private boolean autonomousExitHabitat;
    private int autonomousHatchPanelsPickedUp;
    private int autonomousHatchPanelsSecuredAttempts;
    private int autonomousHatchPanelsSecured;
    private int autonomousCargoPickedUp;
    private int autonomousCargoStoredAttempts;
    private int autonomousCargoStored;
    
    private int teleopHatchPanelsPickedUp;
    private int teleopHatchPanelsSecuredAttempts;
    private int teleopHatchPanelsSecured;
    private int teleopCargoPickedUp;
    private int teleopCargoStoredAttempts;
    private int teleopCargoStored;

    private int endGameReturnedToHabitat;
    private int endGameReturnedToHabitatAttempts;
    
    private int defenseRating;
    private int offenseRating;
    private int driveRating;
    private String notes;
    private Date completedDate;
    private boolean isDraft;

    public ScoutCard(
            int id,
            String matchId,
            int teamId,
            String eventId,
            String allianceColor,
            String completedBy,

            int preGameStartingLevel,
            StartingPosition preGameStartingPosition,
            StartingPiece preGameStartingPiece,

            boolean autonomousExitHabitat,
            int autonomousHatchPanelsPickedUp,
            int autonomousHatchPanelsSecuredAttempts,
            int autonomousHatchPanelsSecured,
            int autonomousCargoPickedUp,
            int autonomousCargoStoredAttempts,
            int autonomousCargoStored,

            int teleopHatchPanelsPickedUp,
            int teleopHatchPanelsSecuredAttempts,
            int teleopHatchPanelsSecured,
            int teleopCargoPickedUp,
            int teleopCargoStoredAttempts,
            int teleopCargoStored,

            int endGameReturnedToHabitat,
            int endGameReturnedToHabitatAttempts,

            int defenseRating,
            int offenseRating,
            int driveRating,
            String notes,
            Date completedDate,
            boolean isDraft)
    {
        this.id = id;
        this.matchId = matchId;
        this.teamId = teamId;
        this.eventId = eventId;
        this.allianceColor = allianceColor;
        this.completedBy = completedBy;

        this.preGameStartingLevel = preGameStartingLevel;
        this.preGameStartingPosition = preGameStartingPosition;
        this.preGameStartingPiece = preGameStartingPiece;

        this.autonomousExitHabitat = autonomousExitHabitat;
        this.autonomousHatchPanelsPickedUp = autonomousHatchPanelsPickedUp;
        this.autonomousHatchPanelsSecuredAttempts = autonomousHatchPanelsSecuredAttempts;
        this.autonomousHatchPanelsSecured = autonomousHatchPanelsSecured;
        this.autonomousCargoPickedUp = autonomousCargoPickedUp;
        this.autonomousCargoStoredAttempts = autonomousCargoStoredAttempts;
        this.autonomousCargoStored = autonomousCargoStored;

        this.teleopHatchPanelsPickedUp = teleopHatchPanelsPickedUp;
        this.teleopHatchPanelsSecuredAttempts = teleopHatchPanelsSecuredAttempts;
        this.teleopHatchPanelsSecured = teleopHatchPanelsSecured;
        this.teleopCargoPickedUp = teleopCargoPickedUp;
        this.teleopCargoStoredAttempts = teleopCargoStoredAttempts;
        this.teleopCargoStored = teleopCargoStored;

        this.endGameReturnedToHabitat = endGameReturnedToHabitat;
        this.endGameReturnedToHabitatAttempts = endGameReturnedToHabitatAttempts;

        this.defenseRating = defenseRating;
        this.offenseRating = offenseRating;
        this.driveRating = driveRating;
        this.notes = notes;
        this.completedDate = completedDate;
        this.isDraft = isDraft;
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

    public String  getMatchId()
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

    public String getAllianceColor()
    {
        return allianceColor;
    }

    public String getCompletedBy()
    {
        return completedBy;
    }

    public int getPreGameStartingLevel()
    {
        return preGameStartingLevel;
    }

    public StartingPosition getPreGameStartingPosition()
    {
        return preGameStartingPosition;
    }

    public StartingPiece getPreGameStartingPiece()
    {
        return preGameStartingPiece;
    }

    public boolean getAutonomousExitHabitat()
    {
        return autonomousExitHabitat;
    }

    public int getAutonomousHatchPanelsPickedUp()
    {
        return autonomousHatchPanelsPickedUp;
    }

    public int getAutonomousHatchPanelsSecuredAttempts()
    {
        return autonomousHatchPanelsSecuredAttempts;
    }

    public int getAutonomousHatchPanelsSecured()
    {
        return autonomousHatchPanelsSecured;
    }

    public int getAutonomousCargoPickedUp()
    {
        return autonomousCargoPickedUp;
    }

    public int getAutonomousCargoStoredAttempts()
    {
        return autonomousCargoStoredAttempts;
    }

    public int getAutonomousCargoStored()
    {
        return autonomousCargoStored;
    }

    public int getTeleopHatchPanelsPickedUp()
    {
        return teleopHatchPanelsPickedUp;
    }

    public int getTeleopHatchPanelsSecuredAttempts()
    {
        return teleopHatchPanelsSecuredAttempts;
    }

    public int getTeleopHatchPanelsSecured()
    {
        return teleopHatchPanelsSecured;
    }

    public int getTeleopCargoPickedUp()
    {
        return teleopCargoPickedUp;
    }

    public int getTeleopCargoStoredAttempts()
    {
        return teleopCargoStoredAttempts;
    }

    public int getTeleopCargoStored()
    {
        return teleopCargoStored;
    }

    public int getEndGameReturnedToHabitat()
    {
        return endGameReturnedToHabitat;
    }

    public int getEndGameReturnedToHabitatAttempts()
    {
        return endGameReturnedToHabitatAttempts;
    }

    public int getDefenseRating()
    {
        return defenseRating;
    }

    public int getOffenseRating()
    {
        return offenseRating;
    }

    public int getDriveRating()
    {
        return driveRating;
    }

    public String getNotes()
    {
        return notes;
    }

    public Date getCompletedDate()
    {
        return completedDate;
    }

    public boolean isDraft()
    {
        return isDraft;
    }

    /**
     * Gets the completed date formated for MySQL timestamp
     * @return MySQL time stamp formatted date
     */
    public String getCompletedDateForSQL()
    {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd H:mm:ss");

        return simpleDateFormat.format(completedDate);
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setMatchId(String matchId)
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

    public void setAllianceColor(String allianceColor)
    {
        this.allianceColor = allianceColor;
    }

    public void setCompletedBy(String completedBy)
    {
        this.completedBy = completedBy;
    }

    public void setPreGameStartingLevel(int preGameStartingLevel)
    {
        this.preGameStartingLevel = preGameStartingLevel;
    }

    public void setPreGameStartingPosition(StartingPosition preGameStartingPosition)
    {
        this.preGameStartingPosition = preGameStartingPosition;
    }

    public void setPreGameStartingPiece(StartingPiece preGameStartingPiece)
    {
        this.preGameStartingPiece = preGameStartingPiece;
    }

    public void setAutonomousExitHabitat(boolean autonomousExitHabitat)
    {
        this.autonomousExitHabitat = autonomousExitHabitat;
    }

    public void setAutonomousHatchPanelsPickedUp(int autonomousHatchPanelsPickedUp)
    {
        this.autonomousHatchPanelsPickedUp = autonomousHatchPanelsPickedUp;
    }

    public void setAutonomousHatchPanelsSecuredAttempts(int autonomousHatchPanelsSecuredAttempts)
    {
        this.autonomousHatchPanelsSecuredAttempts = autonomousHatchPanelsSecuredAttempts;
    }

    public void setAutonomousHatchPanelsSecured(int autonomousHatchPanelsSecured)
    {
        this.autonomousHatchPanelsSecured = autonomousHatchPanelsSecured;
    }

    public void setAutonomousCargoPickedUp(int autonomousCargoPickedUp)
    {
        this.autonomousCargoPickedUp = autonomousCargoPickedUp;
    }

    public void setAutonomousCargoStoredAttempts(int autonomousCargoStoredAttempts)
    {
        this.autonomousCargoStoredAttempts = autonomousCargoStoredAttempts;
    }

    public void setAutonomousCargoStored(int autonomousCargoStored)
    {
        this.autonomousCargoStored = autonomousCargoStored;
    }

    public void setTeleopHatchPanelsPickedUp(int teleopHatchPanelsPickedUp)
    {
        this.teleopHatchPanelsPickedUp = teleopHatchPanelsPickedUp;
    }

    public void setTeleopHatchPanelsSecuredAttempts(int teleopHatchPanelsSecuredAttempts)
    {
        this.teleopHatchPanelsSecuredAttempts = teleopHatchPanelsSecuredAttempts;
    }

    public void setTeleopHatchPanelsSecured(int teleopHatchPanelsSecured)
    {
        this.teleopHatchPanelsSecured = teleopHatchPanelsSecured;
    }

    public void setTeleopCargoPickedUp(int teleopCargoPickedUp)
    {
        this.teleopCargoPickedUp = teleopCargoPickedUp;
    }

    public void setTeleopCargoStoredAttempts(int teleopCargoStoredAttempts)
    {
        this.teleopCargoStoredAttempts = teleopCargoStoredAttempts;
    }

    public void setTeleopCargoStored(int teleopCargoStored)
    {
        this.teleopCargoStored = teleopCargoStored;
    }

    public void setEndGameReturnedToHabitat(int endGameReturnedToHabitat)
    {
        this.endGameReturnedToHabitat = endGameReturnedToHabitat;
    }

    public void setEndGameReturnedToHabitatAttempts(int endGameReturnedToHabitatAttempts)
    {
        this.endGameReturnedToHabitatAttempts = endGameReturnedToHabitatAttempts;
    }

    public void setDefenseRating(int defenseRating)
    {
        this.defenseRating = defenseRating;
    }

    public void setOffenseRating(int offenseRating)
    {
        this.offenseRating = offenseRating;
    }

    public void setDriveRating(int driveRating)
    {
        this.driveRating = driveRating;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public void setCompletedDate(Date completedDate)
    {
        this.completedDate = completedDate;
    }

    public void setDraft(boolean draft)
    {
        isDraft = draft;
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
                setAllianceColor(scoutCard.getAllianceColor());
                setCompletedBy(scoutCard.getCompletedBy());

                setPreGameStartingLevel(scoutCard.getPreGameStartingLevel());
                setPreGameStartingPosition(scoutCard.getPreGameStartingPosition());
                setPreGameStartingPiece(scoutCard.getPreGameStartingPiece());

                setAutonomousExitHabitat(scoutCard.getAutonomousExitHabitat());
                setAutonomousHatchPanelsPickedUp(scoutCard.getAutonomousHatchPanelsPickedUp());
                setAutonomousHatchPanelsSecuredAttempts(scoutCard.getAutonomousHatchPanelsSecuredAttempts());
                setAutonomousHatchPanelsSecured(scoutCard.getAutonomousHatchPanelsSecured());
                setAutonomousCargoPickedUp(scoutCard.getAutonomousCargoPickedUp());
                setAutonomousCargoStoredAttempts(scoutCard.getAutonomousCargoStoredAttempts());
                setAutonomousCargoStored(scoutCard.getAutonomousCargoStored());

                setTeleopHatchPanelsPickedUp(scoutCard.getTeleopHatchPanelsPickedUp());
                setTeleopHatchPanelsSecuredAttempts(scoutCard.getTeleopHatchPanelsSecuredAttempts());
                setTeleopHatchPanelsSecured(scoutCard.getTeleopHatchPanelsSecured());
                setTeleopCargoPickedUp(scoutCard.getTeleopCargoPickedUp());
                setTeleopCargoStoredAttempts(scoutCard.getTeleopCargoStoredAttempts());
                setTeleopCargoStored(scoutCard.getTeleopCargoStored());

                setEndGameReturnedToHabitat(scoutCard.getEndGameReturnedToHabitat());
                setEndGameReturnedToHabitatAttempts(scoutCard.getEndGameReturnedToHabitatAttempts());

                setDefenseRating(scoutCard.getDefenseRating());
                setOffenseRating(scoutCard.getOffenseRating());
                setDriveRating(scoutCard.getDriveRating());
                setNotes(scoutCard.getNotes());
                setCompletedDate(scoutCard.getCompletedDate());
                setDraft(scoutCard.isDraft());
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
        if(!database.isOpen())
            database.open();

        if(database.isOpen())
            id = (int) database.setScoutCard(this);

        //set the id if the save was successful
        if(id > 0)
            setId(id);

        return getId();
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

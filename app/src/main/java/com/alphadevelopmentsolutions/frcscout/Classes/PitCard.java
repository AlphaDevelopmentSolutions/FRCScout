package com.alphadevelopmentsolutions.frcscout.Classes;

public class PitCard
{

    public static final String TABLE_NAME = "pit_cards";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_EVENT_ID = "EventId";
    public static final String COLUMN_NAME_DRIVE_STYLE = "DriveStyle";
    public static final String COLUMN_NAME_AUTO_EXIT_HABITAT = "AutoExitHabitat";
    public static final String COLUMN_NAME_AUTO_HATCH = "AutoHatch";
    public static final String COLUMN_NAME_AUTO_CARGO = "AutoCargo";
    public static final String COLUMN_NAME_TELEOP_HATCH = "TeleopHatch";
    public static final String COLUMN_NAME_TELEOP_CARGO = "TeleopCargo";
    public static final String COLUMN_NAME_TELEOP_ROCKETS_COMPLETE = "TeleopRocketsComplete";
    public static final String COLUMN_NAME_RETURN_TO_HABITAT = "ReturnToHabitat";
    public static final String COLUMN_NAME_NOTES = "Notes";
    public static final String COLUMN_NAME_COMPLETED_BY = "CompletedBy";


    private int id;
    private int teamId;
    private String eventId;
    private String driveStyle;
    private String autoExitHabitat;
    private String autoHatch;
    private String autoCargo;
    private String teleopHatch;
    private String teleopCargo;
    private String teleopRocketsComplete;
    private String returnToHabitat;
    private String notes;
    private String completedBy;
    public PitCard(
            int id,
            int teamId,
            String eventId,
            String driveStyle,
            String autoExitHabitat,
            String autoHatch,
            String autoCargo,
            String teleopHatch,
            String teleopCargo,
            String teleopRocketsComplete,
            String returnToHabitat,
            String notes,
            String completedBy)
    {
        this.id = id;
        this.teamId = teamId;
        this.eventId = eventId;
        this.driveStyle = driveStyle;
        this.autoExitHabitat = autoExitHabitat;
        this.autoHatch = autoHatch;
        this.autoCargo = autoCargo;
        this.teleopHatch = teleopHatch;
        this.teleopCargo = teleopCargo;
        this.teleopRocketsComplete = teleopRocketsComplete;
        this.returnToHabitat = returnToHabitat;
        this.notes = notes;
        this.completedBy = completedBy;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public PitCard(int id)
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

    public String getDriveStyle()
    {
        return driveStyle;
    }

    public String getAutoExitHabitat()
    {
        return autoExitHabitat;
    }

    public String getAutoHatch()
    {
        return autoHatch;
    }

    public String getAutoCargo()
    {
        return autoCargo;
    }

    public String getTeleopHatch()
    {
        return teleopHatch;
    }

    public String getTeleopCargo()
    {
        return teleopCargo;
    }

    public String getTeleopRocketsComplete()
    {
        return teleopRocketsComplete;
    }

    public String getReturnToHabitat()
    {
        return returnToHabitat;
    }

    public String getNotes()
    {
        return notes;
    }

    public String getCompletedBy()
    {
        return completedBy;
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

    public void setDriveStyle(String driveStyle)
    {
        this.driveStyle = driveStyle;
    }

    public void setAutoExitHabitat(String autoExitHabitat)
    {
        this.autoExitHabitat = autoExitHabitat;
    }

    public void setAutoHatch(String autoHatch)
    {
        this.autoHatch = autoHatch;
    }

    public void setAutoCargo(String autoCargo)
    {
        this.autoCargo = autoCargo;
    }

    public void setTeleopHatch(String teleopHatch)
    {
        this.teleopHatch = teleopHatch;
    }

    public void setTeleopCargo(String teleopCargo)
    {
        this.teleopCargo = teleopCargo;
    }

    public void setTeleopRocketsComplete(String teleopRocketsComplete)
    {
        this.teleopRocketsComplete = teleopRocketsComplete;
    }

    public void setReturnToHabitat(String returnToHabitat)
    {
        this.returnToHabitat = returnToHabitat;
    }

    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public void setCompletedBy(String completedBy)
    {
        this.completedBy = completedBy;
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
            PitCard pitCard = database.getPitCard(this);


            if (pitCard != null)
            {
                setTeamId(pitCard.getTeamId());
                setEventId(pitCard.getEventId());
                setDriveStyle(pitCard.getDriveStyle());
                setAutoExitHabitat(pitCard.getAutoExitHabitat());
                setAutoHatch(pitCard.getAutoHatch());
                setAutoCargo(pitCard.getAutoCargo());
                setTeleopHatch(pitCard.getTeleopHatch());
                setTeleopCargo(pitCard.getTeleopCargo());
                setTeleopRocketsComplete(pitCard.getTeleopRocketsComplete());
                setReturnToHabitat(pitCard.getReturnToHabitat());
                setNotes(pitCard.getNotes());
                setCompletedBy(pitCard.getCompletedBy());
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
            id = (int) database.setPitCard(this);

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
            successful = database.deletePitCard(this);

        }

        return successful;
    }

    //endregion
}

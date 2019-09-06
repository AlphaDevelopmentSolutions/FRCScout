package com.alphadevelopmentsolutions.frcscout.Classes;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class PitCard extends Table
{

    public static final String TABLE_NAME = "pit_cards";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_EVENT_ID = "EventId";

    public static final String COLUMN_NAME_DRIVE_STYLE = "DriveStyle";
    public static final String COLUMN_NAME_ROBOT_WEIGHT = "RobotWeight";
    public static final String COLUMN_NAME_ROBOT_LENGTH = "RobotLength";
    public static final String COLUMN_NAME_ROBOT_WIDTH = "RobotWidth";
    public static final String COLUMN_NAME_ROBOT_HEIGHT = "RobotHeight";

    public static final String COLUMN_NAME_AUTO_EXIT_HABITAT = "AutoExitHabitat";
    public static final String COLUMN_NAME_AUTO_HATCH = "AutoHatch";
    public static final String COLUMN_NAME_AUTO_CARGO = "AutoCargo";

    public static final String COLUMN_NAME_TELEOP_HATCH = "TeleopHatch";
    public static final String COLUMN_NAME_TELEOP_CARGO = "TeleopCargo";

    public static final String COLUMN_NAME_RETURN_TO_HABITAT = "ReturnToHabitat";

    public static final String COLUMN_NAME_NOTES = "Notes";

    public static final String COLUMN_NAME_COMPLETED_BY = "CompletedBy";
    public static final String COLUMN_NAME_IS_DRAFT = "IsDraft";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TEAM_ID + " TEXT," +
                    COLUMN_NAME_EVENT_ID + " TEXT," +

                    COLUMN_NAME_DRIVE_STYLE + " TEXT," +
                    COLUMN_NAME_ROBOT_WEIGHT + " TEXT," +
                    COLUMN_NAME_ROBOT_LENGTH + " TEXT," +
                    COLUMN_NAME_ROBOT_WIDTH + " TEXT," +
                    COLUMN_NAME_ROBOT_HEIGHT + " TEXT," +

                    COLUMN_NAME_AUTO_EXIT_HABITAT + " TEXT," +
                    COLUMN_NAME_AUTO_HATCH + " TEXT," +
                    COLUMN_NAME_AUTO_CARGO + " TEXT," +

                    COLUMN_NAME_TELEOP_HATCH + " TEXT," +
                    COLUMN_NAME_TELEOP_CARGO + " TEXT," +

                    COLUMN_NAME_RETURN_TO_HABITAT + " TEXT," +

                    COLUMN_NAME_NOTES + " TEXT," +

                    COLUMN_NAME_COMPLETED_BY + " TEXT," +
                    COLUMN_NAME_IS_DRAFT + " INTEGER)";


    private int id;
    private int teamId;
    private String eventId;

    private String driveStyle;
    private String robotWeight;
    private String robotLength;
    private String robotWidth;
    private String robotHeight;

    private String autoExitHabitat;
    private String autoHatch;
    private String autoCargo;

    private String teleopHatch;
    private String teleopCargo;

    private String returnToHabitat;

    private String notes;

    private String completedBy;
    private boolean isDraft;

    public PitCard(
            int id,
            int teamId,
            String eventId,

            String driveStyle,
            String robotWeight,
            String robotLength,
            String robotWidth,
            String robotHeight,

            String autoExitHabitat,
            String autoHatch,
            String autoCargo,

            String teleopHatch,
            String teleopCargo,

            String returnToHabitat,

            String notes,

            String completedBy,
            boolean isDraft)
    {
        this.id = id;
        this.teamId = teamId;
        this.eventId = eventId;

        this.driveStyle = driveStyle;
        this.robotWeight = robotWeight;
        this.robotLength = robotLength;
        this.robotWidth = robotWidth;
        this.robotHeight = robotHeight;

        this.autoExitHabitat = autoExitHabitat;
        this.autoHatch = autoHatch;
        this.autoCargo = autoCargo;

        this.teleopHatch = teleopHatch;
        this.teleopCargo = teleopCargo;

        this.returnToHabitat = returnToHabitat;

        this.notes = notes;

        this.completedBy = completedBy;
        this.isDraft = isDraft;
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

    public String getRobotWeight()
    {
        return robotWeight;
    }

    public String getRobotLength()
    {
        return robotLength;
    }

    public String getRobotWidth()
    {
        return robotWidth;
    }

    public String getRobotHeight()
    {
        return robotHeight;
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

    public boolean isDraft()
    {
        return isDraft;
    }

    @Override
    public String toString()
    {
        return "Team " + getTeamId() + " - Pit Card";
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

    public void setRobotWeight(String robotWeight)
    {
        this.robotWeight = robotWeight;
    }

    public void setRobotLength(String robotLength)
    {
        this.robotLength = robotLength;
    }

    public void setRobotWidth(String robotWidth)
    {
        this.robotWidth = robotWidth;
    }

    public void setRobotHeight(String robotHeight)
    {
        this.robotHeight = robotHeight;
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

    public void setDraft(boolean draft)
    {
        isDraft = draft;
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
            ArrayList<PitCard> pitCards = getPitCards(null, null, this, false, database);
            PitCard pitCard = (pitCards.size() > 0 ) ? pitCards.get(0) : null;

            if (pitCard != null)
            {
                setTeamId(pitCard.getTeamId());
                setEventId(pitCard.getEventId());

                setDriveStyle(pitCard.getDriveStyle());
                setRobotWeight(pitCard.getRobotWeight());
                setRobotLength(pitCard.getRobotLength());
                setRobotWidth(pitCard.getRobotWidth());
                setRobotHeight(pitCard.getRobotHeight());

                setAutoExitHabitat(pitCard.getAutoExitHabitat());
                setAutoHatch(pitCard.getAutoHatch());
                setAutoCargo(pitCard.getAutoCargo());

                setTeleopHatch(pitCard.getTeleopHatch());
                setTeleopCargo(pitCard.getTeleopCargo());

                setReturnToHabitat(pitCard.getReturnToHabitat());

                setNotes(pitCard.getNotes());

                setCompletedBy(pitCard.getCompletedBy());
                setDraft(pitCard.isDraft());
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
        if(!database.isOpen())
            database.open();

        if(database.isOpen())
            id = (int) database.setPitCard(this);

        //set the id if the save was successful
        if(id > 0)
            setId(id);

        return getId();
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

    /**
     * Clears all data from the classes table
     * @param database used to clear table
     * @param clearDrafts boolean if you want to include drafts in the clear
     */
    public static void clearTable(Database database, boolean clearDrafts)
    {
        database.clearTable(TABLE_NAME, clearDrafts);
    }

    /**
     * Returns arraylist of pit cards with specified filters from database
     * @param event if specified, filters pit cards by event id
     * @param team if specified, filters pit cards by team id
     * @param pitCard if specified, filters pit cards by pitCard id
     * @param onlyDrafts if true, filters pit cards by draft
     * @param database used to load pit cards
     * @return arraylist of teams
     */
    public static ArrayList<PitCard> getPitCards(@Nullable Event event, @Nullable Team team, @Nullable PitCard pitCard, boolean onlyDrafts, @NonNull Database database)
    {
        return database.getPitCards(event, team, pitCard, onlyDrafts);
    }

    //endregion
}

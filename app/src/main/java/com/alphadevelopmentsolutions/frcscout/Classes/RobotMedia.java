package com.alphadevelopmentsolutions.frcscout.Classes;

public class RobotMedia
{

    public static final String TABLE_NAME = "robot_media";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_FILE_URI = "FileURI";
    public static final String COLUMN_NAME_IS_DRAFT = "IsDraft";

    private int id;
    private int teamId;
    private String fileUri;
    private boolean isDraft;

    public RobotMedia(
            int id,
            int teamId,
            String fileUri,
            boolean isDraft)
    {
        this.id = id;
        this.teamId = teamId;
        this.fileUri = fileUri;
        this.isDraft = isDraft;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public RobotMedia(int id)
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

    public String getFileUri()
    {
        return fileUri;
    }

    public boolean isDraft()
    {
        return isDraft;
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

    public void setFileUri(String fileUri)
    {
        this.fileUri = fileUri;
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
            RobotMedia robotMedia = database.getRobotMedia(this);


            if (robotMedia != null)
            {
                setTeamId(robotMedia.getTeamId());
                setFileUri(robotMedia.getFileUri());
                setDraft(robotMedia.isDraft());
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
            id = (int) database.setRobotMedia(this);

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
            successful = database.deleteRobotMedia(this);

        }

        return successful;
    }

    //endregion
}

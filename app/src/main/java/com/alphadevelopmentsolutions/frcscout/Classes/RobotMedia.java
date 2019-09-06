package com.alphadevelopmentsolutions.frcscout.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.UUID;

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

    /**
     * Generates a unique file path for a new file
     * @return file with UUID
     */
    public static File generateFileUri()
    {
        File mediaFolder = new File(Constants.MEDIA_DIRECTORY);

        File mediaFile;

        //keep generating UUID's until we found one that does not exist
        //should only run once
        do
        {
            mediaFile = new File(mediaFolder.getAbsolutePath() + "/" + UUID.randomUUID().toString() + ".jpeg");
        }while (mediaFile.isFile());

        return mediaFile;
    }

    /**
     * Converts the current robot media into base64 format for server submission
     * @return base64 bitmap image
     */
    public String getBase64Image()
    {
        File robotMedia = new File(getFileUri());

        if(robotMedia.exists())
        {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            this.getImageBitmap().compress(Bitmap.CompressFormat.JPEG, 15, byteArrayOutputStream);
            byte[] robotMediaBytes = byteArrayOutputStream.toByteArray();

            return Base64.encodeToString(robotMediaBytes, Base64.DEFAULT);
        }


        return null;
    }

    /**
     * Gets a bitmap version of the robot image
     * @return bitmap version of robot image
     */
    public Bitmap getImageBitmap()
    {
        return BitmapFactory.decodeFile(this.getFileUri());
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
        if(!database.isOpen())
            database.open();

        if(database.isOpen())
            id = (int) database.setRobotMedia(this);

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
            successful = database.deleteRobotMedia(this);

        }

        return successful;
    }

    //endregion
}

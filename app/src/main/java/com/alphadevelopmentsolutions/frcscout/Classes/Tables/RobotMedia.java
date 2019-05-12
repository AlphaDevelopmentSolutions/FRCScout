package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

public class RobotMedia extends Table
{

    public static final String TABLE_NAME = "robot_media";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_TEAM_ID = "TeamId";
    public static final String COLUMN_NAME_FILE_URI = "FileURI";
    public static final String COLUMN_NAME_IS_DRAFT = "IsDraft";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_TEAM_ID + " INTEGER," +
                    COLUMN_NAME_FILE_URI + " TEXT," +
                    COLUMN_NAME_IS_DRAFT + " INTEGER)";

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

    @Override
    public String toString()
    {
        return "Team " + getTeamId() + " - Robot Media";
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
            ArrayList<RobotMedia> robotMediaArrayList = getRobotMedia(this, null, false, database);
            RobotMedia robotMedia = (robotMediaArrayList.size() > 0 ) ? robotMediaArrayList.get(0) : null;

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
     * Returns arraylist of robot media with specified filters from database
     * @param robotMedia if specified, filters robot media by robotmedia id
     * @param team if specified, filters robot media by team id
     * @param onlyDrafts if true, filters robot media by draft
     * @param database used to load robot media
     * @return arraylist of robot media
     */
    public static ArrayList<RobotMedia> getRobotMedia(@Nullable RobotMedia robotMedia, @Nullable Team team, boolean onlyDrafts, @NonNull Database database)
    {
        return database.getRobotMedia(robotMedia, team, onlyDrafts);
    }

    //endregion
}

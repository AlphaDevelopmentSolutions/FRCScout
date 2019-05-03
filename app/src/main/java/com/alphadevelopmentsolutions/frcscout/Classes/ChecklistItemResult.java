package com.alphadevelopmentsolutions.frcscout.Classes;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChecklistItemResult
{
    public static final String TABLE_NAME = "checklist_item_results";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_CHECKLIST_ITEM_ID = "ChecklistItemId";
    public static final String COLUMN_NAME_MATCH_ID = "MatchId";
    public static final String COLUMN_NAME_STATUS = "Status";
    public static final String COLUMN_NAME_COMPLETED_BY = "CompletedBy";
    public static final String COLUMN_NAME_COMPLETED_DATE = "CompletedDate";
    public static final String COLUMN_NAME_IS_DRAFT = "IsDraft";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_CHECKLIST_ITEM_ID + " INTEGER," +
                    COLUMN_NAME_MATCH_ID + " TEXT," +
                    COLUMN_NAME_STATUS + " TEXT," +
                    COLUMN_NAME_COMPLETED_BY + " TEXT," +
                    COLUMN_NAME_COMPLETED_DATE + " INTEGER," +
                    COLUMN_NAME_IS_DRAFT + " INTEGER)";

    private int id;
    private int checklistItemId;
    private String matchId;

    private String status;

    private String completedBy;

    private Date completedDate;

    private boolean isDraft;

    public ChecklistItemResult(
            int id,
            int checklistItemId,
            String matchId,

            String status,

            String completedBy,
            Date completedDate,

            boolean isDraft)
    {
        this.id = id;
        this.checklistItemId = checklistItemId;
        this.matchId = matchId;

        this.status = status;
        this.completedBy = completedBy;

        this.completedDate = completedDate;

        this.isDraft = isDraft;
    }

    /**
     * Used for loading
     * @param id to load
     */
    ChecklistItemResult(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public int getChecklistItemId()
    {
        return checklistItemId;
    }

    public String getMatchId()
    {
        return matchId;
    }

    public String getStatus()
    {
        return status;
    }

    public String getCompletedBy()
    {
        return completedBy;
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

        return simpleDateFormat.format(getCompletedDate());
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setChecklistItemId(int checklistItemId)
    {
        this.checklistItemId = checklistItemId;
    }

    public void setMatchId(String matchId)
    {
        this.matchId = matchId;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public void setCompletedBy(String completedBy)
    {
        this.completedBy = completedBy;
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
            ChecklistItemResult checklistItemResult = database.getChecklistItemResult(this);

            if (checklistItemResult != null)
            {
                setChecklistItemId(checklistItemResult.getChecklistItemId());
                setMatchId(checklistItemResult.getMatchId());
                setStatus(checklistItemResult.getStatus());
                setCompletedBy(checklistItemResult.getCompletedBy());
                setCompletedDate(checklistItemResult.getCompletedDate());
                setDraft(checklistItemResult.isDraft());
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
            id = (int) database.setChecklistItemResult(this);

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
            successful = database.deleteChecklistItemResult(this);
        }

        return successful;
    }

    //endregion
}

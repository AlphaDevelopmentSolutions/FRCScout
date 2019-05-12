package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.util.ArrayList;

public class ChecklistItem extends Table
{

    public static final String TABLE_NAME = "checklist_items";
    public static final String COLUMN_NAME_ID = "LocalId";
    public static final String COLUMN_NAME_SERVER_ID = "Id";
    public static final String COLUMN_NAME_TITLE = "Title";
    public static final String COLUMN_NAME_DESCRIPTION = "Description";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_SERVER_ID + " INTEGER," +
                    COLUMN_NAME_TITLE + " TEXT," +
                    COLUMN_NAME_DESCRIPTION + " TEXT)";

    private int id;
    private int serverId;

    private String title;
    private String description;

    public ChecklistItem(
            int id,
            int serverId,

            String title,
            String description)
    {
        super();
        this.id = id;
        this.serverId = serverId;

        this.title = title;
        this.description = description;
    }

    /**
     * Used for loading
     * @param id to load
     */
    ChecklistItem(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public int getServerId()
    {
        return serverId;
    }

    public String getTitle()
    {
        return title;
    }

    public String getDescription()
    {
        return description;
    }

    /**
     * Gets the checklist item results from the database, sorted from newest to oldest
     * @param checklistItemResult if specified, filters checklist item results by checklist item result id
     * @param database used to load object
     * @param onlyDrafts if true, filters by draft
     * @return arraylist of results
     */
    public ArrayList<ChecklistItemResult> getResults(@Nullable ChecklistItemResult checklistItemResult, boolean onlyDrafts, @NonNull Database database)
    {
        //get results from database
        return ChecklistItemResult.getChecklistItemResults(this, checklistItemResult, onlyDrafts, database);
    }

    @Override
    public String toString()
    {
        return getTitle();
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setServerId(int serverId)
    {
        this.serverId = serverId;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setDescription(String description)
    {
        this.description = description;
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
            ArrayList<ChecklistItem> checklistItems = getChecklistItems(this, database);
            ChecklistItem checklistItem = (checklistItems.size() > 0 ) ? checklistItems.get(0) : null;

            if (checklistItem != null)
            {
                setServerId(checklistItem.getServerId());
                setTitle(checklistItem.getTitle());
                setDescription(checklistItem.getDescription());
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
            id = (int) database.setChecklistItem(this);

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
            successful = database.deleteChecklistItem(this);
        }

        return successful;
    }

    /**
     * Clears all data from the classes table
     * @param database used to clear table
     */
    public static void clearTable(Database database)
    {
        database.clearTable(TABLE_NAME);
    }

    /**
     * Returns arraylist of checklist items with specified filters from database
     * @param checklistItem if specified, filters checklist items by checklistitem id
     * @param database used to load checklist items
     * @return arraylist of checklist items
     */
    public static ArrayList<ChecklistItem> getChecklistItems(@Nullable ChecklistItem checklistItem, @NonNull Database database)
    {
        return database.getChecklistItems(checklistItem);
    }

    //endregion
}

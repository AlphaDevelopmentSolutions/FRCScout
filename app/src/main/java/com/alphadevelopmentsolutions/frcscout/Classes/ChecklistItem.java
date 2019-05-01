package com.alphadevelopmentsolutions.frcscout.Classes;

import java.util.ArrayList;

public class ChecklistItem
{

    public static final String TABLE_NAME = "checklist_items";
    public static final String COLUMN_NAME_ID = "LocalId";
    public static final String COLUMN_NAME_SERVER_ID = "Id";
    public static final String COLUMN_NAME_TITLE = "Title";
    public static final String COLUMN_NAME_DESCRIPTION = "Description";

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
     * @param database used to load object
     * @return arraylist of results
     */
    public ArrayList<ChecklistItemResult> getResults(Database database)
    {
        //get results from database
        return database.getChecklistItemResults(this);
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
            ChecklistItem checklistItem = database.getChecklistItem(this);

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

    //endregion
}

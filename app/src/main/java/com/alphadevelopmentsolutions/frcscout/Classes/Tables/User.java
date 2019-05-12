package com.alphadevelopmentsolutions.frcscout.Classes.Tables;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Classes.Database;

import java.util.ArrayList;

public class User extends Table
{
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_FIRST_NAME = "FirstName";
    public static final String COLUMN_NAME_LAST_NAME = "LastName";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME +" (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_FIRST_NAME + " TEXT," +
                    COLUMN_NAME_LAST_NAME + " TEXT)";

    private int id;
    private String firstName;
    private String lastName;

    public User(
            int id,
            String firstName,
            String lastName)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Used for loading
     * @param id to load
     */
    User(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }


    public String getFirstName()
    {
        return firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public String toString()
    {
        return getFirstName() + " " + getLastName();
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
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
            ArrayList<User> users = getUsers(this, database);
            User user = (users.size() > 0 ) ? users.get(0) : null;

            if (user != null)
            {
                setFirstName(user.getFirstName());
                setLastName(user.getLastName());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the object into the database
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
            id = (int) database.setUser(this);

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
            successful = database.deleteUser(this);

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
     * Returns arraylist of users with specified filters from database
     * @param user if specified, filters users by user id
     * @param database used to load users
     * @return arraylist of users
     */
    public static ArrayList<User> getUsers(@Nullable User user, @NonNull Database database)
    {
        return database.getUsers(user);
    }

    //endregion
}

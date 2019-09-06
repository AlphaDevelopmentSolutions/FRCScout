package com.alphadevelopmentsolutions.frcscout.Classes;

public class User
{
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_FIRST_NAME = "FirstName";
    public static final String COLUMN_NAME_LAST_NAME = "LastName";

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
            User user = database.getUser(this);


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

    //endregion
}

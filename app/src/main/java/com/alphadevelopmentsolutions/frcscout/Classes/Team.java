package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;

public class Team
{
    private int id;
    private String name;
    private int number;
    private String city;
    private String stateProvince;
    private String country;
    private int rookieYear;
    private String website;

    Team(
        int id,
        String name,
        int number,
        String city,
        String stateProvince,
        String country,
        int rookieYear,
        String website)
    {
        this.id = id;
        this.name = name;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
        this.rookieYear = rookieYear;
        this.website = website;
    }

    Team(int id)
    {
        this.id = id;
    }

    //region Getters

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getNumber()
    {
        return number;
    }

    public String getCity()
    {
        return city;
    }

    public String getStateProvince()
    {
        return stateProvince;
    }

    public String getCountry()
    {
        return country;
    }

    public int getRookieYear()
    {
        return rookieYear;
    }

    public String getWebsite()
    {
        return website;
    }

    //endregion

    //region Setters

    public void setId(int id)
    {
        this.id = id;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setNumber(int number)
    {
        this.number = number;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setStateProvince(String stateProvince)
    {
        this.stateProvince = stateProvince;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public void setRookieYear(int rookieYear)
    {
        this.rookieYear = rookieYear;
    }

    public void setWebsite(String website)
    {
        this.website = website;
    }

    //endregion

    //region Load, Save & Delete

    /**
     * Loads the team from the database and populates all values
     * @param context used for opening the DB
     * @return boolean if successful
     */
    public boolean load(Context context)
    {
        Database database = new Database(context);
        if(database.open())
        {
            Team team = database.getTeam(this);
            database.close();

            if (team != null)
            {
                setName(team.getName());
                setNumber(team.getNumber());
                setCity(team.getCity());
                setStateProvince(team.getStateProvince());
                setCountry(team.getCountry());
                setRookieYear(team.getRookieYear());
                setWebsite(team.getWebsite());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the team into the database
     * @param context used for opening the DB
     * @return boolean if successful
     */
    public boolean save(Context context)
    {
        boolean successful = false;
        Database database = new Database(context);

        if(database.open())
        {
            successful = database.setTeam(this);
            database.close();
        }

        return successful;
    }

    /**
     * Deletes the team from the database
     * @param context used for opening the DB
     * @return boolean if successful
     */
    public boolean delete(Context context)
    {
        boolean successful = false;
        Database database = new Database(context);

        if(database.open())
        {
            successful = database.deleteTeam(this);
            database.close();
        }

        return successful;
    }

    //endregion
}

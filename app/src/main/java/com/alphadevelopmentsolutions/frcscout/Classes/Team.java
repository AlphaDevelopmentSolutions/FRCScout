package com.alphadevelopmentsolutions.frcscout.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Team
{

    public static final String TABLE_NAME = "teams";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_NAME = "Name";
    public static final String COLUMN_NAME_NUMBER = "Number";
    public static final String COLUMN_NAME_CITY = "City";
    public static final String COLUMN_NAME_STATEPROVINCE = "StateProvince";
    public static final String COLUMN_NAME_COUNTRY = "Country";
    public static final String COLUMN_NAME_ROOKIE_YEAR = "RookieYear";
    public static final String COLUMN_NAME_WEBSITE = "Website";
    public static final String COLUMN_NAME_IMAGE_FILE_URI = "ImageFileURI";

    private int id;
    private String name;
    private int number;
    private String city;
    private String stateProvince;
    private String country;
    private int rookieYear;
    private String website;
    private String imageFileURI;

    public Team(
            int id,
            String name,
            int number,
            String city,
            String stateProvince,
            String country,
            int rookieYear,
            String website,
            String imageFileURI)
    {
        this.id = id;
        this.name = name;
        this.number = number;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
        this.rookieYear = rookieYear;
        this.website = website;
        this.imageFileURI = imageFileURI;
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

    public String getImageFileURI()
    {
        return imageFileURI;
    }

    /**
     * Returns the bitmap from the specified file location
     * @return null if no image found, bitmap if image found
     */
    public Bitmap getImageBitmap()
    {
        File file = new File(getImageFileURI());

        //check if the image exists
        if(file.exists()) return BitmapFactory.decodeFile(file.getAbsolutePath());

        return null;
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

    public void setImageFileURI(String imageFileURI)
    {
        this.imageFileURI = imageFileURI;
    }

    //endregion

    //region Load, Save & Delete

    /**
     * Loads the team from the database and populates all values
     * @param database used for interacting with the SQLITE db
     * @return boolean if successful
     */
    public boolean load(Database database)
    {
        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
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
                setImageFileURI(team.getImageFileURI());
                return true;
            }
        }

        return false;
    }

    /**
     * Saves the team into the database
     * @param database used for interacting with the SQLITE db
     * @return int id of the saved team
     */
    public int save(Database database)
    {
        int id = -1;

        //try to open the DB if it is not open
        if(!database.isOpen()) database.open();

        if(database.isOpen())
        {
            id = (int) database.setTeam(this);
            database.close();
        }

        return id;
    }

    /**
     * Deletes the team from the database
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
            successful = database.deleteTeam(this);
            database.close();
        }

        return successful;
    }

    //endregion
}

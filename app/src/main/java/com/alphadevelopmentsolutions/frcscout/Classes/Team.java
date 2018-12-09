package com.alphadevelopmentsolutions.frcscout.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;

public class Team
{

    public static final String TABLE_NAME = "teams";
    public static final String COLUMN_NAME_ID = "Id";
    public static final String COLUMN_NAME_NAME = "Name";
    public static final String COLUMN_NAME_CITY = "City";
    public static final String COLUMN_NAME_STATEPROVINCE = "StateProvince";
    public static final String COLUMN_NAME_COUNTRY = "Country";
    public static final String COLUMN_NAME_ROOKIE_YEAR = "RookieYear";
    public static final String COLUMN_NAME_FACEBOOK_URL = "FacebookURL";
    public static final String COLUMN_NAME_TWITTER_URL = "TwitterURL";
    public static final String COLUMN_NAME_INSTAGRAM_URL = "InstagramURL";
    public static final String COLUMN_NAME_YOUTUBE_URL = "YoutubeURL";
    public static final String COLUMN_NAME_WEBSITE_URL = "WebsiteURL";
    public static final String COLUMN_NAME_IMAGE_FILE_URI = "ImageFileURI";

    private int id;
    private String name;
    private String city;
    private String stateProvince;
    private String country;
    private int rookieYear;
    private String facebookURL;
    private String twitterURL;
    private String instagramURL;
    private String youtubeURL;
    private String websiteURL;
    private String imageFileURI;

    public Team(
            int id,
            String name,
            String city,
            String stateProvince,
            String country,
            int rookieYear,
            String facebookURL,
            String twitterURL,
            String instagramURL,
            String youtubeURL,
            String websiteURL,
            String imageFileURI)
    {
        this.id = id;
        this.name = name;
        this.city = city;
        this.stateProvince = stateProvince;
        this.country = country;
        this.rookieYear = rookieYear;
        this.facebookURL = facebookURL;
        this.twitterURL = twitterURL;
        this.instagramURL = instagramURL;
        this.youtubeURL = youtubeURL;
        this.websiteURL = websiteURL;
        this.imageFileURI = imageFileURI;
    }

    /**
     * Used for loading
     * @param id to load
     */
    public Team(int id)
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

    public String getFacebookURL()
    {
        return facebookURL;
    }

    public String getTwitterURL()
    {
        return twitterURL;
    }

    public String getInstagramURL()
    {
        return instagramURL;
    }

    public String getYoutubeURL()
    {
        return youtubeURL;
    }

    public String getWebsiteURL()
    {
        return websiteURL;
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

    public void setFacebookURL(String facebookURL)
    {
        this.facebookURL = facebookURL;
    }

    public void setTwitterURL(String twitterURL)
    {
        this.twitterURL = twitterURL;
    }

    public void setInstagramURL(String instagramURL)
    {
        this.instagramURL = instagramURL;
    }

    public void setYoutubeURL(String youtubeURL)
    {
        this.youtubeURL = youtubeURL;
    }

    public void setWebsiteURL(String websiteURL)
    {
        this.websiteURL = websiteURL;
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
                setCity(team.getCity());
                setStateProvince(team.getStateProvince());
                setCountry(team.getCountry());
                setRookieYear(team.getRookieYear());
                setFacebookURL(team.getFacebookURL());
                setTwitterURL(team.getTwitterURL());
                setInstagramURL(team.getInstagramURL());
                setYoutubeURL(team.getYoutubeURL());
                setWebsiteURL(team.getWebsiteURL());
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

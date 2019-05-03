package com.alphadevelopmentsolutions.frcscout.Classes;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import com.alphadevelopmentsolutions.frcscout.Interfaces.StatsKeys;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_CITY + " TEXT," +
                    COLUMN_NAME_STATEPROVINCE + " TEXT," +
                    COLUMN_NAME_COUNTRY + " TEXT," +
                    COLUMN_NAME_ROOKIE_YEAR + " INTEGER," +
                    COLUMN_NAME_FACEBOOK_URL + " TEXT," +
                    COLUMN_NAME_TWITTER_URL + " TEXT," +
                    COLUMN_NAME_INSTAGRAM_URL + " TEXT," +
                    COLUMN_NAME_YOUTUBE_URL + " TEXT," +
                    COLUMN_NAME_WEBSITE_URL + " TEXT," +
                    COLUMN_NAME_IMAGE_FILE_URI + " TEXT)";

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
     * @param database used to load
     */
    public Team(int id, Database database)
    {
        this.id = id;

        load(database);
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
        if(file.exists())
            return BitmapFactory.decodeFile(file.getAbsolutePath());

        return null;
    }

    /**
     * Calculates all stats for the QuickStatsFragment
     * Highly recommended that this gets ran inside it's own thread is it
     * will take a long time to process
     * @param database used to pull data
     * @return hashmap of stats
     */
    public HashMap<String, HashMap<String, Double>> getStats(Database database, Event event)
    {
        HashMap<String, HashMap<String, Double>> stats = new HashMap<>();
        HashMap<String, Double> minStats = new HashMap<>();
        HashMap<String, Double> avgStats = new HashMap<>();
        HashMap<String, Double> maxStats = new HashMap<>();
        
        //pre-populate min stats
        minStats.put(StatsKeys.AUTO_EXIT_HAB, 1000.0);
        minStats.put(StatsKeys.AUTO_HATCH, 1000.0);
        minStats.put(StatsKeys.AUTO_HATCH_DROPPED, 1000.0);
        minStats.put(StatsKeys.AUTO_CARGO, 1000.0);
        minStats.put(StatsKeys.AUTO_CARGO_DROPPED, 1000.0);
        minStats.put(StatsKeys.TELEOP_HATCH, 1000.0);
        minStats.put(StatsKeys.TELEOP_HATCH_DROPPED, 1000.0);
        minStats.put(StatsKeys.TELEOP_CARGO, 1000.0);
        minStats.put(StatsKeys.TELEOP_CARGO_DROPPED, 1000.0);
        minStats.put(StatsKeys.RETURNED_HAB, 1000.0);
        minStats.put(StatsKeys.RETURNED_HAB_FAILS, 1000.0);
        minStats.put(StatsKeys.DEFENSE_RATING, 1000.0);
        minStats.put(StatsKeys.OFFENSE_RATING, 1000.0);
        minStats.put(StatsKeys.DRIVE_RATING, 1000.0);

        //pre-populate avg stats
        avgStats.put(StatsKeys.AUTO_EXIT_HAB, 0.0);
        avgStats.put(StatsKeys.AUTO_HATCH, 0.0);
        avgStats.put(StatsKeys.AUTO_HATCH_DROPPED, 0.0);
        avgStats.put(StatsKeys.AUTO_CARGO, 0.0);
        avgStats.put(StatsKeys.AUTO_CARGO_DROPPED, 0.0);
        avgStats.put(StatsKeys.TELEOP_HATCH, 0.0);
        avgStats.put(StatsKeys.TELEOP_HATCH_DROPPED, 0.0);
        avgStats.put(StatsKeys.TELEOP_CARGO, 0.0);
        avgStats.put(StatsKeys.TELEOP_CARGO_DROPPED, 0.0);
        avgStats.put(StatsKeys.RETURNED_HAB, 0.0);
        avgStats.put(StatsKeys.RETURNED_HAB_FAILS, 0.0);
        avgStats.put(StatsKeys.DEFENSE_RATING, 0.0);
        avgStats.put(StatsKeys.OFFENSE_RATING, 0.0);
        avgStats.put(StatsKeys.DRIVE_RATING, 0.0);

        //pre-populate max stats
        maxStats.put(StatsKeys.AUTO_EXIT_HAB, 0.0);
        maxStats.put(StatsKeys.AUTO_HATCH, 0.0);
        maxStats.put(StatsKeys.AUTO_HATCH_DROPPED, 0.0);
        maxStats.put(StatsKeys.AUTO_CARGO, 0.0);
        maxStats.put(StatsKeys.AUTO_CARGO_DROPPED, 0.0);
        maxStats.put(StatsKeys.TELEOP_HATCH, 0.0);
        maxStats.put(StatsKeys.TELEOP_HATCH_DROPPED, 0.0);
        maxStats.put(StatsKeys.TELEOP_CARGO, 0.0);
        maxStats.put(StatsKeys.TELEOP_CARGO_DROPPED, 0.0);
        maxStats.put(StatsKeys.RETURNED_HAB, 0.0);
        maxStats.put(StatsKeys.RETURNED_HAB_FAILS, 0.0);
        maxStats.put(StatsKeys.DEFENSE_RATING, 0.0);
        maxStats.put(StatsKeys.OFFENSE_RATING, 0.0);
        maxStats.put(StatsKeys.DRIVE_RATING, 0.0);


        //get all scout cards from the database
        ArrayList<ScoutCard> scoutCards = getScoutCards(event, null, false, database);

        //store iterations for avg
        int i = 0;

        int nulledDefenseRatings = 0;
        int nulledOffenseRatings = 0;
        
        for(ScoutCard scoutCard : scoutCards)
        {
            //calculate min
            
            //if they exited, check the level
            if(scoutCard.getAutonomousExitHabitat())
            {
                if (scoutCard.getPreGameStartingLevel() < minStats.get(StatsKeys.AUTO_EXIT_HAB))
                    minStats.put(StatsKeys.AUTO_EXIT_HAB, (double) scoutCard.getPreGameStartingLevel());
            }
            
            //if they didn't exit, set to 0
            else if(0 < minStats.get(StatsKeys.AUTO_EXIT_HAB))
                minStats.put(StatsKeys.AUTO_EXIT_HAB, 0.0);
            
            if (scoutCard.getAutonomousHatchPanelsSecured() < minStats.get(StatsKeys.AUTO_HATCH))
                minStats.put(StatsKeys.AUTO_HATCH, (double) scoutCard.getAutonomousHatchPanelsSecured());

            if (scoutCard.getAutonomousHatchPanelsSecuredAttempts() < minStats.get(StatsKeys.AUTO_HATCH_DROPPED))
                minStats.put(StatsKeys.AUTO_HATCH_DROPPED, (double) scoutCard.getAutonomousHatchPanelsSecuredAttempts());

            if (scoutCard.getAutonomousCargoStored() < minStats.get(StatsKeys.AUTO_CARGO))
                minStats.put(StatsKeys.AUTO_CARGO, (double) scoutCard.getAutonomousCargoStored());

            if (scoutCard.getAutonomousCargoStoredAttempts() < minStats.get(StatsKeys.AUTO_CARGO_DROPPED))
                minStats.put(StatsKeys.AUTO_CARGO_DROPPED, (double) scoutCard.getAutonomousCargoStoredAttempts());

            if (scoutCard.getTeleopHatchPanelsSecured() < minStats.get(StatsKeys.TELEOP_HATCH))
                minStats.put(StatsKeys.TELEOP_HATCH, (double) scoutCard.getTeleopHatchPanelsSecured());

            if (scoutCard.getTeleopHatchPanelsSecuredAttempts() < minStats.get(StatsKeys.TELEOP_HATCH_DROPPED))
                minStats.put(StatsKeys.TELEOP_HATCH_DROPPED, (double) scoutCard.getTeleopHatchPanelsSecuredAttempts());

            if (scoutCard.getTeleopCargoStored() < minStats.get(StatsKeys.TELEOP_CARGO))
                minStats.put(StatsKeys.TELEOP_CARGO, (double) scoutCard.getTeleopCargoStored());

            if (scoutCard.getTeleopCargoStoredAttempts() < minStats.get(StatsKeys.TELEOP_CARGO_DROPPED))
                minStats.put(StatsKeys.TELEOP_CARGO_DROPPED, (double) scoutCard.getTeleopCargoStoredAttempts());

            if (scoutCard.getEndGameReturnedToHabitat() < minStats.get(StatsKeys.RETURNED_HAB))
                minStats.put(StatsKeys.RETURNED_HAB, (double) scoutCard.getEndGameReturnedToHabitat());

            if (scoutCard.getEndGameReturnedToHabitatAttempts() < minStats.get(StatsKeys.RETURNED_HAB_FAILS))
                minStats.put(StatsKeys.RETURNED_HAB_FAILS, (double) scoutCard.getEndGameReturnedToHabitatAttempts());

            if (scoutCard.getDefenseRating() != 0 && scoutCard.getDefenseRating() < minStats.get(StatsKeys.DEFENSE_RATING))
                minStats.put(StatsKeys.DEFENSE_RATING, (double) scoutCard.getDefenseRating());

            if (scoutCard.getOffenseRating() != 0 && scoutCard.getOffenseRating() < minStats.get(StatsKeys.OFFENSE_RATING))
                minStats.put(StatsKeys.OFFENSE_RATING, (double) scoutCard.getOffenseRating());

            if (scoutCard.getDriveRating() < minStats.get(StatsKeys.DRIVE_RATING))
                minStats.put(StatsKeys.DRIVE_RATING, (double) scoutCard.getDriveRating());
            
            //calculate avg
            if(scoutCard.getAutonomousExitHabitat())
                avgStats.put(StatsKeys.AUTO_EXIT_HAB, avgStats.get(StatsKeys.AUTO_EXIT_HAB) + scoutCard.getPreGameStartingLevel());
            avgStats.put(StatsKeys.AUTO_HATCH, avgStats.get(StatsKeys.AUTO_HATCH) + scoutCard.getAutonomousHatchPanelsSecured());
            avgStats.put(StatsKeys.AUTO_HATCH_DROPPED, avgStats.get(StatsKeys.AUTO_HATCH_DROPPED) + scoutCard.getAutonomousHatchPanelsSecuredAttempts());
            avgStats.put(StatsKeys.AUTO_CARGO, avgStats.get(StatsKeys.AUTO_CARGO) + scoutCard.getAutonomousCargoStored());
            avgStats.put(StatsKeys.AUTO_CARGO_DROPPED, avgStats.get(StatsKeys.AUTO_CARGO_DROPPED) + scoutCard.getAutonomousCargoStoredAttempts());

            avgStats.put(StatsKeys.TELEOP_HATCH, avgStats.get(StatsKeys.TELEOP_HATCH) + scoutCard.getTeleopHatchPanelsSecured());
            avgStats.put(StatsKeys.TELEOP_HATCH_DROPPED, avgStats.get(StatsKeys.TELEOP_HATCH_DROPPED) + scoutCard.getTeleopHatchPanelsSecuredAttempts());
            avgStats.put(StatsKeys.TELEOP_CARGO, avgStats.get(StatsKeys.TELEOP_CARGO) + scoutCard.getTeleopCargoStored());
            avgStats.put(StatsKeys.TELEOP_CARGO_DROPPED, avgStats.get(StatsKeys.TELEOP_CARGO_DROPPED) + scoutCard.getTeleopCargoStoredAttempts());

            avgStats.put(StatsKeys.RETURNED_HAB, avgStats.get(StatsKeys.RETURNED_HAB) + scoutCard.getEndGameReturnedToHabitat());
            avgStats.put(StatsKeys.RETURNED_HAB_FAILS, avgStats.get(StatsKeys.RETURNED_HAB_FAILS) + scoutCard.getEndGameReturnedToHabitatAttempts());

            avgStats.put(StatsKeys.DEFENSE_RATING, avgStats.get(StatsKeys.DEFENSE_RATING) + scoutCard.getDefenseRating());
            avgStats.put(StatsKeys.OFFENSE_RATING, avgStats.get(StatsKeys.OFFENSE_RATING) + scoutCard.getOffenseRating());
            avgStats.put(StatsKeys.DRIVE_RATING, avgStats.get(StatsKeys.DRIVE_RATING) + scoutCard.getDriveRating());

            nulledDefenseRatings = scoutCard.getDefenseRating() == 0 ? nulledDefenseRatings + 1 : nulledDefenseRatings;
            nulledOffenseRatings = scoutCard.getOffenseRating() == 0 ? nulledOffenseRatings + 1 : nulledOffenseRatings;

            i++;
            
            //calculate max

            //if they exited, check the level
            if(scoutCard.getAutonomousExitHabitat())
            {
                if (scoutCard.getPreGameStartingLevel() > maxStats.get(StatsKeys.AUTO_EXIT_HAB))
                    maxStats.put(StatsKeys.AUTO_EXIT_HAB, (double) scoutCard.getPreGameStartingLevel());
            }

            //if they didn't exit, set to 0
            else if(0 > maxStats.get(StatsKeys.AUTO_EXIT_HAB))
                maxStats.put(StatsKeys.AUTO_EXIT_HAB, (double) scoutCard.getPreGameStartingLevel());

            if (scoutCard.getAutonomousHatchPanelsSecured() > maxStats.get(StatsKeys.AUTO_HATCH))
                maxStats.put(StatsKeys.AUTO_HATCH, (double) scoutCard.getAutonomousHatchPanelsSecured());

            if (scoutCard.getAutonomousHatchPanelsSecuredAttempts() > maxStats.get(StatsKeys.AUTO_HATCH_DROPPED))
                maxStats.put(StatsKeys.AUTO_HATCH_DROPPED, (double) scoutCard.getAutonomousHatchPanelsSecuredAttempts());

            if (scoutCard.getAutonomousCargoStored() > maxStats.get(StatsKeys.AUTO_CARGO))
                maxStats.put(StatsKeys.AUTO_CARGO, (double) scoutCard.getAutonomousCargoStored());

            if (scoutCard.getAutonomousCargoStoredAttempts() > maxStats.get(StatsKeys.AUTO_CARGO_DROPPED))
                maxStats.put(StatsKeys.AUTO_CARGO_DROPPED, (double) scoutCard.getAutonomousCargoStoredAttempts());

            if (scoutCard.getTeleopHatchPanelsSecured() > maxStats.get(StatsKeys.TELEOP_HATCH))
                maxStats.put(StatsKeys.TELEOP_HATCH, (double) scoutCard.getTeleopHatchPanelsSecured());

            if (scoutCard.getTeleopHatchPanelsSecuredAttempts() > maxStats.get(StatsKeys.TELEOP_HATCH_DROPPED))
                maxStats.put(StatsKeys.TELEOP_HATCH_DROPPED, (double) scoutCard.getTeleopHatchPanelsSecuredAttempts());

            if (scoutCard.getTeleopCargoStored() > maxStats.get(StatsKeys.TELEOP_CARGO))
                maxStats.put(StatsKeys.TELEOP_CARGO, (double) scoutCard.getTeleopCargoStored());

            if (scoutCard.getTeleopCargoStoredAttempts() > maxStats.get(StatsKeys.TELEOP_CARGO_DROPPED))
                maxStats.put(StatsKeys.TELEOP_CARGO_DROPPED, (double) scoutCard.getTeleopCargoStoredAttempts());

            if (scoutCard.getEndGameReturnedToHabitat() > maxStats.get(StatsKeys.RETURNED_HAB))
                maxStats.put(StatsKeys.RETURNED_HAB, (double) scoutCard.getEndGameReturnedToHabitat());

            if (scoutCard.getEndGameReturnedToHabitatAttempts() > maxStats.get(StatsKeys.RETURNED_HAB_FAILS))
                maxStats.put(StatsKeys.RETURNED_HAB_FAILS, (double) scoutCard.getEndGameReturnedToHabitatAttempts());

            if (scoutCard.getDefenseRating() > maxStats.get(StatsKeys.DEFENSE_RATING))
                maxStats.put(StatsKeys.DEFENSE_RATING, (double) scoutCard.getDefenseRating());

            if (scoutCard.getOffenseRating() > maxStats.get(StatsKeys.OFFENSE_RATING))
                maxStats.put(StatsKeys.OFFENSE_RATING, (double) scoutCard.getOffenseRating());

            if (scoutCard.getDriveRating() > maxStats.get(StatsKeys.DRIVE_RATING))
                maxStats.put(StatsKeys.DRIVE_RATING, (double) scoutCard.getDriveRating());
        }

        //once iterations have ended, do a final calculation for avg
        //calculate avg
        avgStats.put(StatsKeys.AUTO_EXIT_HAB, avgStats.get(StatsKeys.AUTO_EXIT_HAB) / i);
        avgStats.put(StatsKeys.AUTO_HATCH, avgStats.get(StatsKeys.AUTO_HATCH) / i );
        avgStats.put(StatsKeys.AUTO_HATCH_DROPPED, avgStats.get(StatsKeys.AUTO_HATCH_DROPPED) / i );
        avgStats.put(StatsKeys.AUTO_CARGO, avgStats.get(StatsKeys.AUTO_CARGO) / i );
        avgStats.put(StatsKeys.AUTO_CARGO_DROPPED, avgStats.get(StatsKeys.AUTO_CARGO_DROPPED) / i );

        avgStats.put(StatsKeys.TELEOP_HATCH, avgStats.get(StatsKeys.TELEOP_HATCH) / i );
        avgStats.put(StatsKeys.TELEOP_HATCH_DROPPED, avgStats.get(StatsKeys.TELEOP_HATCH_DROPPED) / i );
        avgStats.put(StatsKeys.TELEOP_CARGO, avgStats.get(StatsKeys.TELEOP_CARGO) / i );
        avgStats.put(StatsKeys.TELEOP_CARGO_DROPPED, avgStats.get(StatsKeys.TELEOP_CARGO_DROPPED) / i );

        avgStats.put(StatsKeys.RETURNED_HAB, avgStats.get(StatsKeys.RETURNED_HAB) / i );
        avgStats.put(StatsKeys.RETURNED_HAB_FAILS, avgStats.get(StatsKeys.RETURNED_HAB_FAILS) / i );

        avgStats.put(StatsKeys.DEFENSE_RATING, avgStats.get(StatsKeys.DEFENSE_RATING) / (i - nulledDefenseRatings) );
        avgStats.put(StatsKeys.OFFENSE_RATING, avgStats.get(StatsKeys.OFFENSE_RATING) / (i - nulledOffenseRatings) );
        avgStats.put(StatsKeys.DRIVE_RATING, avgStats.get(StatsKeys.DRIVE_RATING) / i );

        //verify the min stats are not still at default 1000
        for(Map.Entry<String, Double> minStat : minStats.entrySet())
        {
            if(minStat.getValue() > 900)
                minStats.put(minStat.getKey(), 0.0);
        }
        
        
        stats.put(StatsKeys.MIN, minStats);
        stats.put(StatsKeys.AVG, avgStats);
        stats.put(StatsKeys.MAX, maxStats);

        return stats;
    }

    /**
     * Gets all scout cards associated with the team
     * @param database used for loading cards
     * @param onlyDrafts boolean if you only want drafts
     * @return arraylist of scout cards
     */
    public ArrayList<ScoutCard> getScoutCards(@Nullable Event event, @Nullable Match match, boolean onlyDrafts, Database database)
    {
        return database.getScoutCards(event, match, this, onlyDrafts);
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
        if(!database.isOpen())
            database.open();

        if(database.isOpen())
            id = (int) database.setTeam(this);

        //set the id if the save was successful
        if(id > 0)
            setId(id);

        return getId();
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

        }

        return successful;
    }

    //endregion
}

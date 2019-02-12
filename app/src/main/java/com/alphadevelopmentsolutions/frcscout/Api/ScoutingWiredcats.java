package com.alphadevelopmentsolutions.frcscout.Api;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class ScoutingWiredcats extends Api
{
    private static String URL = "http://scouting.wiredcats5885.ca/api/api.php";

    ScoutingWiredcats(String URLExtension, HashMap<String, String> postData)
    {
        super(URL + URLExtension, postData);
    }

    /**
     * Action within the SOPModuleApi file for getting SOPs
     */
    public static class GetTeamsAtEvent extends ScoutingWiredcats
    {
        private ArrayList<Team> teams;

        private final String API_FIELD_NAME_TEAM_ID = "Id";
        private final String API_FIELD_NAME_TEAM_NAME = "Namr";
        private final String API_FIELD_NAME_TEAM_CITY = "City";
        private final String API_FIELD_NAME_TEAM_STATE_PROVINCE = "StateProvince";
        private final String API_FIELD_NAME_TEAM_COUNTRY = "Country";
        private final String API_FIELD_NAME_TEAM_ROOKIE_YEAR = "RookieYear";
        private final String API_FIELD_NAME_TEAM_FACEBOOK_URL = "FacebookURL";
        private final String API_FIELD_NAME_TEAM_TWITTER_URL = "TwitterURL";
        private final String API_FIELD_NAME_TEAM_INSTAGRAM_URL = "InstagramURL";
        private final String API_FIELD_NAME_TEAM_YOUTUBE_URL = "YoutubeURL";
        private final String API_FIELD_NAME_TEAM_WEBSITE_URL = "WebsiteURL";
        private final String API_FIELD_NAME_TEAM_IMAGE_FILE_URL = "TODO";

        private MainActivity context;

        public GetTeamsAtEvent(final MainActivity context, final String event)
        {
            super("", new HashMap<String, String>()
            {{
                put("EventId", event);
            }});

            teams = new ArrayList<>();

            this.context = context;

        }

        @Override
        public boolean execute()
        {
            try
            {
                //parse the data from the server
                ApiParser apiParser = new ApiParser(this);

                //get the response from the server
                JSONArray response = apiParser.parse();

                //could not connect to server
                if (response == null)
                    throw new Exception("Could not connect to the web server.");

                if(!response.getString(0).toLowerCase().equals("success"))
                    throw new Exception(response.getString(1));


                //iterate through, create a new object and add it to the arraylist
                for(int i = 0; i < response.getJSONArray(1).length(); i++)
                {
                    JSONObject teamObject = response.getJSONArray(1).getJSONObject(i);

                    int teamId = teamObject.getInt(API_FIELD_NAME_TEAM_ID);
                    String name = teamObject.getString(API_FIELD_NAME_TEAM_NAME);
                    String city = teamObject.getString(API_FIELD_NAME_TEAM_CITY);
                    String stateProvince = teamObject.getString(API_FIELD_NAME_TEAM_STATE_PROVINCE);
                    String country = teamObject.getString(API_FIELD_NAME_TEAM_COUNTRY);
                    int rookieYear = teamObject.getInt(API_FIELD_NAME_TEAM_ROOKIE_YEAR);
                    String facebookURL = teamObject.getString(API_FIELD_NAME_TEAM_FACEBOOK_URL);
                    String twitterURL = teamObject.getString(API_FIELD_NAME_TEAM_TWITTER_URL);
                    String instagramURL = teamObject.getString(API_FIELD_NAME_TEAM_INSTAGRAM_URL);
                    String youtubeURL = teamObject.getString(API_FIELD_NAME_TEAM_YOUTUBE_URL);
                    String websiteUrl = teamObject.getString(API_FIELD_NAME_TEAM_WEBSITE_URL);

                    teams.add(new Team(
                            teamId,
                            name,
                            city,
                            stateProvince,
                            country,
                            rookieYear,
                            "https://www.facebook.com/" + facebookURL,
                            "https://www.twitter.com/" + twitterURL,
                            "https://www.instagram.com/" + instagramURL,
                            "https://www.youtube.com/" + youtubeURL,
                            websiteUrl,
                            ""));
                }

                return true;
            }
            catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }

        //region Getters

        public ArrayList<Team> getTeams()
        {
            return teams;
        }


        //endregion
    }

}


package com.alphadevelopmentsolutions.frcscout.Api;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class TheBlueAllianceApi extends Api
{
    private static String URL = "https://www.thebluealliance.com/api/v3/";

    TheBlueAllianceApi(String URLExtension, HashMap<String, String> postData)
    {
        super(URL + URLExtension, postData);
    }

    /**
     * Action within the SOPModuleApi file for getting SOPs
     */
    public static class GetTeamsAtEvent extends TheBlueAllianceApi
    {
        private ArrayList<Team> teams;

        private final String API_FIELD_NAME_TEAM_ID = "team_number";
        private final String API_FIELD_NAME_TEAM_NAME = "nickname";
        private final String API_FIELD_NAME_TEAM_CITY = "city";
        private final String API_FIELD_NAME_TEAM_STATE_PROVINCE = "state_prov";
        private final String API_FIELD_NAME_TEAM_COUNTRY = "country";
        private final String API_FIELD_NAME_TEAM_ROOKIE_YEAR = "rookie_year";
        private final String API_FIELD_NAME_TEAM_FACEBOOK_URL = "TODO";
        private final String API_FIELD_NAME_TEAM_TWITTER_URL = "TODO";
        private final String API_FIELD_NAME_TEAM_INSTAGRAM_URL = "TODO";
        private final String API_FIELD_NAME_TEAM_YOUTUBE_URL = "TODO";
        private final String API_FIELD_NAME_TEAM_WEBSITE_URL = "website";
        private final String API_FIELD_NAME_TEAM_IMAGE_FILE_URL = "TODO";

        private MainActivity context;

        public GetTeamsAtEvent(final MainActivity context, String event)
        {
            super("event/" + event + "/teams", null);

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
                    throw new Exception("Could not connect to the blue alliance.");


                //iterate through, create a new object and add it to the arraylist
                for(int i = 0; i < response.length(); i++)
                {
                    JSONObject teamObject = response.getJSONObject(i);

                    int teamId = teamObject.getInt(API_FIELD_NAME_TEAM_ID);
                    String name = teamObject.getString(API_FIELD_NAME_TEAM_NAME);
                    String city = teamObject.getString(API_FIELD_NAME_TEAM_CITY);
                    String stateProvince = teamObject.getString(API_FIELD_NAME_TEAM_STATE_PROVINCE);
                    String country = teamObject.getString(API_FIELD_NAME_TEAM_COUNTRY);
                    int rookieYear = teamObject.getInt(API_FIELD_NAME_TEAM_ROOKIE_YEAR);
                    String websiteUrl = teamObject.getString(API_FIELD_NAME_TEAM_WEBSITE_URL);

                    teams.add(new Team(teamId, name, city, stateProvince, country, rookieYear, "", "", "", "", websiteUrl, ""));
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


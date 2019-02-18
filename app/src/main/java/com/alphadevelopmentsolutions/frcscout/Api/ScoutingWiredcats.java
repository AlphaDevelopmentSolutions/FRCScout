package com.alphadevelopmentsolutions.frcscout.Api;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class ScoutingWiredcats extends Api
{
    private static String URL = "http://scouting.wiredcats5885.ca/api/api.php";

    ScoutingWiredcats(String URLExtension, HashMap<String, String> postData)
    {
        super(URL + URLExtension, postData);
    }

    public static class GetTeamsAtEvent extends ScoutingWiredcats
    {
        private ArrayList<Team> teams;

        private final String API_FIELD_NAME_TEAM_ID = "Id";
        private final String API_FIELD_NAME_TEAM_NAME = "Name";
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
                put("action", "GetTeamsAtEvent");
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
                JSONObject response = apiParser.parse();

                //could not connect to server
                if (response == null)
                    throw new Exception("Could not connect to the web server.");

                if(!response.getString("Status").toLowerCase().equals("success"))
                    throw new Exception(response.getString("Response"));


                //iterate through, create a new object and add it to the arraylist
                for(int i = 0; i < response.getJSONArray("Response").length(); i++)
                {
                    JSONObject teamObject = response.getJSONArray("Response").getJSONObject(i);

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
                            (facebookURL.equals("null") ? "" : "https://www.facebook.com/" + facebookURL),
                            (twitterURL.equals("null") ? "" : "https://www.twitter.com/" + twitterURL),
                            (instagramURL.equals("null") ? "" : "https://www.instagram.com/" + instagramURL),
                            (youtubeURL.equals("null") ? "" : "https://www.youtube.com/" + youtubeURL),
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

    public static class GetUsers extends ScoutingWiredcats
    {
        private ArrayList<User> users;

        private final String API_FIELD_NAME_FIRST_NAME = "FirstName";
        private final String API_FIELD_NAME_LAST_NAME = "LastName";

        private MainActivity context;

        public GetUsers(final MainActivity context)
        {
            super("", new HashMap<String, String>()
            {{
                put("action", "GetUsers");
            }});

            users = new ArrayList<>();

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
                JSONObject response = apiParser.parse();

                //could not connect to server
                if (response == null)
                    throw new Exception("Could not connect to the web server.");

                if(!response.getString("Status").toLowerCase().equals("success"))
                    throw new Exception(response.getString("Response"));


                //iterate through, create a new object and add it to the arraylist
                for(int i = 0; i < response.getJSONArray("Response").length(); i++)
                {
                    JSONObject teamObject = response.getJSONArray("Response").getJSONObject(i);

                    String firstName = teamObject.getString(API_FIELD_NAME_FIRST_NAME);
                    String lastName = teamObject.getString(API_FIELD_NAME_LAST_NAME);

                    users.add(new User(-1, firstName, lastName));
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

        public ArrayList<User> getUsers()
        {
            return users;
        }


        //endregion
    }

    public static class SubmitScoutCard extends ScoutingWiredcats
    {
        private MainActivity context;

        public SubmitScoutCard(final MainActivity context, final ScoutCard scoutCard)
        {
            super("", new HashMap<String, String>()
            {{
                put("action", "SubmitScoutCard");

                put("MatchId", String.valueOf(scoutCard.getMatchId()));
                put("TeamId", String.valueOf(scoutCard.getTeamId()));
                put("EventId", scoutCard.getEventId());
                put("AllianceColor", scoutCard.getAllianceColor().name());
                put("CompletedBy", scoutCard.getCompletedBy());
                put("BlueAllianceFinalScore", String.valueOf(scoutCard.getBlueAllianceFinalScore()));
                put("RedAllianceFinalScore", String.valueOf(scoutCard.getRedAllianceFinalScore()));
                put("AutonomousExitHabitat", scoutCard.isAutonomousExitHabitat() ? "1" : "0");
                put("AutonomousHatchPanelsSecured", String.valueOf(scoutCard.getAutonomousHatchPanelsSecured()));
                put("AutonomousCargoStored", String.valueOf(scoutCard.getAutonomousCargoStored()));
                put("TeleopHatchPanelsSecured", String.valueOf(scoutCard.getTeleopHatchPanelsSecured()));
                put("TeleopCargoStored", String.valueOf(scoutCard.getTeleopCargoStored()));
                put("TeleopRocketsCompleted", String.valueOf(scoutCard.getTeleopRocketsCompleted()));
                put("EndGameReturnedToHabitat", scoutCard.getEndGameReturnedToHabitat());
                put("Notes", scoutCard.getNotes());
                put("CompletedDate", scoutCard.getCompletedDateForSQL());
            }});

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
                JSONObject response = apiParser.parse();

                //could not connect to server
                if (response == null)
                    throw new Exception("Could not connect to the web server.");

                if(!response.getString("Status").toLowerCase().equals("success"))
                    throw new Exception(response.getString("Response"));


                return true;
            }
            catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }
    }

    public static class SubmitPitCard extends ScoutingWiredcats
    {
        private MainActivity context;

        public SubmitPitCard(final MainActivity context, final PitCard pitCard)
        {
            super("", new HashMap<String, String>()
            {{
                put("action", "SumbitPitCard");

                put("TeamId", String.valueOf(pitCard.getTeamId()));
                put("EventId", pitCard.getEventId());
                put("DriveStyle", pitCard.getDriveStyle());
                put("AutoExitHabitat", pitCard.getAutoExitHabitat());
                put("AutoHatch", pitCard.getAutoHatch());
                put("AutoCargo", pitCard.getAutoCargo());
                put("TeleopHatch", pitCard.getTeleopHatch());
                put("TeleopCargo", pitCard.getTeleopCargo());
                put("TeleopRocketsComplete", pitCard.getTeleopRocketsComplete());
                put("ReturnToHabitat", pitCard.getReturnToHabitat());
                put("Notes", pitCard.getNotes());
                put("CompletedBy", pitCard.getCompletedBy());
            }});

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
                JSONObject response = apiParser.parse();

                //could not connect to server
                if (response == null)
                    throw new Exception("Could not connect to the web server.");

                if(!response.getString("Status").toLowerCase().equals("success"))
                    throw new Exception(response.getString("Response"));


                return true;
            }
            catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }
    }

    public static class GetEvents extends ScoutingWiredcats
    {
        private ArrayList<Event> events;

        private final String API_FIELD_NAME_EVENT_BLUE_ALLIANCE_ID = "BlueAllianceId";
        private final String API_FIELD_NAME_EVENT_NAME = "Name";
        private final String API_FIELD_NAME_EVENT_CITY = "City";
        private final String API_FIELD_NAME_EVENT_STATE_PROVINCE = "StateProvince";
        private final String API_FIELD_NAME_EVENT_COUNTRY = "Country";
        private final String API_FIELD_NAME_EVENT_START_DATE = "StartDate";
        private final String API_FIELD_NAME_EVENT_END_DATE = "EndDate";

        private MainActivity context;

        public GetEvents(final MainActivity context)
        {
            super("", new HashMap<String, String>()
            {{
                put("action", "GetEvents");
            }});

            this.context = context;
            events = new ArrayList<>();

        }

        @Override
        public boolean execute()
        {
            try
            {
                //parse the data from the server
                ApiParser apiParser = new ApiParser(this);

                //get the response from the server
                JSONObject response = apiParser.parse();

                //could not connect to server
                if (response == null)
                    throw new Exception("Could not connect to the web server.");

                if(!response.getString("Status").toLowerCase().equals("success"))
                    throw new Exception(response.getString("Response"));

                //iterate through, create a new object and add it to the arraylist
                for(int i = 0; i < response.getJSONArray("Response").length(); i++)
                {
                    JSONObject eventObject = response.getJSONArray("Response").getJSONObject(i);

                    String blueAllianceId = eventObject.getString(API_FIELD_NAME_EVENT_BLUE_ALLIANCE_ID);
                    String name = eventObject.getString(API_FIELD_NAME_EVENT_NAME);
                    String city = eventObject.getString(API_FIELD_NAME_EVENT_CITY);
                    String stateProvince = eventObject.getString(API_FIELD_NAME_EVENT_STATE_PROVINCE);
                    String country = eventObject.getString(API_FIELD_NAME_EVENT_COUNTRY);
//                    String startDate = eventObject.getString(API_FIELD_NAME_EVENT_START_DATE);
//                    String endDate = eventObject.getString(API_FIELD_NAME_EVENT_END_DATE);

                    events.add(new Event(-1, blueAllianceId, name, city, stateProvince, country, new Date(), new Date()));
                }


                return true;
            }
            catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }

        public ArrayList<Event> getEvents()
        {
            return events;
        }
    }

}


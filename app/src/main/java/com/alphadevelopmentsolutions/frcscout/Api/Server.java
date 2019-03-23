package com.alphadevelopmentsolutions.frcscout.Api;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Classes.StartingPosition;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Keys;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class Server extends Api
{
    Server(String URLExtension, HashMap<String, String> postData)
    {
        super(Keys.API_URL + URLExtension, postData);
    }

    public static class GetTeamsAtEvent extends Server
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

    public static class GetUsers extends Server
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

    public static class GetScoutCards extends Server
    {
        private ArrayList<ScoutCard> scoutCards;

        private final String API_FIELD_NAME_SCOUT_CARD_MATCH_ID = "MatchId";
        private final String API_FIELD_NAME_SCOUT_CARD_TEAM_ID = "TeamId";
        private final String API_FIELD_NAME_SCOUT_CARD_EVENT_ID = "EventId";
        private final String API_FIELD_NAME_SCOUT_CARD_ALLIANCE_COLOR = "AllianceColor";
        private final String API_FIELD_NAME_SCOUT_CARD_COMPLETED_BY = "CompletedBy";
        
        private final String API_FIELD_NAME_SCOUT_CARD_PRE_GAME_STARTING_LEVEL = "PreGameStartingLevel";
        private final String API_FIELD_NAME_SCOUT_CARD_PRE_GAME_STARTING_POSITION = "PreGameStartingPosition";
        private final String API_FIELD_NAME_SCOUT_CARD_PRE_GAME_STARTING_PIECE = "PreGameStartingPiece";
        
        private final String API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_EXIT_HABITAT = "AutonomousExitHabitat";
        private final String API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_HATCH_PANELS_PICKED_UP = "AutonomousHatchPanelsPickedUp";
        private final String API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS = "AutonomousHatchPanelsSecuredAttempts";
        private final String API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_HATCH_PANELS_SECURED = "AutonomousHatchPanelsSecured";
        private final String API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_CARGO_PICKED_UP = "AutonomousHatchPanelsPickedUp";
        private final String API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_CARGO_STORED_ATTEMPTS = "AutonomousCargoStoredAttempts";
        private final String API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_CARGO_STORED = "AutonomousCargoStored";


        private final String API_FIELD_NAME_SCOUT_CARD_TELEOP_HATCH_PANELS_PICKED_UP = "CargoHatchPanelsPickedUp";
        private final String API_FIELD_NAME_SCOUT_CARD_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS = "CargoHatchPanelsSecuredAttempts";
        private final String API_FIELD_NAME_SCOUT_CARD_TELEOP_HATCH_PANELS_SECURED = "CargoHatchPanelsSecured";
        private final String API_FIELD_NAME_SCOUT_CARD_TELEOP_CARGO_PICKED_UP = "CargoHatchPanelsPickedUp";
        private final String API_FIELD_NAME_SCOUT_CARD_TELEOP_CARGO_STORED_ATTEMPTS = "CargoCargoStoredAttempts";
        private final String API_FIELD_NAME_SCOUT_CARD_TELEOP_CARGO_STORED = "CargoCargoStored";
        
        
        private final String API_FIELD_NAME_SCOUT_CARD_END_GAME_RETURNED_TO_HABITAT = "EndGameReturnedToHabitat";
        private final String API_FIELD_NAME_SCOUT_CARD_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS = "EndGameReturnedToHabitatAttempts";

        private final String API_FIELD_NAME_SCOUT_CARD_BLUE_ALLIANCE_FINAL_SCORE = "RedAllianceFinalScore";
        private final String API_FIELD_NAME_SCOUT_CARD_RED_ALLIANCE_FINAL_SCORE = "BlueAllianceFinalScore";
        private final String API_FIELD_NAME_SCOUT_CARD_DEFENSE_RATING = "DefenseRating";
        private final String API_FIELD_NAME_SCOUT_CARD_OFFENSE_RATING = "OffenseRating";
        private final String API_FIELD_NAME_SCOUT_CARD_DRIVE_RATING = "DriveRating";
        private final String API_FIELD_NAME_SCOUT_CARD_NOTES = "Notes";

        private MainActivity context;

        public GetScoutCards(final MainActivity context, final String event)
        {
            super("", new HashMap<String, String>()
            {{
                put("action", "GetScoutCards");
                put("EventId", event);
            }});

            scoutCards = new ArrayList<>();

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
                    JSONObject scoutCardObject = response.getJSONArray("Response").getJSONObject(i);

                    int matchId = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_MATCH_ID);
                    int teamId = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_TEAM_ID);
                    String eventId = scoutCardObject.getString(API_FIELD_NAME_SCOUT_CARD_EVENT_ID);
                    String allianceColor = scoutCardObject.getString(API_FIELD_NAME_SCOUT_CARD_ALLIANCE_COLOR);
                    String completedBy = scoutCardObject.getString(API_FIELD_NAME_SCOUT_CARD_COMPLETED_BY);

                    int preGameStartingLevel = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_PRE_GAME_STARTING_LEVEL);
                    StartingPosition preGameStartingPosition = StartingPosition.getPositionFromString(scoutCardObject.getString(API_FIELD_NAME_SCOUT_CARD_PRE_GAME_STARTING_POSITION));
                    StartingPiece preGameStartingPiece = StartingPiece.getPieceFromString(scoutCardObject.getString(API_FIELD_NAME_SCOUT_CARD_PRE_GAME_STARTING_PIECE));

                    boolean autonomousExitHabitat = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_EXIT_HABITAT) == 1;
                    int autonomousHatchPanelsPickedUp = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_HATCH_PANELS_PICKED_UP);
                    int autonomousHatchPanelsSecuredAttempts = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS);
                    int autonomousHatchPanelsSecured = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_HATCH_PANELS_SECURED);
                    int autonomousCargoPickedUp = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_CARGO_PICKED_UP);
                    int autonomousCargoStoredAttempts = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_CARGO_STORED_ATTEMPTS);
                    int autonomousCargoStored = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_AUTONOMOUS_CARGO_STORED);

                    int teleopHatchPanelsPickedUp = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_TELEOP_HATCH_PANELS_PICKED_UP);
                    int teleopHatchPanelsSecuredAttempts = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS);
                    int teleopHatchPanelsSecured = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_TELEOP_HATCH_PANELS_SECURED);
                    int teleopCargoPickedUp = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_TELEOP_CARGO_PICKED_UP);
                    int teleopCargoStoredAttempts = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_TELEOP_CARGO_STORED_ATTEMPTS);
                    int teleopCargoStored = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_TELEOP_CARGO_STORED);

                    int endGameReturnedToHabitat = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_END_GAME_RETURNED_TO_HABITAT);
                    int endGameReturnedToHabitatAttempts = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS);

                    int blueAllianceFinalScore = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_BLUE_ALLIANCE_FINAL_SCORE);
                    int redAllianceFinalScore = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_RED_ALLIANCE_FINAL_SCORE);
                    int defenseRating = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_DEFENSE_RATING);
                    int offenseRating = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_OFFENSE_RATING);
                    int driveRating = scoutCardObject.getInt(API_FIELD_NAME_SCOUT_CARD_DRIVE_RATING);
                    String notes = scoutCardObject.getString(API_FIELD_NAME_SCOUT_CARD_NOTES);
                    
                    scoutCards.add(
                    new ScoutCard(
                            -1,
                            matchId,
                            teamId,
                            eventId,
                            allianceColor,
                            completedBy,

                            preGameStartingLevel,
                            preGameStartingPosition,
                            preGameStartingPiece,

                            autonomousExitHabitat,
                            autonomousHatchPanelsPickedUp,
                            autonomousHatchPanelsSecuredAttempts,
                            autonomousHatchPanelsSecured,
                            autonomousCargoPickedUp,
                            autonomousCargoStoredAttempts,
                            autonomousCargoStored,

                            teleopHatchPanelsPickedUp,
                            teleopHatchPanelsSecuredAttempts,
                            teleopHatchPanelsSecured,
                            teleopCargoPickedUp,
                            teleopCargoStoredAttempts,
                            teleopCargoStored,

                            endGameReturnedToHabitat,
                            endGameReturnedToHabitatAttempts,

                            blueAllianceFinalScore,
                            redAllianceFinalScore,
                            defenseRating,
                            offenseRating,
                            driveRating,
                            notes,
                            new Date(0),
                            false));
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

        public ArrayList<ScoutCard> getScoutCards()
        {
            return scoutCards;
        }


        //endregion
    }

    public static class GetPitCards extends Server
    {
        private ArrayList<PitCard> pitCards;

        private final String API_FIELD_NAME_PIT_CARD_TEAM_ID = "TeamId";
        private final String API_FIELD_NAME_PIT_CARD_EVENT_ID = "EventId";

        private final String API_FIELD_NAME_PIT_CARD_DRIVE_STYLE = "DriveStyle";
        private final String API_FIELD_NAME_PIT_CARD_ROBOT_WEIGHT = "RobotWeight";

        private final String API_FIELD_NAME_PIT_CARD_AUTO_EXIT_HABITAT = "AutoExitHabitat";
        private final String API_FIELD_NAME_PIT_CARD_AUTO_HATCH = "AutoHatch";
        private final String API_FIELD_NAME_PIT_CARD_AUTO_CARGO = "AutoCargo";

        private final String API_FIELD_NAME_PIT_CARD_TELEOP_HATCH = "TeleopHatch";
        private final String API_FIELD_NAME_PIT_CARD_TELEOP_CARGO = "TeleopCargo";

        private final String API_FIELD_NAME_PIT_CARD_RETURN_TO_HABITAT = "ReturnToHabitat";

        private final String API_FIELD_NAME_PIT_CARD_NOTES = "Notes";
        private final String API_FIELD_NAME_PIT_CARD_COMPLETED_BY = "CompletedBy";


        private MainActivity context;

        public GetPitCards(final MainActivity context, final String event)
        {
            super("", new HashMap<String, String>()
            {{
                put("action", "GetPitCards");
                put("EventId", event);
            }});

            pitCards = new ArrayList<>();

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
                    JSONObject pitCardObject = response.getJSONArray("Response").getJSONObject(i);

                    int teamId = pitCardObject.getInt(API_FIELD_NAME_PIT_CARD_TEAM_ID);
                    String eventId = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_EVENT_ID);

                    String driveStyle = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_DRIVE_STYLE);
                    String robotWeight = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_ROBOT_WEIGHT);

                    String autonomousExitHabitat = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_AUTO_EXIT_HABITAT);
                    String autonomousHatchPanelsSecured = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_AUTO_HATCH);
                    String autonomousCargoStored = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_AUTO_CARGO);

                    String teleopHatchPanelsSecured = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_TELEOP_HATCH);
                    String teleopCargoStored = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_TELEOP_CARGO);

                    String endGameReturnedToHabitat = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_RETURN_TO_HABITAT);

                    String notes = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_NOTES);
                    String completedBy = pitCardObject.getString(API_FIELD_NAME_PIT_CARD_COMPLETED_BY);


                    pitCards.add(new PitCard(
                            -1,
                            teamId,
                            eventId,

                            driveStyle,
                            robotWeight,

                            autonomousExitHabitat,
                            autonomousHatchPanelsSecured,
                            autonomousCargoStored,

                            teleopHatchPanelsSecured,
                            teleopCargoStored,

                            endGameReturnedToHabitat,

                            notes,
                            completedBy,
                            false
                    ));
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

        public ArrayList<PitCard> getPitCards()
        {
            return pitCards;
        }


        //endregion
    }

    public static class SubmitScoutCard extends Server
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
                put("AllianceColor", scoutCard.getAllianceColor());
                put("CompletedBy", scoutCard.getCompletedBy());


                put("PreGameStartingLevel", String.valueOf(scoutCard.getPreGameStartingLevel()));
                put("PreGameStartingPosition", scoutCard.getPreGameStartingPosition().name());
                put("PreGameStartingPiece", scoutCard.getPreGameStartingPiece().name());

                put("AutonomousExitHabitat", String.valueOf(scoutCard.getAutonomousExitHabitat() ? 1 : 0));
                put("AutonomousHatchPanelsPickedUp", String.valueOf(scoutCard.getAutonomousHatchPanelsPickedUp()));
                put("AutonomousHatchPanelsSecuredAttempts", String.valueOf(scoutCard.getAutonomousHatchPanelsSecuredAttempts()));
                put("AutonomousHatchPanelsSecured", String.valueOf(scoutCard.getAutonomousHatchPanelsSecured()));
                put("AutonomousCargoPickedUp", String.valueOf(scoutCard.getAutonomousCargoPickedUp()));
                put("AutonomousCargoSecuredAttempts", String.valueOf(scoutCard.getAutonomousCargoStoredAttempts()));
                put("AutonomousCargoSecured", String.valueOf(scoutCard.getAutonomousCargoStored()));

                put("TeleopHatchPanelsPickedUp", String.valueOf(scoutCard.getTeleopHatchPanelsPickedUp()));
                put("TeleopHatchPanelsSecuredAttempts", String.valueOf(scoutCard.getTeleopHatchPanelsSecuredAttempts()));
                put("TeleopHatchPanelsSecured", String.valueOf(scoutCard.getTeleopHatchPanelsSecured()));
                put("TeleopCargoPickedUp", String.valueOf(scoutCard.getTeleopCargoPickedUp()));
                put("TeleopCargoSecuredAttempts", String.valueOf(scoutCard.getTeleopCargoStoredAttempts()));
                put("TeleopCargoSecured", String.valueOf(scoutCard.getTeleopCargoStored()));

                put("EndGameReturnedToHabitat", String.valueOf(scoutCard.getEndGameReturnedToHabitat()));
                put("EndGameReturnedToHabitatAttempts", String.valueOf(scoutCard.getEndGameReturnedToHabitatAttempts()));

                put("BlueAllianceFinalScore", String.valueOf(scoutCard.getBlueAllianceFinalScore()));
                put("RedAllianceFinalScore", String.valueOf(scoutCard.getRedAllianceFinalScore()));
                put("DefenseRating", String.valueOf(scoutCard.getDefenseRating()));
                put("OffenseRating", String.valueOf(scoutCard.getOffenseRating()));
                put("DriveRating", String.valueOf(scoutCard.getDriveRating()));
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

    public static class SubmitPitCard extends Server
    {
        private MainActivity context;

        public SubmitPitCard(final MainActivity context, final PitCard pitCard)
        {
            super("", new HashMap<String, String>()
            {{
                put("action", "SubmitPitCard");

                put("TeamId", String.valueOf(pitCard.getTeamId()));
                put("EventId", pitCard.getEventId());

                put("DriveStyle", pitCard.getDriveStyle());
                put("RobotWeight", pitCard.getRobotWeight());

                put("AutoExitHabitat", pitCard.getAutoExitHabitat());
                put("AutoHatch", pitCard.getAutoHatch());
                put("AutoCargo", pitCard.getAutoCargo());

                put("TeleopHatch", pitCard.getTeleopHatch());
                put("TeleopCargo", pitCard.getTeleopCargo());

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

    public static class GetEvents extends Server
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


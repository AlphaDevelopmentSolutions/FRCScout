package com.alphadevelopmentsolutions.frcscout.Api;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItemResult;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.User;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Years;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public abstract class Server extends Api
{
    Server(String URL, String key, HashMap<String, String> postData)
    {
        super(URL, key, postData);
    }

    //region Getters

    public static class Hello extends Server
    {
        private MainActivity context;

        public Hello(final MainActivity context)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "Hello");
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(context.getString(R.string.server_error));
                
                return true;
            } catch (Exception e)
            {
                return false;
            }
        }
    }

    public static class GetServerConfig extends Server
    {
        private MainActivity context;

        public GetServerConfig(final MainActivity context)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetServerConfig");
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));

                //get the json obj from the server
                JSONObject serverConfigObject = response.getJSONObject(API_FIELD_NAME_RESPONSE);

                String apiKey = serverConfigObject.getString("ApiKey");
                int teamNumber = serverConfigObject.getInt("TeamNumber");
                String teamName = serverConfigObject.getString("TeamName");

                //store the configs into the shared prefs
                context.setPreference(Constants.SharedPrefKeys.API_KEY_KEY, apiKey);
                context.setPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, teamNumber);
                context.setPreference(Constants.SharedPrefKeys.TEAM_NAME_KEY, teamName);

                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }
    }
    
    public static class GetTeamsAtEvent extends Server
    {
        private ArrayList<Team> teams;

        private MainActivity context;

        public GetTeamsAtEvent(final MainActivity context, final Event event)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetTeamsAtEvent");
                put("EventId", event.getBlueAllianceId());
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject teamObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    int teamId = teamObject.getInt(Team.COLUMN_NAME_ID);
                    String name = teamObject.getString(Team.COLUMN_NAME_NAME);
                    String city = teamObject.getString(Team.COLUMN_NAME_CITY);
                    String stateProvince = teamObject.getString(Team.COLUMN_NAME_STATEPROVINCE);
                    String country = teamObject.getString(Team.COLUMN_NAME_COUNTRY);
                    int rookieYear = teamObject.getInt(Team.COLUMN_NAME_ROOKIE_YEAR);
                    String facebookURL = teamObject.getString(Team.COLUMN_NAME_FACEBOOK_URL);
                    String twitterURL = teamObject.getString(Team.COLUMN_NAME_TWITTER_URL);
                    String instagramURL = teamObject.getString(Team.COLUMN_NAME_INSTAGRAM_URL);
                    String youtubeURL = teamObject.getString(Team.COLUMN_NAME_YOUTUBE_URL);
                    String websiteUrl = teamObject.getString(Team.COLUMN_NAME_WEBSITE_URL);

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
            } catch (Exception e)
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

        private MainActivity context;

        public GetUsers(final MainActivity context)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetUsers");
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject teamObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    String firstName = teamObject.getString(User.COLUMN_NAME_FIRST_NAME);
                    String lastName = teamObject.getString(User.COLUMN_NAME_LAST_NAME);

                    users.add(new User(-1, firstName, lastName));
                }

                return true;
            } catch (Exception e)
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

        private MainActivity context;

        public GetScoutCards(final MainActivity context, final Event event)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetScoutCards");
                put("EventId", event.getBlueAllianceId());
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject scoutCardObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    String matchId = scoutCardObject.getString(ScoutCard.COLUMN_NAME_MATCH_ID);
                    int teamId = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_TEAM_ID);
                    String eventId = scoutCardObject.getString(ScoutCard.COLUMN_NAME_EVENT_ID);
                    String allianceColor = scoutCardObject.getString(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR);
                    String completedBy = scoutCardObject.getString(ScoutCard.COLUMN_NAME_COMPLETED_BY);

                    int preGameStartingLevel = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_LEVEL);
                    StartingPosition preGameStartingPosition = StartingPosition.getPositionFromString(scoutCardObject.getString(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_POSITION));
                    StartingPiece preGameStartingPiece = StartingPiece.getPieceFromString(scoutCardObject.getString(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_PIECE));

                    boolean autonomousExitHabitat = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT) == 1;
                    int autonomousHatchPanelsPickedUp = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP);
                    int autonomousHatchPanelsSecuredAttempts = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS);
                    int autonomousHatchPanelsSecured = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED);
                    int autonomousCargoPickedUp = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP);
                    int autonomousCargoStoredAttempts = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS);
                    int autonomousCargoStored = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED);

                    int teleopHatchPanelsPickedUp = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP);
                    int teleopHatchPanelsSecuredAttempts = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS);
                    int teleopHatchPanelsSecured = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED);
                    int teleopCargoPickedUp = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_TELEOP_CARGO_PICKED_UP);
                    int teleopCargoStoredAttempts = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS);
                    int teleopCargoStored = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED);

                    int endGameReturnedToHabitat = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT);
                    int endGameReturnedToHabitatAttempts = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS);

                    int defenseRating = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_DEFENSE_RATING);
                    int offenseRating = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_OFFENSE_RATING);
                    int driveRating = scoutCardObject.getInt(ScoutCard.COLUMN_NAME_DRIVE_RATING);
                    String notes = scoutCardObject.getString(ScoutCard.COLUMN_NAME_NOTES);

                    Date completedDate = simpleDateFormat.parse(scoutCardObject.getString(ScoutCard.COLUMN_NAME_COMPLETED_DATE));

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

                                    defenseRating,
                                    offenseRating,
                                    driveRating,
                                    notes,
                                    completedDate,
                                    false));
                }

                return true;
            } catch (Exception e)
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

        private MainActivity context;

        public GetPitCards(final MainActivity context, final Event event)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetPitCards");
                put("EventId", event.getBlueAllianceId());
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject pitCardObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    int teamId = pitCardObject.getInt(PitCard.COLUMN_NAME_TEAM_ID);
                    String eventId = pitCardObject.getString(PitCard.COLUMN_NAME_EVENT_ID);

                    String driveStyle = pitCardObject.getString(PitCard.COLUMN_NAME_DRIVE_STYLE);
                    String robotWeight = pitCardObject.getString(PitCard.COLUMN_NAME_ROBOT_WEIGHT);
                    String robotLength = pitCardObject.getString(PitCard.COLUMN_NAME_ROBOT_LENGTH);
                    String robotWidth = pitCardObject.getString(PitCard.COLUMN_NAME_ROBOT_WIDTH);
                    String robotHeight = pitCardObject.getString(PitCard.COLUMN_NAME_ROBOT_HEIGHT);

                    String autonomousExitHabitat = pitCardObject.getString(PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT);
                    String autonomousHatchPanelsSecured = pitCardObject.getString(PitCard.COLUMN_NAME_AUTO_HATCH);
                    String autonomousCargoStored = pitCardObject.getString(PitCard.COLUMN_NAME_AUTO_CARGO);

                    String teleopHatchPanelsSecured = pitCardObject.getString(PitCard.COLUMN_NAME_TELEOP_HATCH);
                    String teleopCargoStored = pitCardObject.getString(PitCard.COLUMN_NAME_TELEOP_CARGO);

                    String endGameReturnedToHabitat = pitCardObject.getString(PitCard.COLUMN_NAME_RETURN_TO_HABITAT);

                    String notes = pitCardObject.getString(PitCard.COLUMN_NAME_NOTES);
                    String completedBy = pitCardObject.getString(PitCard.COLUMN_NAME_COMPLETED_BY);


                    pitCards.add(new PitCard(
                            -1,
                            teamId,
                            eventId,

                            driveStyle,
                            robotWeight,
                            robotLength,
                            robotWidth,
                            robotHeight,

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
            } catch (Exception e)
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

    public static class GetMatches extends Server
    {
        private ArrayList<Match> matches;

        private MainActivity context;

        public GetMatches(final MainActivity context, final Event event)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetMatches");
                put("EventId", event.getBlueAllianceId());
            }});

            matches = new ArrayList<>();

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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject matchObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    Date date = simpleDateFormat.parse(matchObject.getString(Match.COLUMN_NAME_DATE));
                    String eventId = matchObject.getString(Match.COLUMN_NAME_EVENT_ID);
                    String key = matchObject.getString(Match.COLUMN_NAME_KEY);
                    Match.Type matchType = Match.Type.getTypeFromString(matchObject.getString(Match.COLUMN_NAME_MATCH_TYPE));
                    int setNumber = matchObject.getInt(Match.COLUMN_NAME_SET_NUMBER);
                    int matchNumber = matchObject.getInt(Match.COLUMN_NAME_MATCH_NUMBER);

                    int blueAllianceTeamOneId = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID);
                    int blueAllianceTeamTwoId = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID);
                    int blueAllianceTeamThreeId = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID);

                    int redAllianceTeamOneId = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID);
                    int redAllianceTeamTwoId = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID);
                    int redAllianceTeamThreeId = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID);

                    int blueAllianceScore = matchObject.getInt(Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE);
                    int redAllianceScore = matchObject.getInt(Match.COLUMN_NAME_RED_ALLIANCE_SCORE);


                    matches.add(new Match(
                            -1,
                            date,
                            eventId,
                            key,
                            matchType,
                            setNumber,
                            matchNumber,

                            blueAllianceTeamOneId,
                            blueAllianceTeamTwoId,
                            blueAllianceTeamThreeId,

                            redAllianceTeamOneId,
                            redAllianceTeamTwoId,
                            redAllianceTeamThreeId,

                            blueAllianceScore,
                            redAllianceScore
                    ));
                }

                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }

        //region Getters

        public ArrayList<Match> getMatches()
        {
            return matches;
        }


        //endregion
    }

    public static class GetRobotMedia extends Server
    {
        private ArrayList<RobotMedia> robotMedia;

        private MainActivity context;

        public GetRobotMedia(final MainActivity context, final int teamId)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetRobotMedia");
                put("TeamId", String.valueOf(teamId));
            }});

            robotMedia = new ArrayList<>();

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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject robotMediaJson = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    int teamId = robotMediaJson.getInt(RobotMedia.COLUMN_NAME_TEAM_ID);
                    String fileUri = context.getPreference(Constants.SharedPrefKeys.WEB_URL_KEY, "").toString() + "/assets/robot-media/" + robotMediaJson.getString(RobotMedia.COLUMN_NAME_FILE_URI);

                    fileUri = apiParser.downloadImage(fileUri, Constants.ROBOT_MEDIA_DIRECTORY).getAbsolutePath();

                    robotMedia.add(new RobotMedia(
                            -1,
                            teamId,
                            fileUri,
                            false
                    ));

                }

                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }

        //region Getters

        public ArrayList<RobotMedia> getRobotMedia()
        {
            return robotMedia;
        }


        //endregion
    }

    public static class GetYears extends Server
    {
        private ArrayList<Years> yearsArrayList;

        private MainActivity context;

        public GetYears(final MainActivity context)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetYears");
            }});

            yearsArrayList = new ArrayList<>();

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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject yearsJson = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    int serverId = yearsJson.getInt(Years.COLUMN_NAME_SERVER_ID);
                    String name = yearsJson.getString(Years.COLUMN_NAME_NAME);
                    Date startDate = simpleDateFormat.parse(yearsJson.getString(Years.COLUMN_NAME_START_DATE));
                    Date endDate = simpleDateFormat.parse(yearsJson.getString(Years.COLUMN_NAME_END_DATE));
                    String fileUri = context.getPreference (Constants.SharedPrefKeys.WEB_URL_KEY, "").toString() + "/assets/year-media/" + yearsJson.getString(Years.COLUMN_NAME_IMAGE_URI);

                    fileUri = apiParser.downloadImage(fileUri, Constants.YEAR_MEDIA_DIRECTORY).getAbsolutePath();

                    yearsArrayList.add(new Years(
                            -1,
                            serverId,
                            name,
                            startDate,
                            endDate,
                            fileUri
                    ));

                }

                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }

        //region Getters

        public ArrayList<Years> getYears()
        {
            return yearsArrayList;
        }


        //endregion
    }

    public static class GetEvents extends Server
    {
        private ArrayList<Event> events;

        private MainActivity context;

        public GetEvents(final MainActivity context)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetEvents");
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));

                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject eventObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    int yearId = eventObject.getInt(Event.COLUMN_NAME_YEAR_ID);
                    String blueAllianceId = eventObject.getString(Event.COLUMN_NAME_BLUE_ALLIANCE_ID);
                    String name = eventObject.getString(Event.COLUMN_NAME_NAME);
                    String city = eventObject.getString(Event.COLUMN_NAME_CITY);
                    String stateProvince = eventObject.getString(Event.COLUMN_NAME_STATEPROVINCE);
                    String country = eventObject.getString(Event.COLUMN_NAME_COUNTRY);
                    String startDate = eventObject.getString(Event.COLUMN_NAME_START_DATE);
                    String endDate = eventObject.getString(Event.COLUMN_NAME_END_DATE);

                    events.add(new Event(
                            -1,
                            yearId,
                            blueAllianceId,
                            name,
                            city,
                            stateProvince,
                            country,
                            simpleDateFormat.parse(startDate),
                            simpleDateFormat.parse(endDate)));
                }


                return true;
            } catch (Exception e)
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

    public static class GetChecklistItems extends Server
    {
        private ArrayList<ChecklistItem> checklistItems;

        private MainActivity context;

        public GetChecklistItems(final MainActivity context)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetChecklistItems");
            }});

            checklistItems = new ArrayList<>();

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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONObject checklistItemObject = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONObject(i);

                    int serverId = checklistItemObject.getInt(ChecklistItem.COLUMN_NAME_SERVER_ID);

                    String title = checklistItemObject.getString(ChecklistItem.COLUMN_NAME_TITLE);
                    String description = checklistItemObject.getString(ChecklistItem.COLUMN_NAME_DESCRIPTION);

                    checklistItems.add(
                            new ChecklistItem(
                                    -1,
                                    serverId,
                                    title,
                                    description));
                }

                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }

        //region Getters

        public ArrayList<ChecklistItem> getChecklistItems()
        {
            return checklistItems;
        }

        //endregion
    }

    public static class GetChecklistItemResults extends Server
    {
        private ArrayList<ChecklistItemResult> checklistItemResults;

        private MainActivity context;

        public GetChecklistItemResults(final MainActivity context)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "GetChecklistItemResults");
            }});

            checklistItemResults = new ArrayList<>();

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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                //iterate through, create a new object and add it to the arraylist
                for (int i = 0; i < response.getJSONArray(API_FIELD_NAME_RESPONSE).length(); i++)
                {
                    JSONArray checklistItemResultArray = response.getJSONArray(API_FIELD_NAME_RESPONSE).getJSONArray(i);

                    for(int j = 0; j < checklistItemResultArray.length(); j++)
                    {

                        JSONObject checklistItemResultObject = checklistItemResultArray.getJSONObject(j);

                        int checklistItemId = checklistItemResultObject.getInt(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID);

                        String matchId = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_MATCH_ID);
                        String status = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_STATUS);
                        String completedBy = checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY);

                        Date completedDate = simpleDateFormat.parse(checklistItemResultObject.getString(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE));

                        checklistItemResults.add(
                                new ChecklistItemResult(
                                        -1,
                                        checklistItemId,
                                        matchId,

                                        status,
                                        completedBy,

                                        completedDate,
                                        false
                                ));
                    }
                }

                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }

        //region Getters

        public ArrayList<ChecklistItemResult> getChecklistItemResults()
        {
            return checklistItemResults;
        }

        //endregion
    }

    //endregion

    //region Setters

    public static class SubmitScoutCard extends Server
    {
        private MainActivity context;

        public SubmitScoutCard(final MainActivity context, final ScoutCard scoutCard)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "SubmitScoutCard");

                put(ScoutCard.COLUMN_NAME_MATCH_ID, String.valueOf(scoutCard.getMatchId()));
                put(ScoutCard.COLUMN_NAME_TEAM_ID, String.valueOf(scoutCard.getTeamId()));
                put(ScoutCard.COLUMN_NAME_EVENT_ID, scoutCard.getEventId());
                put(ScoutCard.COLUMN_NAME_ALLIANCE_COLOR, scoutCard.getAllianceColor());
                put(ScoutCard.COLUMN_NAME_COMPLETED_BY, scoutCard.getCompletedBy());

                put(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_LEVEL, String.valueOf(scoutCard.getPreGameStartingLevel()));
                put(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_POSITION, scoutCard.getPreGameStartingPosition().name());
                put(ScoutCard.COLUMN_NAME_PRE_GAME_STARTING_PIECE, scoutCard.getPreGameStartingPiece().name());

                put(ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT, String.valueOf(scoutCard.getAutonomousExitHabitat() ? 1 : 0));
                put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_PICKED_UP, String.valueOf(scoutCard.getAutonomousHatchPanelsPickedUp()));
                put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED_ATTEMPTS, String.valueOf(scoutCard.getAutonomousHatchPanelsSecuredAttempts()));
                put(ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED, String.valueOf(scoutCard.getAutonomousHatchPanelsSecured()));
                put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_PICKED_UP, String.valueOf(scoutCard.getAutonomousCargoPickedUp()));
                put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED_ATTEMPTS, String.valueOf(scoutCard.getAutonomousCargoStoredAttempts()));
                put(ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED, String.valueOf(scoutCard.getAutonomousCargoStored()));

                put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_PICKED_UP, String.valueOf(scoutCard.getTeleopHatchPanelsPickedUp()));
                put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED_ATTEMPTS, String.valueOf(scoutCard.getTeleopHatchPanelsSecuredAttempts()));
                put(ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED, String.valueOf(scoutCard.getTeleopHatchPanelsSecured()));
                put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_PICKED_UP, String.valueOf(scoutCard.getTeleopCargoPickedUp()));
                put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED_ATTEMPTS, String.valueOf(scoutCard.getTeleopCargoStoredAttempts()));
                put(ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED, String.valueOf(scoutCard.getTeleopCargoStored()));

                put(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT, String.valueOf(scoutCard.getEndGameReturnedToHabitat()));
                put(ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT_ATTEMPTS, String.valueOf(scoutCard.getEndGameReturnedToHabitatAttempts()));

                put(ScoutCard.COLUMN_NAME_DEFENSE_RATING, String.valueOf(scoutCard.getDefenseRating()));
                put(ScoutCard.COLUMN_NAME_OFFENSE_RATING, String.valueOf(scoutCard.getOffenseRating()));
                put(ScoutCard.COLUMN_NAME_DRIVE_RATING, String.valueOf(scoutCard.getDriveRating()));
                put(ScoutCard.COLUMN_NAME_NOTES, scoutCard.getNotes());

                put(ScoutCard.COLUMN_NAME_COMPLETED_DATE, scoutCard.getCompletedDateForSQL());
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                return true;
            } catch (Exception e)
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
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "SubmitPitCard");

                put(PitCard.COLUMN_NAME_TEAM_ID, String.valueOf(pitCard.getTeamId()));
                put(PitCard.COLUMN_NAME_EVENT_ID, pitCard.getEventId());

                put(PitCard.COLUMN_NAME_DRIVE_STYLE, pitCard.getDriveStyle());
                put(PitCard.COLUMN_NAME_ROBOT_WEIGHT, pitCard.getRobotWeight());
                put(PitCard.COLUMN_NAME_ROBOT_LENGTH, pitCard.getRobotLength());
                put(PitCard.COLUMN_NAME_ROBOT_WIDTH, pitCard.getRobotWidth());
                put(PitCard.COLUMN_NAME_ROBOT_HEIGHT, pitCard.getRobotHeight());

                put(PitCard.COLUMN_NAME_AUTO_EXIT_HABITAT, pitCard.getAutoExitHabitat());
                put(PitCard.COLUMN_NAME_AUTO_HATCH, pitCard.getAutoHatch());
                put(PitCard.COLUMN_NAME_AUTO_CARGO, pitCard.getAutoCargo());

                put(PitCard.COLUMN_NAME_TELEOP_HATCH, pitCard.getTeleopHatch());
                put(PitCard.COLUMN_NAME_TELEOP_CARGO, pitCard.getTeleopCargo());

                put(PitCard.COLUMN_NAME_RETURN_TO_HABITAT, pitCard.getReturnToHabitat());

                put(PitCard.COLUMN_NAME_NOTES, pitCard.getNotes());
                put(PitCard.COLUMN_NAME_COMPLETED_BY, pitCard.getCompletedBy());
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }
    }

    public static class SubmitRobotMedia extends Server
    {
        private MainActivity context;

        public SubmitRobotMedia(final MainActivity context, final RobotMedia robotMedia)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "SubmitRobotMedia");

                put("TeamId", String.valueOf(robotMedia.getTeamId()));
                put("Base64Image", robotMedia.getBase64Image());
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));


                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }
    }

    public static class SubmitChecklistItemResult extends Server
    {
        private MainActivity context;

        public SubmitChecklistItemResult(final MainActivity context, final ChecklistItemResult checklistItemResult)
        {
            super(context.getPreference(Constants.SharedPrefKeys.API_URL_KEY, "").toString(), context.getPreference(Constants.SharedPrefKeys.API_KEY_KEY, "").toString(), new HashMap<String, String>()
            {{
                put(API_PARAM_API_ACTION, "SubmitChecklistItemResult");

                put(ChecklistItemResult.COLUMN_NAME_CHECKLIST_ITEM_ID, String.valueOf(checklistItemResult.getChecklistItemId()));
                put(ChecklistItemResult.COLUMN_NAME_MATCH_ID, checklistItemResult.getMatchId());

                put(ChecklistItemResult.COLUMN_NAME_STATUS, checklistItemResult.getStatus());
                put(ChecklistItemResult.COLUMN_NAME_COMPLETED_BY, checklistItemResult.getCompletedBy());

                put(ChecklistItemResult.COLUMN_NAME_COMPLETED_DATE, checklistItemResult.getCompletedDateForSQL());
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
                    throw new Exception(context.getString(R.string.server_error));

                if (!response.getString(API_FIELD_NAME_STATUS).equals(API_FIELD_NAME_STATUS_SUCCESS))
                    throw new Exception(response.getString(API_FIELD_NAME_RESPONSE));

                return true;
            } catch (Exception e)
            {
                context.showSnackbar(e.getMessage());
                return false;
            }
        }
    }
    //endregion

}


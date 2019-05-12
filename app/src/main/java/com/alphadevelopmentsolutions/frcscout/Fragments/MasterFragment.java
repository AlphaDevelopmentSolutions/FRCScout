package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

public class MasterFragment extends Fragment
{
    //store the context and database in the master fragment which all other fragmets extend from
    protected MainActivity context;
    protected Database database;

    protected Event event;

    protected static final String ARG_TEAM_JSON = "TEAM_JSON";
    protected static final String ARG_MATCH_JSON = "MATCH_JSON";
    protected static final String ARG_PARAM_SCOUT_CARD_JSON = "SCOUT_CARD_JSON";
    protected static final String ARG_PARAM_PIT_CARD_JSON = "PIT_CARD_JSON";

    protected String teamJson;
    protected String matchJson;
    protected String scoutCardJson;
    protected String pitCardJson;

    protected Team team;
    protected Match match;
    protected ScoutCard scoutCard;
    protected PitCard pitCard;

    protected Gson gson;
    protected static Gson staticGson;

    protected Thread loadingThread;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //assign context and database vars
        context = (MainActivity) getActivity();
        database = context.getDatabase();
        gson = new Gson();

        //check if any args were passed, specifically for team and match json
        if (getArguments() != null)
        {
            teamJson = getArguments().getString(ARG_TEAM_JSON);
            matchJson = getArguments().getString(ARG_MATCH_JSON);
            scoutCardJson = getArguments().getString(ARG_PARAM_SCOUT_CARD_JSON);
            pitCardJson = getArguments().getString(ARG_PARAM_PIT_CARD_JSON);
        }

        //create and start the thread to load the json vars
        loadingThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //check if the event id is set properly, and load the event
                int eventId = (Integer) context.getPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1);
                if(eventId > 0)
                    event = new Event(eventId, database);

                //load the team from json, if available
                if(teamJson != null && !teamJson.equals(""))
                    team = gson.fromJson(teamJson, Team.class);

                //load the match from json, if available
                if(matchJson != null && !matchJson.equals(""))
                    match = gson.fromJson(matchJson, Match.class);

                //load the scout card from json, if available
                if(scoutCardJson != null && !scoutCardJson.equals(""))
                    scoutCard = new Gson().fromJson(scoutCardJson, ScoutCard.class);

                //load the scout card from json, if available
                if(pitCardJson != null && !pitCardJson.equals(""))
                    pitCard = new Gson().fromJson(pitCardJson, PitCard.class);
            }
        });

        loadingThread.start();
    }

    @Override
    public void onDetach()
    {
        //reset the title of the app upon detach
        context.getSupportActionBar().setTitle(R.string.app_name);
        super.onDetach();
    }

    /**
     * Joins back up with the loading thread
     */
    protected void joinLoadingThread()
    {
        //join back up with the loading thread
        try
        {
            loadingThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Converts fields to json regardless if they are null or not
     * @param object to convert to json
     * @return null | string json object
     */
    protected static @Nullable String toJson(Object object)
    {
        if(staticGson == null)
            staticGson = new Gson();

        if(object == null)
            return null;
        else
            return staticGson.toJson(object);
    }
}

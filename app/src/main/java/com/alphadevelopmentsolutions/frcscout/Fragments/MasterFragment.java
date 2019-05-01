package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
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

    protected String teamJson;
    protected String matchJson;

    protected Team team;
    protected Match match;

    protected Gson gson;

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
                //no team provided, default to current app team
                else
                    team = new Team((Integer) context.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1), database);

                //load the match from json, if available
                if(matchJson != null && !matchJson.equals(""))
                    match = gson.fromJson(matchJson, Match.class);
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
}

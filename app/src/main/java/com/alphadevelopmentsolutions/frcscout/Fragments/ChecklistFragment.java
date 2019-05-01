package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphadevelopmentsolutions.frcscout.Adapters.ChecklistItemListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Adapters.EventListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Adapters.MatchListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChecklistFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChecklistFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChecklistFragment extends MasterFragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";

    // TODO: Rename and change types of parameters
    private String teamJson;
    private String eventJson;
    private String matchJson;

    private OnFragmentInteractionListener mListener;

    public ChecklistFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param teamJson
     * @param eventJson
     * @param matchJson
     * @return A new instance of fragment ChecklistFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChecklistFragment newInstance(String teamJson, String eventJson, String matchJson)
    {
        ChecklistFragment fragment = new ChecklistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, teamJson);
        args.putString(ARG_PARAM2, eventJson);
        args.putString(ARG_PARAM3, matchJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            teamJson = getArguments().getString(ARG_PARAM1);
            eventJson = getArguments().getString(ARG_PARAM2);
            matchJson = getArguments().getString(ARG_PARAM3);
        }

        loadingThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                gson = new Gson();

                //load the team from json
                if(teamJson != null && !teamJson.equals(""))
                    team = gson.fromJson(teamJson, Team.class);

                //load the event from json
                if(eventJson != null && !eventJson.equals(""))
                    event = gson.fromJson(eventJson, Event.class);

                //load the match from json
                if(matchJson != null && !matchJson.equals(""))
                    match = gson.fromJson(matchJson, Match.class);
            }
        });

        loadingThread.start();

    }

    private Thread loadingThread;

    private Gson gson;

    private Event event;

    private Match match;

    private Team team;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;

        RecyclerView recyclerView;

        //join back up with the loading thread
        try
        {
            loadingThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        //no event selected, show event list
        if(event == null)
        {
            view = inflater.inflate(R.layout.fragment_event_list, container, false);

            recyclerView = view.findViewById(R.id.EventListRecyclerView);

            EventListRecyclerViewAdapter eventListRecyclerViewAdapter = new EventListRecyclerViewAdapter(database.getEvents(), context, (this).getClass());
            recyclerView.setAdapter(eventListRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        //no match selected, show match list
        else if (match == null)
        {
            view = inflater.inflate(R.layout.fragment_match_list, container, false);

            recyclerView = view.findViewById(R.id.MatchListRecyclerView);

            MatchListRecyclerViewAdapter matchListRecyclerViewAdapter = new MatchListRecyclerViewAdapter(event, team, database.getMatches(team, event), context, (this).getClass());
            recyclerView.setAdapter(matchListRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        //match and event selected, show the checklist
        else
        {
            view = inflater.inflate(R.layout.fragment_checklist, container, false);

            recyclerView = view.findViewById(R.id.ChecklistItemsRecyclerView);

            ChecklistItemListRecyclerViewAdapter checklistItemListRecyclerViewAdapter = new ChecklistItemListRecyclerViewAdapter(event, match, team, database.getChecklistItems(), context);
            recyclerView.setAdapter(checklistItemListRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
        }





        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

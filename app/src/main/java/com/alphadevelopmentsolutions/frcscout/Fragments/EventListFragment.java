package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphadevelopmentsolutions.frcscout.Adapters.EventListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Years;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EventListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EventListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventListFragment extends MasterFragment
{
    private OnFragmentInteractionListener mListener;

    private static final String ARG_YEAR_JSON = "YEAR_JSON";

    private String yearJson;

    public EventListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param year to grab events from
     * @return A new instance of fragment EventListFragment.
     */
    public static EventListFragment newInstance(Years year)
    {
        EventListFragment fragment = new EventListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_YEAR_JSON, toJson(year));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        //check if any args were passed, specifically for team and match json
        if (getArguments() != null)
        {
            yearJson = getArguments().getString(ARG_YEAR_JSON);
        }

        //create and start the thread to load the json vars
        loadYearThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                //load the scout card from json, if available
                if(yearJson != null && !yearJson.equals(""))
                    year = new Gson().fromJson(yearJson, Years.class);
            }
        });

        loadYearThread.start();
    }

    private RecyclerView eventListRecyclerView;

    private Thread loadYearThread;

    private Years year;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);

        joinLoadingThread();

        //join back up with the load year thread
        try
        {
            loadYearThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        context.setTitle(year.toString());
        context.setChangeButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //send to eventlist frag
                context.changeFragment(YearListFragment.newInstance(), false);
            }
        }, getString(R.string.change_year), false);



        //showing this view means the user has not selected an event, clear the shared pref
        context.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1);

        eventListRecyclerView = view.findViewById(R.id.EventListRecyclerView);

        EventListRecyclerViewAdapter eventListRecyclerViewAdapter = new EventListRecyclerViewAdapter(Event.getEvents(year, null, database), context);
        eventListRecyclerView.setAdapter(eventListRecyclerViewAdapter);
        eventListRecyclerView.setLayoutManager(new LinearLayoutManager(context));

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
        context.setChangeButtonOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Years year = new Years((Integer) context.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)));
                year.load(database);

                //send to eventlist frag
                context.changeFragment(EventListFragment.newInstance(year), false);
            }
        }, getString(R.string.change_event), true);
    }

    @Override
    public void onStop()
    {
        context.unlockDrawerLayout();
        super.onStop();
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

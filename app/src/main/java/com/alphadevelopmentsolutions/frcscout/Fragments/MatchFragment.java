package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Adapters.EventViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.Adapters.MatchViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MatchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchFragment extends MasterFragment
{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "BlueAllianceTeamIds";
    private static final String ARG_PARAM2 = "RedAllianceTeamIds";

    private ArrayList<Integer> blueAllianceTeamIds;
    private ArrayList<Integer> redAllianceTeamIds;

    private OnFragmentInteractionListener mListener;

    public MatchFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param blueAllianceTeamIds Ids of all blue alliance teams
     * @param redAllianceTeamIds Ids of all red alliance teams
     * @return A new instance of fragment MatchFragment.
     */
    public static MatchFragment newInstance(ArrayList<Integer> blueAllianceTeamIds, ArrayList<Integer> redAllianceTeamIds)
    {
        MatchFragment fragment = new MatchFragment();
        Bundle args = new Bundle();
        args.putIntegerArrayList(ARG_PARAM1, blueAllianceTeamIds);
        args.putIntegerArrayList(ARG_PARAM2, redAllianceTeamIds);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            blueAllianceTeamIds = getArguments().getIntegerArrayList(ARG_PARAM1);
            redAllianceTeamIds = getArguments().getIntegerArrayList(ARG_PARAM2);
        }
    }

    private ViewPager matchViewPager;

    private TabLayout matchTabLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_match, container, false);

        //gets rid of the shadow on the actionbar
        ActionBar actionBar = context.getSupportActionBar();
        actionBar.setElevation(0);

        matchViewPager = view.findViewById(R.id.MatchViewPager);
        matchTabLayout = view.findViewById(R.id.MatchTabLayout);

        MatchViewPagerAdapter matchViewPagerAdapter = new MatchViewPagerAdapter(getChildFragmentManager());

        //populate the viewPager with all the blue alliance teams
        for(int teamId : blueAllianceTeamIds)
        {
            matchViewPagerAdapter.addFragment(ScoutCardFragment.newInstance(-1, teamId), String.valueOf(teamId));
        }

        //populate the viewPager with all the red alliance teams
        for(int teamId : redAllianceTeamIds)
        {
            matchViewPagerAdapter.addFragment(ScoutCardFragment.newInstance(-1, teamId), String.valueOf(teamId));
        }

        matchViewPager.setAdapter(matchViewPagerAdapter);
        matchTabLayout.setupWithViewPager(matchViewPager);

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

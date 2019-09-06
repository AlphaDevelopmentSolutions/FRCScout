package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.Adapters.TeamListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TeamListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TeamListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TeamListFragment extends MasterFragment
{
    static final String ARG_ALLIANCE_COLOR = "ALLIANCE_COLOR";

    private String allianceColorString;

    private OnFragmentInteractionListener mListener;

    public TeamListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TeamListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamListFragment newInstance(@Nullable Match match, @Nullable AllianceColor allianceColor)
    {
        TeamListFragment fragment = new TeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MATCH_JSON, toJson(match));
        args.putString(ARG_ALLIANCE_COLOR, (allianceColor == null) ? AllianceColor.NONE.name() : allianceColor.name());
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
            matchJson = getArguments().getString(ARG_MATCH_JSON);
            allianceColorString = getArguments().getString(ARG_ALLIANCE_COLOR);
        }

        loadTeamsThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                joinLoadingThread();

                if(match != null && allianceColorString != null && !allianceColorString.equals(""))
                    allianceColor = AllianceColor.getColorFromString(allianceColorString);

                //get all teams at event
                teams = event.getTeams(null, null, database);

                //if a match and alliance color was specified,
                //remove any teams that are not in that match or alliance color
                if(match != null && allianceColor != null)
                {
                    for(Team team : new ArrayList<>(teams))
                    {
                        //team not in match / alliance color
                        if(match.getTeamAllianceColor(team) != allianceColor)
                            teams.remove(team);
                    }

                    //add all the teams to the searchedTeams arraylist
                    searchedTeams = new ArrayList<>(teams);
                }
                else
                    //add all the teams to the searchedTeams arraylist
                    searchedTeams = new ArrayList<>(teams);
            }
        });

        loadTeamsThread.start();
    }

    private RecyclerView teamsRecyclerView;

    private EditText teamSearchEditText;

    private ArrayList<Team> teams;
    private ArrayList<Team> searchedTeams;

    private Thread loadTeamsThread;

    private AllianceColor allianceColor;

    private LinearLayout allianceViewPagerLinearLayout;
    private TabLayout allianceTabLayout;
    private ViewPager allianceViewPager;

    private Toolbar searchTeamsToolbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_team_list, container, false);

        //gets rid of the shadow on the actionbar
        context.dropActionBar();

        teamsRecyclerView = view.findViewById(R.id.TeamsRecyclerView);
        teamSearchEditText = view.findViewById(R.id.TeamSearchEditText);
        allianceViewPagerLinearLayout = view.findViewById(R.id.AllianceViewPagerLinearLayout);
        allianceTabLayout = view.findViewById(R.id.AllianceTabLayout);
        allianceViewPager = view.findViewById(R.id.AllianceViewPager);
        searchTeamsToolbar = view.findViewById(R.id.SearchTeamsToolbar);



        //join back up with the load teams thread
        try
        {
            loadTeamsThread.join();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        context.setTitle((match == null) ? event.getName() : match.toString());

        //hide search bar if match is specified
        if(match != null)
            searchTeamsToolbar.setVisibility(View.GONE);

        if(match == null || (allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED))
        {
            final TeamListRecyclerViewAdapter teamListRecyclerViewAdapter = new TeamListRecyclerViewAdapter(match, searchedTeams, context);

            teamsRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            teamsRecyclerView.setAdapter(teamListRecyclerViewAdapter);

            teamSearchEditText.addTextChangedListener(new TextWatcher()
            {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
                {

                }

                @Override
                public void onTextChanged(CharSequence searchText, int start, int before, int count)
                {
                    //You only need to reset the list if you are removing from your search, adding the objects back
                    if (count < before)
                    {
                        //Reset the list
                        for (int i = 0; i < teams.size(); i++)
                        {
                            Team team = teams.get(i);

                            //check if the contact doesn't exist in the viewable list
                            if (!searchedTeams.contains(team))
                            {
                                //add it and notify the recyclerview
                                searchedTeams.add(i, team);
                                teamListRecyclerViewAdapter.notifyItemInserted(i);
                                teamListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedTeams.size());
                            }

                        }
                    }

                    //Delete from the list
                    for (int i = 0; i < searchedTeams.size(); i++)
                    {
                        Team team = searchedTeams.get(i);
                        String name = team.getId() + " - " + team.getName();

                        //If the contacts name doesn't equal the searched name
                        if (!name.toLowerCase().contains(searchText.toString().toLowerCase()))
                        {
                            //remove it from the list and notify the recyclerview
                            searchedTeams.remove(i);
                            teamListRecyclerViewAdapter.notifyItemRemoved(i);
                            teamListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedTeams.size());

                            //this prevents the index from passing the size of the list,
                            //stays on the same index until you NEED to move to the next one
                            i--;
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable)
                {

                }
            });
        }

        //if match specified, setup the viewpager and hide the recyclerview
        else
        {
            allianceViewPagerLinearLayout.setVisibility(View.VISIBLE);
            teamsRecyclerView.setVisibility(View.GONE);

            FragmentViewPagerAdapter viewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());

            viewPagerAdapter.addFragment(TeamListFragment.newInstance(match, AllianceColor.BLUE), getString(R.string.blue_alliance));
            viewPagerAdapter.addFragment(TeamListFragment.newInstance(match, AllianceColor.RED), getString(R.string.red_alliance));

            allianceViewPager.setAdapter(viewPagerAdapter);
            allianceTabLayout.setupWithViewPager(allianceViewPager);
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

package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.alphadevelopmentsolutions.frcscout.Adapters.TeamListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public TeamListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TeamListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TeamListFragment newInstance(String param1, String param2)
    {
        TeamListFragment fragment = new TeamListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        teams = database.getTeams();
        searchedTeams = new ArrayList<>(teams);
    }

    private RecyclerView teamsRecyclerView;

    private EditText teamSearchEditText;

    private ArrayList<Team> teams;
    private ArrayList<Team> searchedTeams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_team_list, container, false);

        teamsRecyclerView = view.findViewById(R.id.TeamsRecyclerView);
        teamSearchEditText = view.findViewById(R.id.TeamSearchEditText);

        final TeamListRecyclerViewAdapter teamListRecyclerViewAdapter = new TeamListRecyclerViewAdapter(searchedTeams, context);

        teamsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
                if(count < before)
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

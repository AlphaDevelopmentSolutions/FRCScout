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

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Adapters.PitCardsRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PitCardListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PitCardListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PitCardListFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private int teamId;

    private OnFragmentInteractionListener mListener;

    public PitCardListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param teamId Parameter 1.
     * @return A new instance of fragment PitCardListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PitCardListFragment newInstance(int teamId)
    {
        PitCardListFragment fragment = new PitCardListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            teamId = getArguments().getInt(ARG_PARAM1);
        }
    }

    private RecyclerView pitCardListRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pit_card_list, container, false);

        MainActivity context = (MainActivity) getActivity();
        Database database = context.getDatabase();

        Team team = new Team(teamId);
        team.load(database);

        pitCardListRecyclerView = view.findViewById(R.id.PitCardListRecyclerView);

        PitCardsRecyclerViewAdapter scoutCardsRecyclerViewAdapter = new PitCardsRecyclerViewAdapter(team, database.getPitCards(team), context);
        pitCardListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        pitCardListRecyclerView.setAdapter(scoutCardsRecyclerViewAdapter);

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
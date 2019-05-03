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

import com.alphadevelopmentsolutions.frcscout.Adapters.ScoutCardsRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardListFragment extends MasterFragment
{
    private OnFragmentInteractionListener mListener;

    public ScoutCardListFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param teamJson json of the team.
     * @return A new instance of fragment ScoutCardListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutCardListFragment newInstance(String teamJson)
    {
        ScoutCardListFragment fragment = new ScoutCardListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_JSON, teamJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            teamJson = getArguments().getString(ARG_TEAM_JSON);
        }
    }

    private RecyclerView scoutCardListRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_list, container, false);

        scoutCardListRecyclerView = view.findViewById(R.id.ScoutCardListRecyclerView);

        joinLoadingThread();

        ScoutCardsRecyclerViewAdapter scoutCardsRecyclerViewAdapter = new ScoutCardsRecyclerViewAdapter(team, gson.toJson(event), team.getScoutCards(event, null, false, database), context);
        scoutCardListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        scoutCardListRecyclerView.setAdapter(scoutCardsRecyclerViewAdapter);

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

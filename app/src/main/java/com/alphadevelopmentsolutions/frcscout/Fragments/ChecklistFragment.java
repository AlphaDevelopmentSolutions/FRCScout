package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphadevelopmentsolutions.frcscout.Adapters.ChecklistItemListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Adapters.MatchListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.ChecklistItem;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;

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

    // TODO: Rename and change types of parameters

    private OnFragmentInteractionListener mListener;

    public ChecklistFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param team
     * @param match
     * @return A new instance of fragment ChecklistFragment.
     */
    public static ChecklistFragment newInstance(@NonNull Team team, @Nullable Match match)
    {
        ChecklistFragment fragment = new ChecklistFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEAM_JSON, toJson(team));
        args.putString(ARG_MATCH_JSON, toJson(match));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view;

        RecyclerView recyclerView;

        joinLoadingThread();

        //no match selected, show match list
        if (match == null)
        {
            view = inflater.inflate(R.layout.fragment_match_list, container, false);

            recyclerView = view.findViewById(R.id.MatchListRecyclerView);

            MatchListRecyclerViewAdapter matchListRecyclerViewAdapter = new MatchListRecyclerViewAdapter(event, team, context, (this).getClass());
            recyclerView.setAdapter(matchListRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            context.setTitle(event.getName());
        }

        //match and event selected, show the checklist
        else
        {
            view = inflater.inflate(R.layout.fragment_checklist, container, false);

            recyclerView = view.findViewById(R.id.ChecklistItemsRecyclerView);

            ChecklistItemListRecyclerViewAdapter checklistItemListRecyclerViewAdapter = new ChecklistItemListRecyclerViewAdapter(match, ChecklistItem.getChecklistItems(null, database), context);
            recyclerView.setAdapter(checklistItemListRecyclerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            context.setTitle(match.toString());
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

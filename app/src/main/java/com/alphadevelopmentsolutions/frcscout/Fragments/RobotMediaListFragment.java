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

import com.alphadevelopmentsolutions.frcscout.Adapters.RobotMediaListRecyclerViewAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RobotMediaListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RobotMediaListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RobotMediaListFragment extends MasterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String teamJson;


    private OnFragmentInteractionListener mListener;

    public RobotMediaListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param teamJson json for a team.
     * @return A new instance of fragment RobotMediaListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RobotMediaListFragment newInstance(String teamJson) {
        RobotMediaListFragment fragment = new RobotMediaListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, teamJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            teamJson = getArguments().getString(ARG_PARAM1);
        }

        team = new Gson().fromJson(teamJson, Team.class);
    }

    private RecyclerView robotMediaRecyclerView;

    private Team team;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_robot_media_list, container, false);

        robotMediaRecyclerView = view.findViewById(R.id.RobotMediaRecyclerView);

        new Thread(new Runnable() {
            @Override
            public void run() {


                final RobotMediaListRecyclerViewAdapter robotMediaListRecyclerViewAdapter = new RobotMediaListRecyclerViewAdapter(team, database.getRobotMedia(team, false), context);

                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        robotMediaRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        robotMediaRecyclerView.setAdapter(robotMediaListRecyclerViewAdapter);
                    }
                });

            }
        }).start();


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

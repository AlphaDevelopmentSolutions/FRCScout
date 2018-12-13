package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardFragment extends Fragment
{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "ScoutCardId";

    private int scoutCardId;

    private OnFragmentInteractionListener mListener;

    public ScoutCardFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCardId scout card ID
     * @return A new instance of fragment ScoutCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutCardFragment newInstance(int scoutCardId)
    {
        ScoutCardFragment fragment = new ScoutCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, scoutCardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            scoutCardId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card, container, false);

        ArrayList<String> teamNumbers = new ArrayList<>();
        teamNumbers.add("5885");
        teamNumbers.add("1234");
        teamNumbers.add("610");
        teamNumbers.add("180");
        teamNumbers.add("772");
        teamNumbers.add("771");

        ArrayList<String> scouterNames = new ArrayList<>();
        teamNumbers.add("Griffin Sorrentino");
        teamNumbers.add("Bob Hedrick");
        teamNumbers.add("Stacey Greenwood");
        teamNumbers.add("Scott Pilgrim");
        teamNumbers.add("Pedro De Pezia");
        teamNumbers.add("Alex Abruzezezezezezeze");
        teamNumbers.add("Dan Cordario");
        teamNumbers.add("Kathleen Beach");

        ArrayAdapter<String> teamNumbersAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, teamNumbers);
        AutoCompleteTextView teamNumberAutoCompleteTextView = view.findViewById(R.id.TeamNumberAutoCompleteTextView);
        teamNumberAutoCompleteTextView.setAdapter(teamNumbersAdapter);

        ArrayAdapter<String> scouterNameAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, teamNumbers);
        AutoCompleteTextView scouterNameAutoCompleteTextView = view.findViewById(R.id.ScouterNameAutoCompleteTextView);
        scouterNameAutoCompleteTextView.setAdapter(scouterNameAdapter);

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

package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardEndGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardEndGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardEndGameFragment extends MasterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int scoutCardId;

    private OnFragmentInteractionListener mListener;

    public ScoutCardEndGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCardId Scout card id
     * @return A new instance of fragment ScoutCardEndGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutCardEndGameFragment newInstance(int scoutCardId) {
        ScoutCardEndGameFragment fragment = new ScoutCardEndGameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, scoutCardId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scoutCardId = getArguments().getInt(ARG_PARAM1);
        }
    }

    private ScoutCard scoutCard;

    //Exit Habitat
    private TextView endGameReturnedToHabitatTextView;
    private TextView endGameReturnedToHabitatAttemptsTextView;

    private Button endGameReturnedToHabitatNoButton;
    private Button endGameReturnedToHabitatLevelOneButton;
    private Button endGameReturnedToHabitatLevelTwoButton;
    private Button endGameReturnedToHabitatLevelThreeButton;

    private Button endGameReturnedToHabitatAttemptsNoButton;
    private Button endGameReturnedToHabitatAttemptsLevelOneButton;
    private Button endGameReturnedToHabitatAttemptsLevelTwoButton;
    private Button endGameReturnedToHabitatAttemptsLevelThreeButton;

    private int returnedToHabLevel;
    private int returnedToHabAttemptLevel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_end_game, container, false);

        //load the scoutcard if passed
        if(scoutCardId > 0)
        {
            scoutCard = new ScoutCard(scoutCardId);
            scoutCard.load(database);
        }

        //Exit Habitat
        endGameReturnedToHabitatTextView = view.findViewById(R.id.EndGameReturnedToHabitatTextView);
        endGameReturnedToHabitatAttemptsTextView = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsTextView);

        endGameReturnedToHabitatNoButton = view.findViewById(R.id.EndGameReturnedToHabitatNoButton);
        endGameReturnedToHabitatAttemptsNoButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsNoButton);
        endGameReturnedToHabitatLevelOneButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelOneButton);
        endGameReturnedToHabitatAttemptsLevelOneButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsLevelOneButton);
        endGameReturnedToHabitatLevelTwoButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelTwoButton);
        endGameReturnedToHabitatAttemptsLevelTwoButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsLevelTwoButton);
        endGameReturnedToHabitatLevelThreeButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelThreeButton);
        endGameReturnedToHabitatAttemptsLevelThreeButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsLevelThreeButton);

        endGameReturnedToHabitatNoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.no);
                returnedToHabLevel = 0;
            }
        });

        endGameReturnedToHabitatLevelOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.level_1);
                returnedToHabLevel = 1;
            }
        });

        endGameReturnedToHabitatLevelTwoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.level_2);
                returnedToHabLevel = 2;
            }
        });

        endGameReturnedToHabitatLevelThreeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.level_3);
                returnedToHabLevel = 3;
            }
        });

        endGameReturnedToHabitatAttemptsNoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatAttemptsTextView.setText(R.string.no);
                returnedToHabAttemptLevel = 0;
            }
        });

        endGameReturnedToHabitatAttemptsLevelOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatAttemptsTextView.setText(R.string.level_1);
                returnedToHabAttemptLevel = 1;
            }
        });

        endGameReturnedToHabitatAttemptsLevelTwoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatAttemptsTextView.setText(R.string.level_2);
                returnedToHabAttemptLevel = 2;
            }
        });

        endGameReturnedToHabitatAttemptsLevelThreeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatAttemptsTextView.setText(R.string.level_3);
                returnedToHabAttemptLevel = 3;
            }
        });


        return view;
    }

    //region Getters

    public int getReturnedToHabLevel()
    {
        return returnedToHabLevel;
    }

    public int getReturnedToHabAttemptLevel()
    {
        return returnedToHabAttemptLevel;
    }


    //endregion

    /**
     * Validates all fields on the form for saving
     * @return boolean if fields valid
     */
    public boolean validateFields()
    {
        //placeholder in case of future validation needed
        return true;
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

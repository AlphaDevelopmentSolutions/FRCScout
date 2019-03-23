package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardPostGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardPostGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardPostGameFragment extends MasterFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int scoutCardId;

    private OnFragmentInteractionListener mListener;

    public ScoutCardPostGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCardId Scout card id
     * @return A new instance of fragment ScoutCardPostGameFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutCardPostGameFragment newInstance(int scoutCardId) {
        ScoutCardPostGameFragment fragment = new ScoutCardPostGameFragment();
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

    private Button scoutCardSaveButton;

    private View.OnClickListener onSaveButtonClickListener;

    private EditText blueAllianceFinalScoreEditText;
    private EditText redAllianceFinalScoreEditText;
    private EditText matchNotesEditText;

    private RatingBar offenseRatingBar;
    private RatingBar defenseRatingBar;
    private RatingBar driveRatingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_post_game, container, false);

        //load the scoutcard if passed
        if(scoutCardId > 0)
        {
            scoutCard = new ScoutCard(scoutCardId);
            scoutCard.load(database);
        }

        scoutCardSaveButton = view.findViewById(R.id.ScoutCardSaveButton);

        blueAllianceFinalScoreEditText = view.findViewById(R.id.BlueAllianceFinalScoreEditText);
        redAllianceFinalScoreEditText = view.findViewById(R.id.RedAllianceFinalScoreEditText);
        matchNotesEditText = view.findViewById(R.id.MatchNotesEditText);

        offenseRatingBar = view.findViewById(R.id.OffenseRatingBar);
        defenseRatingBar = view.findViewById(R.id.DefenseRatingBar);
        driveRatingBar = view.findViewById(R.id.DriveRatingBar);

        if(onSaveButtonClickListener != null)
            scoutCardSaveButton.setOnClickListener(onSaveButtonClickListener);

        if(scoutCard != null)
            if(!scoutCard.isDraft())
                scoutCardSaveButton.setVisibility(View.GONE);

        return view;
    }

    //region Getters

    public int getBlueAllianceScore()
    {
        return Integer.parseInt(blueAllianceFinalScoreEditText.getText().toString());
    }

    public int getRedAllianceScore()
    {
        return Integer.parseInt(redAllianceFinalScoreEditText.getText().toString());
    }

    public int getDefenseRating()
    {
        return defenseRatingBar.getNumStars();
    }

    public int getOffenseRating()
    {
        return offenseRatingBar.getNumStars();
    }

    public int getDriveRating()
    {
        return driveRatingBar.getNumStars();
    }

    public String getNotes()
    {
        return matchNotesEditText.getText().toString();
    }

    //endregion




    /**
     * Validates all fields on the form for saving
     * @return boolean if fields valid
     */
    public boolean validateFields()
    {
        if(!blueAllianceFinalScoreEditText.getText().toString().matches("^[-+]?\\d*$")
                || blueAllianceFinalScoreEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid blue alliance score.");
            return false;
        }

        if(!redAllianceFinalScoreEditText.getText().toString().matches("^[-+]?\\d*$")
                || redAllianceFinalScoreEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid red alliance score.");
            return false;
        }

        return true;
    }

    /**
     * Stores the save button click listener used for calling upwards to the scoutcard frag
     * @param onSaveButtonClickListener used to set scout card save button
     */
    public void setOnSaveButtonClickListener(View.OnClickListener onSaveButtonClickListener)
    {
        this.onSaveButtonClickListener = onSaveButtonClickListener;
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
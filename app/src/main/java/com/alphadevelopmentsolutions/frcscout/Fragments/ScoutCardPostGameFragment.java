package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;

import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardPostGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardPostGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardPostGameFragment extends MasterFragment {

    private OnFragmentInteractionListener mListener;

    public ScoutCardPostGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCard
     * @return A new instance of fragment ScoutCardPostGameFragment.
     */
    public static ScoutCardPostGameFragment newInstance(@Nullable ScoutCard scoutCard) {
        ScoutCardPostGameFragment fragment = new ScoutCardPostGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_SCOUT_CARD_JSON, toJson(scoutCard));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private View.OnClickListener onSaveButtonClickListener;

    private EditText matchNotesEditText;

    private RatingBar offenseRatingBar;
    private RatingBar defenseRatingBar;
    private RatingBar driveRatingBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_post_game, container, false);


        matchNotesEditText = view.findViewById(R.id.MatchNotesEditText);

        defenseRatingBar = view.findViewById(R.id.DefenseRatingBar);
        offenseRatingBar = view.findViewById(R.id.OffenseRatingBar);
        driveRatingBar = view.findViewById(R.id.DriveRatingBar);

        joinLoadingThread();

        if(scoutCard != null)
        {

            defenseRatingBar.setRating(scoutCard.getDefenseRating());
            offenseRatingBar.setRating(scoutCard.getOffenseRating());
            driveRatingBar.setRating(scoutCard.getDriveRating());

            matchNotesEditText.setText(scoutCard.getNotes());

            //only disable if card is not draft
            if(!scoutCard.isDraft())
            {
                defenseRatingBar.setEnabled(false);
                offenseRatingBar.setEnabled(false);
                driveRatingBar.setEnabled(false);

                matchNotesEditText.setFocusable(false);
                matchNotesEditText.setInputType(InputType.TYPE_NULL);

            }
        }

        return view;
    }

    //region Getters

    public int getDefenseRating()
    {
        return (int) defenseRatingBar.getRating();
    }

    public int getOffenseRating()
    {
        return (int) offenseRatingBar.getRating();
    }

    public int getDriveRating()
    {
        return (int) driveRatingBar.getRating();
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

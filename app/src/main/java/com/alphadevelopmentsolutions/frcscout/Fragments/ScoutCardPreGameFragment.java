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
import android.widget.EditText;
import android.widget.Spinner;

import com.alphadevelopmentsolutions.frcscout.Classes.AllianceColor;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Classes.StartingPosition;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardPreGameFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardPreGameFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardPreGameFragment extends MasterFragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private int scoutCardId;
    private int teamId;

    private OnFragmentInteractionListener mListener;

    public ScoutCardPreGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCardId Scout card id
     * @param teamId Team id
     * @return A new instance of fragment ScoutCardPreGameFragment.
     */
    public static ScoutCardPreGameFragment newInstance(int scoutCardId, int teamId) {
        ScoutCardPreGameFragment fragment = new ScoutCardPreGameFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, scoutCardId);
        args.putInt(ARG_PARAM2, teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            scoutCardId = getArguments().getInt(ARG_PARAM1);
            teamId = getArguments().getInt(ARG_PARAM2);
        }
    }

    private ScoutCard scoutCard;

    private AutoCompleteTextView teamNumberAutoCompleteTextView;
    private AutoCompleteTextView scouterNameAutoCompleteTextView;

    private EditText matchIdEditText;

    private Spinner allianceColorSpinner;
    private Spinner startingPositionSpinner;
    private Spinner startingLevelSpinner;
    private Spinner startingPieceSpinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_pre_game, container, false);

        //load the scoutcard if passed
        if(scoutCardId > 0)
        {
            scoutCard = new ScoutCard(scoutCardId);
            scoutCard.load(database);
        }

        teamNumberAutoCompleteTextView = view.findViewById(R.id.TeamNumberAutoCompleteTextView);
        scouterNameAutoCompleteTextView = view.findViewById(R.id.ScouterNameAutoCompleteTextView);

        matchIdEditText = view.findViewById(R.id.MatchIdEditText);

        allianceColorSpinner = view.findViewById(R.id.AllianceColorSpinner);
        startingPositionSpinner = view.findViewById(R.id.StartingPositionSpinner);
        startingLevelSpinner = view.findViewById(R.id.StartingLevelSpinner);
        startingPieceSpinner = view.findViewById(R.id.StartingGamePieceSpinner);

        teamNumberAutoCompleteTextView.setText(String.valueOf(teamId));

        //populate the scouter name auto complete textview
        ArrayList<String> scouterNames = new ArrayList<>();

        for(User user : database.getUsers())
            scouterNames.add(user.getName());

        ArrayAdapter<String> scouterNameAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, scouterNames);
        scouterNameAutoCompleteTextView.setAdapter(scouterNameAdapter);

        //scoutcard loaded, populate fields
        if(scoutCard != null)
        {
            teamNumberAutoCompleteTextView.setText(String.valueOf(scoutCard.getTeamId()));
            scouterNameAutoCompleteTextView.setText(scoutCard.getCompletedBy());

            matchIdEditText.setText(String.valueOf(scoutCard.getMatchId()));

            allianceColorSpinner.setSelection(scoutCard.getAllianceColor().equals(AllianceColor.RED) ? 0 : 1);
            startingLevelSpinner.setSelection(scoutCard.getPreGameStartingLevel());
            startingPositionSpinner.setSelection(scoutCard.getPreGameStartingPosition().equals(StartingPosition.LEFT) ? 0 : (scoutCard.getPreGameStartingPosition().equals(StartingPosition.CENTER)) ? 1 : 2);
            startingPieceSpinner.setSelection(scoutCard.getPreGameStartingPiece().equals(StartingPiece.HATCH) ? 0 : 1);
        }

        if(teamId > 0)
            teamNumberAutoCompleteTextView.setText(String.valueOf(teamId));

        return view;
    }

    //region Getters

    public String getScouterName()
    {
        return scouterNameAutoCompleteTextView.getText().toString();
    }

    public int getMatchNumber()
    {
        return Integer.parseInt(matchIdEditText.getText().toString());
    }

    public String getAllianceColor()
    {
        return allianceColorSpinner.getSelectedItem().toString();
    }

    public StartingPosition getStartingPosition()
    {
        return StartingPosition.getPositionFromString(startingPositionSpinner.getSelectedItem().toString());
    }

    public int getStartingLevel()
    {
        return Integer.parseInt(startingLevelSpinner.getSelectedItem().toString().substring(0, startingLevelSpinner.getSelectedItem().toString().indexOf(" ")));
    }

    public StartingPiece getStartingPiece()
    {
        return StartingPiece.getPieceFromString(startingPieceSpinner.getSelectedItem().toString());
    }

    //endregion

    public boolean validateFields()
    {
        if(!teamNumberAutoCompleteTextView.getText().toString().matches("^[-+]?\\d*$")
            || teamNumberAutoCompleteTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid team number.");
            return false;
        }

        if(scouterNameAutoCompleteTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid scouter name.");
            return false;
        }

        if(!matchIdEditText.getText().toString().matches("^[-+]?\\d*$")
            || matchIdEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid match number.");
            return false;
        }

        return true;
    }

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

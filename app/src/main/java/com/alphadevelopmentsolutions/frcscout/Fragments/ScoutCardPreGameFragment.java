package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

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

    private OnFragmentInteractionListener mListener;

    public ScoutCardPreGameFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param scoutCard
     * @param team
     * @return A new instance of fragment ScoutCardPreGameFragment.
     */
    public static ScoutCardPreGameFragment newInstance(@Nullable ScoutCard scoutCard, @NonNull Team team) {
        ScoutCardPreGameFragment fragment = new ScoutCardPreGameFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_SCOUT_CARD_JSON, toJson(scoutCard));
        args.putString(ARG_TEAM_JSON, toJson(team));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //load the user auto complete data on a new thread
        loadScouterNamesThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                joinLoadingThread();

                //populate the scouter name auto complete textview
                scouterNames = new ArrayList<>();

                //get all users
                for(User user : User.getUsers(null, database))
                    scouterNames.add(user.toString());

                scouterNameAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, scouterNames);
            }
        });
        loadScouterNamesThread.start();
    }

    private TextInputEditText teamNumberTextInputEditText;
    private AutoCompleteTextView scouterNameAutoCompleteTextView;

    private Spinner startingPositionSpinner;
    private Spinner startingLevelSpinner;
    private Spinner startingPieceSpinner;

    private ArrayList<String> scouterNames;

    private Thread loadScouterNamesThread;
    private ArrayAdapter<String> scouterNameAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card_pre_game, container, false);

        teamNumberTextInputEditText = view.findViewById(R.id.TeamNumberTextInputEditText);
        scouterNameAutoCompleteTextView = view.findViewById(R.id.ScouterNameAutoCompleteTextView);

        startingPositionSpinner = view.findViewById(R.id.StartingPositionSpinner);
        startingLevelSpinner = view.findViewById(R.id.StartingLevelSpinner);
        startingPieceSpinner = view.findViewById(R.id.StartingGamePieceSpinner);

        teamNumberTextInputEditText.setText(String.valueOf(team.getId()));
        teamNumberTextInputEditText.setFocusable(false);
        teamNumberTextInputEditText.setInputType(InputType.TYPE_NULL);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    loadScouterNamesThread.join();

                    context.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            scouterNameAutoCompleteTextView.setAdapter(scouterNameAdapter);
                        }
                    });
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

            }
        }).start();

        //scoutcard loaded, populate fields
        if(scoutCard != null)
        {
            scouterNameAutoCompleteTextView.setText(scoutCard.getCompletedBy());

            startingLevelSpinner.setSelection(scoutCard.getPreGameStartingLevel() - 1);
            startingPositionSpinner.setSelection(scoutCard.getPreGameStartingPosition().equals(StartingPosition.LEFT) ? 0 : (scoutCard.getPreGameStartingPosition().equals(StartingPosition.CENTER)) ? 1 : 2);
            startingPieceSpinner.setSelection(scoutCard.getPreGameStartingPiece().equals(StartingPiece.HATCH) ? 0 : 1);

            //only disable fields if card is not draft
            if(!scoutCard.isDraft())
            {
                scouterNameAutoCompleteTextView.setFocusable(false);
                scouterNameAutoCompleteTextView.setInputType(InputType.TYPE_NULL);

                startingLevelSpinner.setEnabled(false);
                startingPositionSpinner.setEnabled(false);
                startingPieceSpinner.setEnabled(false);

            }
        }


        return view;
    }

    //region Getters

    public int getTeamId()
    {
        return Integer.parseInt(teamNumberTextInputEditText.getText().toString());
    }

    public String getScouterName()
    {
        return scouterNameAutoCompleteTextView.getText().toString();
    }


    public StartingPosition getStartingPosition()
    {
        return StartingPosition.getPositionFromString(startingPositionSpinner.getSelectedItem().toString());
    }

    public int getStartingLevel()
    {
        return Integer.parseInt(startingLevelSpinner.getSelectedItem().toString().substring(startingLevelSpinner.getSelectedItem().toString().indexOf(" ") + 1));
    }

    public StartingPiece getStartingPiece()
    {
        return StartingPiece.getPieceFromString(startingPieceSpinner.getSelectedItem().toString());
    }

    //endregion

    public boolean validateFields()
    {
        if(!teamNumberTextInputEditText.getText().toString().matches("^[-+]?\\d*$")
            || teamNumberTextInputEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid team number.");
            return false;
        }

        if(scouterNameAutoCompleteTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid scouter name.");
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

package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PitCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PitCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PitCardFragment extends MasterFragment
{
    private OnFragmentInteractionListener mListener;

    public PitCardFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pitCard
     * @param team
     * @return A new instance of fragment PitCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PitCardFragment newInstance(@Nullable PitCard pitCard, @NonNull Team team)
    {
        PitCardFragment fragment = new PitCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM_PIT_CARD_JSON, toJson(pitCard));
        args.putString(ARG_TEAM_JSON, toJson(team));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }
    
    private AutoCompleteTextView teamNumberAutoCompleteTextView;
    private AutoCompleteTextView scouterNameAutoCompleteTextView;
    
    private EditText driveStyleEditText;
    private EditText robotWeightEditText;
    private EditText robotLengthEditText;
    private EditText robotWidthEditText;
    private EditText robotHeightEditText;

    private EditText autoExitHabitatEditText;
    private EditText autoHatchEditText;
    private EditText autoCargoEditText;

    private EditText teleopHatchEditText;
    private EditText teleopCargoEditText;

    private EditText returnedToHabitatEditText;

    private EditText notesEditText;
    
    private Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pit_card, container, false);

        //gets rid of the shadow on the actionbar
        context.dropActionBar();

        teamNumberAutoCompleteTextView = view.findViewById(R.id.TeamNumberTextInputEditText);
        scouterNameAutoCompleteTextView = view.findViewById(R.id.ScouterNameAutoCompleteTextView);

        driveStyleEditText = view.findViewById(R.id.DriveStyleEditText);
        robotWeightEditText = view.findViewById(R.id.RobotWeightEditText);
        robotLengthEditText = view.findViewById(R.id.RobotLengthEditText);
        robotWidthEditText = view.findViewById(R.id.RobotWidthEditText);
        robotHeightEditText = view.findViewById(R.id.RobotHeightEditText);

        autoExitHabitatEditText = view.findViewById(R.id.AutoExitHabitatEditText);
        autoHatchEditText = view.findViewById(R.id.AutoHatchEditText);
        autoCargoEditText = view.findViewById(R.id.AutoCargoEditText);

        teleopHatchEditText = view.findViewById(R.id.TeleopHatchEditText);
        teleopCargoEditText = view.findViewById(R.id.TeleopCargoEditText);

        returnedToHabitatEditText = view.findViewById(R.id.ReturnedToHabitatEditText);

        notesEditText = view.findViewById(R.id.NotesEditText);

        saveButton = view.findViewById(R.id.SaveButton);

        joinLoadingThread();
        
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validateFields())
                {

                    int teamNumber = Integer.parseInt(teamNumberAutoCompleteTextView.getText().toString());
                    String eventId = (pitCard == null) ? event.getBlueAllianceId() : pitCard.getEventId();

                    String driveStyle = driveStyleEditText.getText().toString();
                    String robotWeight = robotWeightEditText.getText().toString();
                    String robotLength = robotLengthEditText.getText().toString();
                    String robotWidth = robotWidthEditText.getText().toString();
                    String robotHeight = robotHeightEditText.getText().toString();

                    String autonomousExitHabitat = autoExitHabitatEditText.getText().toString();
                    String autonomousHatchPanelsSecured = autoHatchEditText.getText().toString();
                    String autonomousCargoStored = autoCargoEditText.getText().toString();

                    String teleopHatchPanelsSecured = teleopHatchEditText.getText().toString();
                    String teleopCargoStored = teleopCargoEditText.getText().toString();

                    String endGameReturnedToHabitat = returnedToHabitatEditText.getText().toString();
                    String notes = notesEditText.getText().toString();

                    String scouterName = scouterNameAutoCompleteTextView.getText().toString();

                    //pitcard is a draft
                    if (pitCard != null)
                    {
                        pitCard.setTeamId(teamNumber);
                        pitCard.setEventId(eventId);

                        pitCard.setDriveStyle(driveStyle);
                        pitCard.setRobotWeight(robotWeight);
                        pitCard.setRobotLength(robotLength);
                        pitCard.setRobotWidth(robotWidth);
                        pitCard.setRobotHeight(robotHeight);

                        pitCard.setAutoExitHabitat(autonomousExitHabitat);
                        pitCard.setAutoHatch(autonomousHatchPanelsSecured);
                        pitCard.setAutoCargo(autonomousCargoStored);

                        pitCard.setTeleopHatch(teleopHatchPanelsSecured);
                        pitCard.setTeleopCargo(teleopCargoStored);

                        pitCard.setReturnToHabitat(endGameReturnedToHabitat);

                        pitCard.setNotes(notes);

                        pitCard.setCompletedBy(scouterName);

                        if (pitCard.save(database) > 0)
                        {
                            context.showSnackbar("Saved Successfully.");
                            context.getSupportFragmentManager().popBackStackImmediate();
                        }


                    }
                    //new pitcard
                    else
                    {
                        PitCard pitCard = new PitCard(
                                -1,
                                teamNumber,
                                eventId,

                                driveStyle,
                                robotWeight,
                                robotLength,
                                robotWidth,
                                robotHeight,

                                autonomousExitHabitat,
                                autonomousHatchPanelsSecured,
                                autonomousCargoStored,

                                teleopHatchPanelsSecured,
                                teleopCargoStored,

                                endGameReturnedToHabitat,

                                notes,

                                scouterName,
                                true);
                        if (pitCard.save(database) > 0)
                        {
                            context.showSnackbar("Saved Successfully.");
                            context.getSupportFragmentManager().popBackStackImmediate();
                        }
                    }
                }
            }
        });


        teamNumberAutoCompleteTextView.setText(String.valueOf(team.getId()));
        teamNumberAutoCompleteTextView.setFocusable(false);
        teamNumberAutoCompleteTextView.setInputType(InputType.TYPE_NULL);


        ArrayList<String> scouterNames = new ArrayList<>();

        for(User user : database.getUsers())
            scouterNames.add(user.toString());

        ArrayAdapter<String> scouterNameAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, scouterNames);
        scouterNameAutoCompleteTextView.setAdapter(scouterNameAdapter);

        //pit card sent over, disable all fields and populate data
        if(pitCard != null)
        {
            teamNumberAutoCompleteTextView.setText(String.valueOf(pitCard.getTeamId()));
            scouterNameAutoCompleteTextView.setText(pitCard.getCompletedBy());

            driveStyleEditText.setText(String.valueOf(pitCard.getDriveStyle()));

            robotWeightEditText.setText(String.valueOf(pitCard.getRobotWeight()));
            robotLengthEditText.setText(String.valueOf(pitCard.getRobotLength()));
            robotWidthEditText.setText(String.valueOf(pitCard.getRobotWidth()));
            robotHeightEditText.setText(String.valueOf(pitCard.getRobotHeight()));

            autoExitHabitatEditText.setText(pitCard.getAutoExitHabitat());
            autoHatchEditText.setText(String.valueOf(pitCard.getAutoHatch()));
            autoCargoEditText.setText(String.valueOf(pitCard.getAutoCargo()));

            teleopHatchEditText.setText(String.valueOf(pitCard.getTeleopHatch()));
            teleopCargoEditText.setText(String.valueOf(pitCard.getTeleopCargo()));

            returnedToHabitatEditText.setText(pitCard.getReturnToHabitat());

            notesEditText.setText(pitCard.getNotes());


            //only disable fields if card is not draft
            if(!pitCard.isDraft())
            {
                saveButton.setVisibility(View.GONE);

                scouterNameAutoCompleteTextView.setFocusable(false);
                scouterNameAutoCompleteTextView.setInputType(InputType.TYPE_NULL);

                driveStyleEditText.setFocusable(false);
                driveStyleEditText.setInputType(InputType.TYPE_NULL);

                robotWeightEditText.setFocusable(false);
                robotWeightEditText.setInputType(InputType.TYPE_NULL);

                robotLengthEditText.setFocusable(false);
                robotLengthEditText.setInputType(InputType.TYPE_NULL);

                robotWidthEditText.setFocusable(false);
                robotWidthEditText.setInputType(InputType.TYPE_NULL);

                robotHeightEditText.setFocusable(false);
                robotHeightEditText.setInputType(InputType.TYPE_NULL);

                autoExitHabitatEditText.setFocusable(false);
                autoExitHabitatEditText.setInputType(InputType.TYPE_NULL);

                autoHatchEditText.setFocusable(false);
                autoHatchEditText.setInputType(InputType.TYPE_NULL);

                autoCargoEditText.setFocusable(false);
                autoCargoEditText.setInputType(InputType.TYPE_NULL);

                teleopHatchEditText.setFocusable(false);
                teleopHatchEditText.setInputType(InputType.TYPE_NULL);

                teleopCargoEditText.setFocusable(false);
                teleopCargoEditText.setInputType(InputType.TYPE_NULL);

                returnedToHabitatEditText.setFocusable(false);
                returnedToHabitatEditText.setInputType(InputType.TYPE_NULL);

                notesEditText.setFocusable(false);
                notesEditText.setInputType(InputType.TYPE_NULL);
            }
        }

        return view;
    }

    /**
     * Validates all fields are either filled in or filled in with correct data
     * @return boolean if fields valid
     */
    private boolean validateFields()
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

        if(driveStyleEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid drivetrain.");
            return false;
        }

        if(robotWeightEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid robot weight.");
            return false;
        }

        if(autoExitHabitatEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid autonomous exit habitat info.");
            return false;
        }

        if(autoHatchEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid autonomous hatch panels info.");
            return false;
        }

        if(autoCargoEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid autonomous cargo stored info.");
            return false;
        }

        if(teleopHatchEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid teleop hatch panels secured info.");
            return false;
        }

        if(teleopCargoEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid teleop cargo stored info.");
            return false;
        }

        if(returnedToHabitatEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid end game returned to habitat info.");
            return false;
        }

        return true;
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

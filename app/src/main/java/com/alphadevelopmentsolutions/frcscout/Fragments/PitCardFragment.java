package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;

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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "PitCardId";
    private static final String ARG_PARAM2 = "TeamId";

    private int pitCardId;
    private int teamId;

    private OnFragmentInteractionListener mListener;

    public PitCardFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param pitCardId Parameter 1.
     * @param teamId Parameter 2.
     * @return A new instance of fragment PitCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PitCardFragment newInstance(int pitCardId, int teamId)
    {
        PitCardFragment fragment = new PitCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, pitCardId);
        args.putInt(ARG_PARAM2, teamId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            pitCardId = getArguments().getInt(ARG_PARAM1);
            teamId = getArguments().getInt(ARG_PARAM2);
        }
    }
    
    private AutoCompleteTextView teamNumberAutoCompleteTextView;
    private AutoCompleteTextView scouterNameAutoCompleteTextView;
    
    private EditText driveStyleEditText;
    private EditText robotWeightEditText;

    private EditText autoExitHabitatEditText;
    private EditText autoHatchEditText;
    private EditText autoCargoEditText;

    private EditText teleopHatchEditText;
    private EditText teleopCargoEditText;

    private EditText returnedToHabitatEditText;

    private EditText notesEditText;
    
    private Button saveButton;

    private PitCard pitCard;
    private Team team;

    private MainActivity context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pit_card, container, false);
        
        if(pitCardId > 0)
        {
            pitCard = new PitCard(pitCardId);
            pitCard.load(database);
        }
        
        if(teamId > 0)
        {
            team = new Team(teamId);
            team.load(database);
        }


        teamNumberAutoCompleteTextView = view.findViewById(R.id.TeamNumberAutoCompleteTextView);
        scouterNameAutoCompleteTextView = view.findViewById(R.id.ScouterNameAutoCompleteTextView);

        driveStyleEditText = view.findViewById(R.id.DriveStyleEditText);
        robotWeightEditText = view.findViewById(R.id.RobotWeightEditText);

        autoExitHabitatEditText = view.findViewById(R.id.AutoExitHabitatEditText);
        autoHatchEditText = view.findViewById(R.id.AutoHatchEditText);
        autoCargoEditText = view.findViewById(R.id.AutoCargoEditText);

        teleopHatchEditText = view.findViewById(R.id.TeleopHatchEditText);
        teleopCargoEditText = view.findViewById(R.id.TeleopCargoEditText);

        returnedToHabitatEditText = view.findViewById(R.id.ReturnedToHabitatEditText);

        notesEditText = view.findViewById(R.id.NotesEditText);

        saveButton = view.findViewById(R.id.SaveButton);

        if(pitCard != null)
            if(!pitCard.isDraft())
                saveButton.setVisibility(View.GONE);
        
        saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(validateFields())
                {

                    int teamNumber = Integer.parseInt(teamNumberAutoCompleteTextView.getText().toString());
                    String eventId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.EVENT_ID_PREF, "");

                    String driveStyle = driveStyleEditText.getText().toString();
                    String robotWeight = robotWeightEditText.getText().toString();

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

        if(team != null)
            teamNumberAutoCompleteTextView.setText(String.valueOf(team.getId()));

        ArrayList<String> scouterNames = new ArrayList<>();

        for(User user : database.getUsers())
            scouterNames.add(user.getName());

        ArrayAdapter<String> scouterNameAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, scouterNames);
        scouterNameAutoCompleteTextView.setAdapter(scouterNameAdapter);
        
        if(pitCard != null)
        {
            teamNumberAutoCompleteTextView.setText(String.valueOf(pitCard.getTeamId()));
            scouterNameAutoCompleteTextView.setText(pitCard.getCompletedBy());

            driveStyleEditText.setText(String.valueOf(pitCard.getDriveStyle()));
            robotWeightEditText.setText(String.valueOf(pitCard.getRobotWeight()));

            autoExitHabitatEditText.setText(pitCard.getAutoExitHabitat());
            autoHatchEditText.setText(String.valueOf(pitCard.getAutoHatch()));
            autoCargoEditText.setText(String.valueOf(pitCard.getAutoCargo()));

            teleopHatchEditText.setText(String.valueOf(pitCard.getTeleopHatch()));
            teleopCargoEditText.setText(String.valueOf(pitCard.getTeleopCargo()));

            returnedToHabitatEditText.setText(pitCard.getReturnToHabitat());

            notesEditText.setText(pitCard.getNotes());
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

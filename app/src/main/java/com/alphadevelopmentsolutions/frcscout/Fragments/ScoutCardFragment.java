package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Database;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;
import java.util.Date;

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
    private static final String ARG_PARAM2 = "TeamId";

    private int scoutCardId;
    private int teamId;

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
     * @param teamId teamId
     * @return A new instance of fragment ScoutCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutCardFragment newInstance(int scoutCardId, int teamId)
    {
        ScoutCardFragment fragment = new ScoutCardFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, scoutCardId);
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
            scoutCardId = getArguments().getInt(ARG_PARAM1);
            teamId = getArguments().getInt(ARG_PARAM2);
        }
    }
    
    private AutoCompleteTextView teamNumberAutoCompleteTextView;
    private AutoCompleteTextView scouterNameAutoCompleteTextView;

    private EditText matchIdEditText;
    private EditText blueAllianceFinalScoreEditText;
    private EditText redAllianceFinalScoreEditText;
    private EditText matchNotesEditText;
    
    private Button scoutCardSaveButton;

    private ScoutCard scoutCard;

    private Team team;

    private MainActivity context;

    //region Autonomous
    
    //Exit Habitat
    private TextView autonomousExitHabitatTextView;
    
    private Button autonomousExitHabitatYesButton;
    private Button autonomousExitHabitatNoButton;

    //Hatch Panels Secured
    private TextView autonomousHatchPanelsSecuredTextView;

    private Button autonomousHatchPanelsSecuredPlusOneButton;
    private Button autonomousHatchPanelsSecuredMinusOneButton;
    
    //Cargo Stored
    private TextView autonomousCargoStoredTextView;

    private Button autonomousCargoStoredPlusOneButton;
    private Button autonomousCargoStoredMinusOneButton;
    
    //endregion

    //region Teleop

    //Hatch Panels Secured
    private TextView teleopHatchPanelsSecuredTextView;

    private Button teleopHatchPanelsSecuredPlusOneButton;
    private Button teleopHatchPanelsSecuredMinusOneButton;

    //Cargo Stored
    private TextView teleopCargoStoredTextView;

    private Button teleopCargoStoredPlusOneButton;
    private Button teleopCargoStoredMinusOneButton;


    //Rockets Completed
    private TextView teleopRocketsCompletedTextView;

    private Button teleopRocketsCompletedPlusOneButton;
    private Button teleopRocketsCompletedMinusOneButton;

    //endregion

    //region End Game

    //Exit Habitat
    private TextView endGameReturnedToHabitatTextView;

    private Button endGameReturnedToHabitatNoButton;
    private Button endGameReturnedToHabitatLevelOneButton;
    private Button endGameReturnedToHabitatLevelTwoButton;
    private Button endGameReturnedToHabitatLevelThreeButton;

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card, container, false);

        context = (MainActivity) getActivity();
        final Database database = context.getDatabase();

        if(teamId > 0)
        {
            team = new Team(teamId);
            team.load(database);
        }

        if(scoutCardId > 0)
        {
            scoutCard = new ScoutCard(scoutCardId);
            scoutCard.load(database);
        }

        teamNumberAutoCompleteTextView = view.findViewById(R.id.TeamNumberAutoCompleteTextView);
        scouterNameAutoCompleteTextView = view.findViewById(R.id.ScouterNameAutoCompleteTextView);

        matchIdEditText = view.findViewById(R.id.MatchIdEditText);
        blueAllianceFinalScoreEditText = view.findViewById(R.id.BlueAllianceFinalScoreEditText);
        redAllianceFinalScoreEditText = view.findViewById(R.id.RedAllianceFinalScoreEditText);
        matchNotesEditText = view.findViewById(R.id.MatchNotesEditText);

        scoutCardSaveButton = view.findViewById(R.id.ScoutCardSaveButton);

        //region Autonomous

        //Exit Habitat
        autonomousExitHabitatTextView = view.findViewById(R.id.AutonomousExitHabitatTextView);

        autonomousExitHabitatYesButton = view.findViewById(R.id.AutonomousExitHabitatYesButton);
        autonomousExitHabitatNoButton = view.findViewById(R.id.AutonomousExitHabitatNoButton);

        autonomousExitHabitatYesButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousExitHabitatTextView.setText(R.string.yes);
            }
        });

        autonomousExitHabitatNoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousExitHabitatTextView.setText(R.string.no);
            }
        });

        //Hatch Panels Secured
        autonomousHatchPanelsSecuredTextView = view.findViewById(R.id.AutonomousHatchPanelsSecuredTextView);

        autonomousHatchPanelsSecuredPlusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecurePlusOneButton);
        autonomousHatchPanelsSecuredMinusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecureMinusOneButton);

        autonomousHatchPanelsSecuredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsSecuredTextView.setText((Integer.parseInt(autonomousHatchPanelsSecuredTextView.getText().toString()) + 1) + "");
            }
        });

        autonomousHatchPanelsSecuredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousHatchPanelsSecuredTextView.setText((Integer.parseInt(autonomousHatchPanelsSecuredTextView.getText().toString()) - 1) + "");
            }
        });

        //Cargo Stored
        autonomousCargoStoredTextView = view.findViewById(R.id.AutonomousCargoStoredTextView);

        autonomousCargoStoredPlusOneButton = view.findViewById(R.id.AutonomousCargoStoredPlusOneButton);
        autonomousCargoStoredMinusOneButton = view.findViewById(R.id.AutonomousCargoStoredMinusOneButton);

        autonomousCargoStoredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoStoredTextView.setText((Integer.parseInt(autonomousCargoStoredTextView.getText().toString()) + 1) + "");
            }
        });

        autonomousCargoStoredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                autonomousCargoStoredTextView.setText((Integer.parseInt(autonomousCargoStoredTextView.getText().toString()) - 1) + "");
            }
        });

        //endregion

        //region Teleop

        //Hatch Panels Secured
        teleopHatchPanelsSecuredTextView = view.findViewById(R.id.TeleopHatchPanelsSecuredTextView);

        teleopHatchPanelsSecuredPlusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecurePlusOneButton);
        teleopHatchPanelsSecuredMinusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecureMinusOneButton);

        teleopHatchPanelsSecuredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                teleopHatchPanelsSecuredTextView.setText((Integer.parseInt(teleopHatchPanelsSecuredTextView.getText().toString()) + 1) + "");
            }
        });

        teleopHatchPanelsSecuredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                teleopHatchPanelsSecuredTextView.setText((Integer.parseInt(teleopHatchPanelsSecuredTextView.getText().toString()) - 1) + "");
            }
        });

        //Cargo Stored
        teleopCargoStoredTextView = view.findViewById(R.id.TeleopCargoStoredTextView);

        teleopCargoStoredPlusOneButton = view.findViewById(R.id.TeleopCargoStoredPlusOneButton);
        teleopCargoStoredMinusOneButton = view.findViewById(R.id.TeleopCargoStoredMinusOneButton);

        teleopCargoStoredPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                teleopCargoStoredTextView.setText((Integer.parseInt(teleopCargoStoredTextView.getText().toString()) + 1) + "");
            }
        });

        teleopCargoStoredMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                teleopCargoStoredTextView.setText((Integer.parseInt(teleopCargoStoredTextView.getText().toString()) - 1) + "");
            }
        });


        //Rockets Completed
        teleopRocketsCompletedTextView = view.findViewById(R.id.TeleopRocketsCompletedTextView);

        teleopRocketsCompletedPlusOneButton = view.findViewById(R.id.TeleopRocketsCompletedPlusOneButton);
        teleopRocketsCompletedMinusOneButton = view.findViewById(R.id.TeleopRocketsCompletedMinusOneButton);

        teleopRocketsCompletedPlusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                teleopRocketsCompletedTextView.setText((Integer.parseInt(teleopRocketsCompletedTextView.getText().toString()) + 1) + "");
            }
        });

        teleopRocketsCompletedMinusOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                teleopRocketsCompletedTextView.setText((Integer.parseInt(teleopRocketsCompletedTextView.getText().toString()) - 1) + "");
            }
        });

        //endregion

        //region End Game

        //Exit Habitat
        endGameReturnedToHabitatTextView = view.findViewById(R.id.EndGameReturnedToHabitatTextView);

        endGameReturnedToHabitatNoButton = view.findViewById(R.id.EndGameReturnedToHabitatNoButton);
        endGameReturnedToHabitatLevelOneButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelOneButton);
        endGameReturnedToHabitatLevelTwoButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelTwoButton);
        endGameReturnedToHabitatLevelThreeButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelThreeButton);

        endGameReturnedToHabitatNoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.no);
            }
        });

        endGameReturnedToHabitatLevelOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.level_1);
            }
        });

        endGameReturnedToHabitatLevelTwoButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.level_2);
            }
        });

        endGameReturnedToHabitatLevelThreeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                endGameReturnedToHabitatTextView.setText(R.string.level_3);
            }
        });


        //endregion

        scoutCardSaveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(validateFields())
                {

                    int matchId = Integer.parseInt(matchIdEditText.getText().toString());
                    int teamNumber = Integer.parseInt(teamNumberAutoCompleteTextView.getText().toString());
                    String scouterName = scouterNameAutoCompleteTextView.getText().toString();
                    int blueAllianceFinalScore = Integer.parseInt(blueAllianceFinalScoreEditText.getText().toString());
                    int redAllianceFinalScore = Integer.parseInt(redAllianceFinalScoreEditText.getText().toString());
                    boolean autonomousExitHabitat = autonomousExitHabitatTextView.getText().toString().equals(getActivity().getResources().getString(R.string.yes));
                    int autonomousHatchPanelsSecured = Integer.parseInt(autonomousHatchPanelsSecuredTextView.getText().toString());
                    int autonomousCargoStored = Integer.parseInt(autonomousCargoStoredTextView.getText().toString());
                    int teleopHatchPanelsSecured = Integer.parseInt(teleopHatchPanelsSecuredTextView.getText().toString());
                    int teleopCargoStored = Integer.parseInt(teleopCargoStoredTextView.getText().toString());
                    int teleopRocketsCompleted = Integer.parseInt(teleopRocketsCompletedTextView.getText().toString());
                    String endGameReturnedToHabitat = endGameReturnedToHabitatTextView.getText().toString();
                    String matchNotes = matchNotesEditText.getText().toString();
                    Date completedDate = new Date(System.currentTimeMillis());

                    if (scoutCard != null)
                    {
                        scoutCard.setMatchId(matchId);
                        scoutCard.setTeamId(teamNumber);
                        scoutCard.setCompletedBy(scouterName);
                        scoutCard.setBlueAllianceFinalScore(blueAllianceFinalScore);
                        scoutCard.setRedAllianceFinalScore(redAllianceFinalScore);
                        scoutCard.setAutonomousExitHabitat(autonomousExitHabitat);
                        scoutCard.setAutonomousHatchPanelsSecured(autonomousHatchPanelsSecured);
                        scoutCard.setAutonomousCargoStored(autonomousCargoStored);
                        scoutCard.setTeleopHatchPanelsSecured(teleopHatchPanelsSecured);
                        scoutCard.setTeleopCargoStored(teleopCargoStored);
                        scoutCard.setTeleopRocketsCompleted(teleopRocketsCompleted);
                        scoutCard.setEndGameReturnedToHabitat(endGameReturnedToHabitat);
                        scoutCard.setNotes(matchNotes);
                        scoutCard.setCompletedDate(completedDate);
                        if (scoutCard.save(database) > 0)
                        {
                            context.showSnackbar("Saved Successfully.");
                            context.getSupportFragmentManager().popBackStackImmediate();
                        }


                    } else
                    {
                        ScoutCard scoutCard = new ScoutCard(
                                -1,
                                matchId,
                                teamNumber,
                                scouterName,
                                blueAllianceFinalScore,
                                redAllianceFinalScore,
                                autonomousExitHabitat,
                                autonomousHatchPanelsSecured,
                                autonomousCargoStored,
                                teleopHatchPanelsSecured,
                                teleopCargoStored,
                                teleopRocketsCompleted,
                                endGameReturnedToHabitat,
                                matchNotes,
                                completedDate);
                        if (scoutCard.save(database) > 0)
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

        ArrayAdapter<String> scouterNameAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_dropdown_item_1line, scouterNames);
        scouterNameAutoCompleteTextView.setAdapter(scouterNameAdapter);

        //scoutcard loaded, populate fields
        if(scoutCard != null)
        {
            teamNumberAutoCompleteTextView.setText(String.valueOf(scoutCard.getTeamId()));
            scouterNameAutoCompleteTextView.setText(scoutCard.getCompletedBy());

            matchIdEditText.setText(String.valueOf(scoutCard.getMatchId()));

            blueAllianceFinalScoreEditText.setText(String.valueOf(scoutCard.getBlueAllianceFinalScore()));
            redAllianceFinalScoreEditText.setText(String.valueOf(scoutCard.getRedAllianceFinalScore()));

            autonomousExitHabitatTextView.setText(scoutCard.isAutonomousExitHabitat() ? R.string.yes : R.string.no);
            autonomousHatchPanelsSecuredTextView.setText(String.valueOf(scoutCard.getAutonomousHatchPanelsSecured()));
            autonomousCargoStoredTextView.setText(String.valueOf(scoutCard.getAutonomousCargoStored()));

            teleopHatchPanelsSecuredTextView.setText(String.valueOf(scoutCard.getTeleopHatchPanelsSecured()));
            teleopCargoStoredTextView.setText(String.valueOf(scoutCard.getTeleopCargoStored()));
            teleopRocketsCompletedMinusOneButton.setText(String.valueOf(scoutCard.getTeleopRocketsCompleted()));

            endGameReturnedToHabitatTextView.setText(scoutCard.getEndGameReturnedToHabitat());

            matchNotesEditText.setText(scoutCard.getNotes());
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

        if(!matchIdEditText.getText().toString().matches("^[-+]?\\d*$")
            || matchIdEditText.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid match number.");
            return false;
        }

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

        if(autonomousExitHabitatTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid autonomous exit habitat score.");
            return false;
        }

        if(!autonomousHatchPanelsSecuredTextView.getText().toString().matches("^[-+]?\\d*$")
            || autonomousHatchPanelsSecuredTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid autonomous hatch panels secured.");
            return false;
        }

        if(!autonomousCargoStoredTextView.getText().toString().matches("^[-+]?\\d*$")
            || autonomousCargoStoredTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid autonomous cargo stored.");
            return false;
        }

        if(!teleopHatchPanelsSecuredTextView.getText().toString().matches("^[-+]?\\d*$")
            || teleopHatchPanelsSecuredTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid teleop hatch panels secured.");
            return false;
        }

        if(!teleopCargoStoredTextView.getText().toString().matches("^[-+]?\\d*$")
            || teleopCargoStoredTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid teleop cargo stored.");
            return false;
        }

        if(!teleopRocketsCompletedTextView.getText().toString().matches("^[-+]?\\d*$")
            || teleopRocketsCompletedTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid teleop rockets completed.");
            return false;
        }

        if(endGameReturnedToHabitatTextView.getText().toString().equals(""))
        {
            context.showSnackbar("Invalid end game returned to habitat.");
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

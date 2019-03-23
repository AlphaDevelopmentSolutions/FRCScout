package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphadevelopmentsolutions.frcscout.Adapters.ScoutCardViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Classes.StartingPosition;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScoutCardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoutCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoutCardFragment extends MasterFragment
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
    
    private ScoutCard scoutCard;

    private TabLayout scoutCardTabLayout;
    private ViewPager scoutCardViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card, container, false);

        if(scoutCardId > 0)
        {
            scoutCard = new ScoutCard(scoutCardId);
            scoutCard.load(database);
        }


        scoutCardTabLayout = view.findViewById(R.id.ScoutCardTabLayout);
        scoutCardViewPager = view.findViewById(R.id.ScoutCardViewPager);



        final ScoutCardPreGameFragment scoutCardPreGameFragment = ScoutCardPreGameFragment.newInstance(scoutCardId, teamId);
        final ScoutCardAutoFragment scoutCardAutoFragment = ScoutCardAutoFragment.newInstance(scoutCardId);
        final ScoutCardTeleopFragment scoutCardTeleopFragment = ScoutCardTeleopFragment.newInstance(scoutCardId);
        final ScoutCardEndGameFragment scoutCardEndGameFragment = ScoutCardEndGameFragment.newInstance(scoutCardId);
        final ScoutCardPostGameFragment scoutCardPostGameFragment = ScoutCardPostGameFragment.newInstance(scoutCardId);


        final Resources resources = context.getResources();

        ScoutCardViewPagerAdapter scoutCardViewPagerAdapter = new ScoutCardViewPagerAdapter(getChildFragmentManager());
        scoutCardViewPagerAdapter.addFragment(scoutCardPreGameFragment, resources.getString(R.string.pre_game));
        scoutCardViewPagerAdapter.addFragment(scoutCardAutoFragment, resources.getString(R.string.autonomous));
        scoutCardViewPagerAdapter.addFragment(scoutCardTeleopFragment, resources.getString(R.string.teleop));
        scoutCardViewPagerAdapter.addFragment(scoutCardEndGameFragment, resources.getString(R.string.end_game));
        scoutCardViewPagerAdapter.addFragment(scoutCardPostGameFragment, resources.getString(R.string.post_game));

        scoutCardViewPager.setAdapter(scoutCardViewPagerAdapter);
        scoutCardTabLayout.setupWithViewPager(scoutCardViewPager);


        scoutCardPostGameFragment.setOnSaveButtonClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //validates all fields are valid on the forms
                if(scoutCardPreGameFragment.validateFields() &&
                    scoutCardAutoFragment.validateFields() &&
                    scoutCardTeleopFragment.validateFields() &&
                    scoutCardEndGameFragment.validateFields() &&
                    scoutCardPostGameFragment.validateFields())
                {

                    //pre game info
                    int matchId = scoutCardPreGameFragment.getMatchNumber();
                    int teamNumber = teamId;
                    String eventId = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.EVENT_ID_PREF, "");
                    String allianceColor = scoutCardPreGameFragment.getAllianceColor();
                    String completedBy = scoutCardPreGameFragment.getScouterName();

                    int preGameStartingLevel = scoutCardPreGameFragment.getStartingLevel();
                    StartingPosition preGameStartingPosition = scoutCardPreGameFragment.getStartingPosition();
                    StartingPiece preGameStartingPiece = scoutCardPreGameFragment.getStartingPiece();

                    //auto info
                    boolean autonomousExitHabitat = scoutCardAutoFragment.getAutonomousExitHab();
                    int autonomousHatchPanelsPickedUp = scoutCardAutoFragment.getAutonomousHatchPanelsPickedUp();
                    int autonomousHatchPanelsSecuredAttempts = scoutCardAutoFragment.getAutonomousHatchPanelsDropped();
                    int autonomousHatchPanelsSecured = scoutCardAutoFragment.getAutonomousHatchPanelsSecured();
                    int autonomousCargoPickedUp = scoutCardAutoFragment.getAutonomousCargoPickedUp();
                    int autonomousCargoStoredAttempts = scoutCardAutoFragment.getAutonomousCargoDropped();
                    int autonomousCargoStored = scoutCardAutoFragment.getAutonomousCargoStored();

                    //teleop info
                    int teleopHatchPanelsPickedUp = scoutCardTeleopFragment.getTeleopHatchPanelsPickedUp();
                    int teleopHatchPanelsSecuredAttempts = scoutCardTeleopFragment.getTeleopHatchPanelsDropped();
                    int teleopHatchPanelsSecured = scoutCardTeleopFragment.getTeleopHatchPanelsSecured();
                    int teleopCargoPickedUp = scoutCardTeleopFragment.getTeleopCargoPickedUp();
                    int teleopCargoStoredAttempts = scoutCardTeleopFragment.getTeleopCargoDropped();
                    int teleopCargoStored = scoutCardTeleopFragment.getTeleopCargoStored();

                    //endgame info
                    int endGameReturnedToHabitat = scoutCardEndGameFragment.getReturnedToHabLevel();
                    int endGameReturnedToHabitatAttempts = scoutCardEndGameFragment.getReturnedToHabAttemptLevel();

                    //post game info
                    int blueAllianceFinalScore = scoutCardPostGameFragment.getBlueAllianceScore();
                    int redAllianceFinalScore = scoutCardPostGameFragment.getRedAllianceScore();
                    int defenseRating = scoutCardPostGameFragment.getDefenseRating();
                    int offenseRating = scoutCardPostGameFragment.getOffenseRating();
                    int driveRating = scoutCardPostGameFragment.getDriveRating();
                    String notes = scoutCardPostGameFragment.getNotes();

                    Date completedDate = new Date(System.currentTimeMillis());

                    //saving a draft scout card
                    if (scoutCard != null)
                    {
                        scoutCard.setMatchId(matchId);
                        scoutCard.setTeamId(teamNumber);
                        scoutCard.setEventId(eventId);
                        scoutCard.setAllianceColor(allianceColor);
                        scoutCard.setCompletedBy(completedBy);

                        scoutCard.setPreGameStartingLevel(preGameStartingLevel);
                        scoutCard.setPreGameStartingPosition(preGameStartingPosition);
                        scoutCard.setPreGameStartingPiece(preGameStartingPiece);

                        scoutCard.setAutonomousExitHabitat(autonomousExitHabitat);
                        scoutCard.setAutonomousHatchPanelsPickedUp(autonomousHatchPanelsPickedUp);
                        scoutCard.setAutonomousHatchPanelsSecuredAttempts(autonomousHatchPanelsSecuredAttempts);
                        scoutCard.setAutonomousHatchPanelsSecured(autonomousHatchPanelsSecured);
                        scoutCard.setAutonomousCargoPickedUp(autonomousCargoPickedUp);
                        scoutCard.setAutonomousCargoStoredAttempts(autonomousCargoStoredAttempts);
                        scoutCard.setAutonomousCargoStored(autonomousCargoStored);

                        scoutCard.setTeleopHatchPanelsPickedUp(teleopHatchPanelsPickedUp);
                        scoutCard.setTeleopHatchPanelsSecuredAttempts(teleopHatchPanelsSecuredAttempts);
                        scoutCard.setTeleopHatchPanelsSecured(teleopHatchPanelsSecured);
                        scoutCard.setTeleopCargoPickedUp(teleopCargoPickedUp);
                        scoutCard.setTeleopCargoStoredAttempts(teleopCargoStoredAttempts);
                        scoutCard.setTeleopCargoStored(teleopCargoStored);

                        scoutCard.setEndGameReturnedToHabitat(endGameReturnedToHabitat);
                        scoutCard.setEndGameReturnedToHabitatAttempts(endGameReturnedToHabitatAttempts);

                        scoutCard.setBlueAllianceFinalScore(blueAllianceFinalScore);
                        scoutCard.setRedAllianceFinalScore(redAllianceFinalScore);
                        scoutCard.setDefenseRating(defenseRating);
                        scoutCard.setOffenseRating(offenseRating);
                        scoutCard.setDriveRating(driveRating);
                        scoutCard.setNotes(notes);
                        scoutCard.setCompletedDate(completedDate);
                        scoutCard.setDraft(true);
                        
                        //save the scout card
                        if (scoutCard.save(database) > 0)
                        {
                            context.showSnackbar("Saved Successfully.");
                            context.getSupportFragmentManager().popBackStackImmediate();
                        }
                        else
                        {
                            context.showSnackbar("Saved Failed.");
                        }


                    } 
                    
                    //saving a new scoutcard
                    else
                    {
                        ScoutCard scoutCard = new ScoutCard(
                                -1,
                                matchId,
                                teamId,
                                eventId,
                                allianceColor,
                                completedBy,
        
                                preGameStartingLevel,
                                preGameStartingPosition,
                                preGameStartingPiece,
        
                                autonomousExitHabitat,
                                autonomousHatchPanelsPickedUp,
                                autonomousHatchPanelsSecuredAttempts,
                                autonomousHatchPanelsSecured,
                                autonomousCargoPickedUp,
                                autonomousCargoStoredAttempts,
                                autonomousCargoStored,
        
                                teleopHatchPanelsPickedUp,
                                teleopHatchPanelsSecuredAttempts,
                                teleopHatchPanelsSecured,
                                teleopCargoPickedUp,
                                teleopCargoStoredAttempts,
                                teleopCargoStored,
        
                                endGameReturnedToHabitat,
                                endGameReturnedToHabitatAttempts,
        
                                blueAllianceFinalScore,
                                redAllianceFinalScore,
                                defenseRating,
                                offenseRating,
                                driveRating,
                                notes,
                                completedDate,
                                true);

                        //save the scout card
                        if (scoutCard.save(database) > 0)
                        {
                            context.showSnackbar("Saved Successfully.");
                            context.getSupportFragmentManager().popBackStackImmediate();
                        }
                        else
                        {
                            context.showSnackbar("Saved Failed.");
                        }
                    }
                }
            }
        });


//        //scoutcard loaded, populate fields
//        if(scoutCard != null)
//        {
//            teamNumberAutoCompleteTextView.setText(String.valueOf(scoutCard.getTeamId()));
//            scouterNameAutoCompleteTextView.setText(scoutCard.getCompletedBy());
//
//            matchIdEditText.setText(String.valueOf(scoutCard.getMatchId()));
//            allianceColorSpinner.setSelection(scoutCard.getAllianceColor() == AllianceColor.RED ? 0 : 1);
//
//            blueAllianceFinalScoreEditText.setText(String.valueOf(scoutCard.getBlueAllianceFinalScore()));
//            redAllianceFinalScoreEditText.setText(String.valueOf(scoutCard.getRedAllianceFinalScore()));
//
//            autonomousExitHabitatTextView.setText(scoutCard.isAutonomousExitHabitat());
//            autonomousHatchPanelsSecuredTextView.setText(String.valueOf(scoutCard.getAutonomousHatchPanelsSecured()));
//            autonomousHatchPanelsSecuredAttemptsTextView.setText(String.valueOf(scoutCard.getAutonomousHatchPanelsSecured()));
//            autonomousCargoStoredTextView.setText(String.valueOf(scoutCard.getAutonomousCargoStored()));
//            autonomousCargoStoredAttemptsTextView.setText(String.valueOf(scoutCard.getAutonomousCargoStored()));
//
//            teleopHatchPanelsSecuredTextView.setText(String.valueOf(scoutCard.getTeleopHatchPanelsSecured()));
//            teleopHatchPanelsSecuredAttemptsTextView.setText(String.valueOf(scoutCard.getTeleopHatchPanelsSecured()));
//            teleopCargoStoredTextView.setText(String.valueOf(scoutCard.getTeleopCargoStored()));
//            teleopCargoStoredAttemptsTextView.setText(String.valueOf(scoutCard.getTeleopCargoStored()));
//
//            endGameReturnedToHabitatTextView.setText(scoutCard.getEndGameReturnedToHabitat());
//            endGameReturnedToHabitatAttemptsTextView.setText(scoutCard.getEndGameReturnedToHabitatAttempts());
//
//            matchNotesEditText.setText(scoutCard.getNotes());
//        }

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

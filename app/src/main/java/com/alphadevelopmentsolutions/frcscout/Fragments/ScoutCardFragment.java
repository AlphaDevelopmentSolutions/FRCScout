package com.alphadevelopmentsolutions.frcscout.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece;
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

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
    private OnFragmentInteractionListener mListener;

    public ScoutCardFragment()
    {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param match
     * @param scoutCard
     * @param team
     * @return A new instance of fragment ScoutCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoutCardFragment newInstance(@NonNull Match match, @Nullable ScoutCard scoutCard, @NonNull Team team)
    {
        ScoutCardFragment fragment = new ScoutCardFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MATCH_JSON, toJson(match));
        args.putString(ARG_PARAM_SCOUT_CARD_JSON, toJson(scoutCard));
        args.putString(ARG_TEAM_JSON, toJson(team));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //start the creation of fragments on a new thread
        fragCreationThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {

                joinLoadingThread();

                scoutCardPreGameFragment = ScoutCardPreGameFragment.newInstance(scoutCard, team);
                scoutCardAutoFragment = ScoutCardAutoFragment.newInstance(scoutCard);
                scoutCardTeleopFragment = ScoutCardTeleopFragment.newInstance(scoutCard);
                scoutCardEndGameFragment = ScoutCardEndGameFragment.newInstance(scoutCard);
                scoutCardPostGameFragment = ScoutCardPostGameFragment.newInstance(scoutCard);
            }
        });
        fragCreationThread.start();

    }

    private TabLayout scoutCardTabLayout;
    private ViewPager scoutCardViewPager;

    private ScoutCardPreGameFragment scoutCardPreGameFragment;
    private ScoutCardAutoFragment scoutCardAutoFragment;
    private ScoutCardTeleopFragment scoutCardTeleopFragment;
    private ScoutCardEndGameFragment scoutCardEndGameFragment;
    private ScoutCardPostGameFragment scoutCardPostGameFragment;

    private Thread fragCreationThread;

    private FloatingActionButton scoutCardSaveFloatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scout_card, container, false);

        //gets rid of the shadow on the actionbar
        context.dropActionBar();

        scoutCardTabLayout = view.findViewById(R.id.ScoutCardTabLayout);
        scoutCardViewPager = view.findViewById(R.id.ScoutCardViewPager);

        scoutCardSaveFloatingActionButton = view.findViewById(R.id.ScoutCardSaveFloatingActionButton);

        final FragmentViewPagerAdapter scoutCardViewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());

        //join back with the frag creation thread
        try
        {
            fragCreationThread.join();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        scoutCardViewPagerAdapter.addFragment(scoutCardPreGameFragment, getString(R.string.pre_game));
        scoutCardViewPagerAdapter.addFragment(scoutCardAutoFragment, getString(R.string.auto));
        scoutCardViewPagerAdapter.addFragment(scoutCardTeleopFragment, getString(R.string.teleop));
        scoutCardViewPagerAdapter.addFragment(scoutCardEndGameFragment, getString(R.string.end_game));
        scoutCardViewPagerAdapter.addFragment(scoutCardPostGameFragment, getString(R.string.post_game));

        //update the title of the page to display the match
        context.setTitle(match.getMatchType().toString(match));


        scoutCardViewPager.setAdapter(scoutCardViewPagerAdapter);
        scoutCardViewPager.setOffscreenPageLimit(5);
        scoutCardTabLayout.setupWithViewPager(scoutCardViewPager);

        if(scoutCard == null || scoutCard.isDraft())
        {
            scoutCardSaveFloatingActionButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    //validates all fields are valid on the forms
                    if (scoutCardPreGameFragment.validateFields() &&
                            scoutCardAutoFragment.validateFields() &&
                            scoutCardTeleopFragment.validateFields() &&
                            scoutCardEndGameFragment.validateFields() &&
                            scoutCardPostGameFragment.validateFields())
                    {

                        //pre game info
                        int teamNumber = scoutCardPreGameFragment.getTeamId();
                        String eventId = (scoutCard == null) ? event.getBlueAllianceId() : scoutCard.getEventId();
                        AllianceColor allianceColor = match.getTeamAllianceColor(team);
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
                        int defenseRating = scoutCardPostGameFragment.getDefenseRating();
                        int offenseRating = scoutCardPostGameFragment.getOffenseRating();
                        int driveRating = scoutCardPostGameFragment.getDriveRating();
                        String notes = scoutCardPostGameFragment.getNotes();

                        Date completedDate = new Date(System.currentTimeMillis());

                        //saving a draft scout card
                        if (scoutCard != null)
                        {
                            scoutCard.setMatchId(match.getKey());
                            scoutCard.setTeamId(teamNumber);
                            scoutCard.setEventId(eventId);
                            scoutCard.setAllianceColor(allianceColor.name());
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
                            } else
                            {
                                context.showSnackbar("Saved Failed.");
                            }


                        }

                        //saving a new scoutcard
                        else
                        {
                            ScoutCard scoutCard = new ScoutCard(
                                    -1,
                                    match.getKey(),
                                    team.getId(),
                                    eventId,
                                    allianceColor.name(),
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
                            } else
                            {
                                context.showSnackbar("Saved Failed.");
                            }
                        }
                    }
                }
            });
        }
        else
            scoutCardSaveFloatingActionButton.setVisibility(View.GONE);

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

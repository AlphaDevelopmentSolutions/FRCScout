package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition
import com.alphadevelopmentsolutions.frcscout.R

import java.util.Date

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoutCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoutCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutCardFragment : MasterFragment()
{
    private var mListener: OnFragmentInteractionListener? = null

    private var scoutCardTabLayout: TabLayout? = null
    private var scoutCardViewPager: ViewPager? = null

    private var scoutCardPreGameFragment: ScoutCardPreGameFragment? = null
    private var scoutCardAutoFragment: ScoutCardAutoFragment? = null
    private var scoutCardTeleopFragment: ScoutCardTeleopFragment? = null
    private var scoutCardEndGameFragment: ScoutCardEndGameFragment? = null
    private var scoutCardPostGameFragment: ScoutCardPostGameFragment? = null

    private var fragCreationThread: Thread? = null

    private var scoutCardSaveFloatingActionButton: FloatingActionButton? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //start the creation of fragments on a new thread
        fragCreationThread = Thread(Runnable {
            joinLoadingThread()

            scoutCardPreGameFragment = ScoutCardPreGameFragment.newInstance(scoutCard, team!!)
            scoutCardAutoFragment = ScoutCardAutoFragment.newInstance(scoutCard)
            scoutCardTeleopFragment = ScoutCardTeleopFragment.newInstance(scoutCard)
            scoutCardEndGameFragment = ScoutCardEndGameFragment.newInstance(scoutCard)
            scoutCardPostGameFragment = ScoutCardPostGameFragment.newInstance(scoutCard)
        })
        fragCreationThread!!.start()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card, container, false)

        //gets rid of the shadow on the actionbar
        context.dropActionBar()

        scoutCardTabLayout = view.findViewById(R.id.ScoutCardTabLayout)
        scoutCardViewPager = view.findViewById(R.id.ScoutCardViewPager)

        scoutCardSaveFloatingActionButton = view.findViewById(R.id.ScoutCardSaveFloatingActionButton)

        val scoutCardViewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

        //join back with the frag creation thread
        try
        {
            fragCreationThread!!.join()
        } catch (e: InterruptedException)
        {
            e.printStackTrace()
        }

        scoutCardViewPagerAdapter.addFragment(scoutCardPreGameFragment!!, getString(R.string.pre_game))
        scoutCardViewPagerAdapter.addFragment(scoutCardAutoFragment!!, getString(R.string.auto))
        scoutCardViewPagerAdapter.addFragment(scoutCardTeleopFragment!!, getString(R.string.teleop))
        scoutCardViewPagerAdapter.addFragment(scoutCardEndGameFragment!!, getString(R.string.end_game))
        scoutCardViewPagerAdapter.addFragment(scoutCardPostGameFragment!!, getString(R.string.post_game))

        //update the title of the page to display the match
        context.setTitle(match!!.matchType.toString(match!!))


        scoutCardViewPager!!.adapter = scoutCardViewPagerAdapter
        scoutCardViewPager!!.offscreenPageLimit = 5
        scoutCardTabLayout!!.setupWithViewPager(scoutCardViewPager)

        if (scoutCard == null || scoutCard!!.isDraft)
        {
            scoutCardSaveFloatingActionButton!!.setOnClickListener {
                //validates all fields are valid on the forms
                if (scoutCardPreGameFragment!!.validateFields() &&
                        scoutCardAutoFragment!!.validateFields() &&
                        scoutCardTeleopFragment!!.validateFields() &&
                        scoutCardEndGameFragment!!.validateFields() &&
                        scoutCardPostGameFragment!!.validateFields())
                {

                    //pre game info
                    val teamNumber = scoutCardPreGameFragment!!.teamId
                    val eventId = if (scoutCard == null) event!!.blueAllianceId else scoutCard!!.eventId
                    val allianceColor = match!!.getTeamAllianceColor(team!!)
                    val completedBy = scoutCardPreGameFragment!!.scouterName

                    val preGameStartingLevel = scoutCardPreGameFragment!!.startingLevel
                    val preGameStartingPosition = scoutCardPreGameFragment!!.startingPosition
                    val preGameStartingPiece = scoutCardPreGameFragment!!.startingPiece

                    //auto info
                    val autonomousExitHabitat = scoutCardAutoFragment!!.autonomousExitHab
                    val autonomousHatchPanelsPickedUp = scoutCardAutoFragment!!.autonomousHatchPanelsPickedUp
                    val autonomousHatchPanelsSecuredAttempts = scoutCardAutoFragment!!.autonomousHatchPanelsDropped
                    val autonomousHatchPanelsSecured = scoutCardAutoFragment!!.autonomousHatchPanelsSecured
                    val autonomousCargoPickedUp = scoutCardAutoFragment!!.autonomousCargoPickedUp
                    val autonomousCargoStoredAttempts = scoutCardAutoFragment!!.autonomousCargoDropped
                    val autonomousCargoStored = scoutCardAutoFragment!!.autonomousCargoStored

                    //teleop info
                    val teleopHatchPanelsPickedUp = scoutCardTeleopFragment!!.teleopHatchPanelsPickedUp
                    val teleopHatchPanelsSecuredAttempts = scoutCardTeleopFragment!!.teleopHatchPanelsDropped
                    val teleopHatchPanelsSecured = scoutCardTeleopFragment!!.teleopHatchPanelsSecured
                    val teleopCargoPickedUp = scoutCardTeleopFragment!!.teleopCargoPickedUp
                    val teleopCargoStoredAttempts = scoutCardTeleopFragment!!.teleopCargoDropped
                    val teleopCargoStored = scoutCardTeleopFragment!!.teleopCargoStored

                    //endgame info
                    val endGameReturnedToHabitat = scoutCardEndGameFragment!!.returnedToHabLevel
                    val endGameReturnedToHabitatAttempts = scoutCardEndGameFragment!!.returnedToHabAttemptLevel

                    //post game info
                    val defenseRating = scoutCardPostGameFragment!!.defenseRating
                    val offenseRating = scoutCardPostGameFragment!!.offenseRating
                    val driveRating = scoutCardPostGameFragment!!.driveRating
                    val notes = scoutCardPostGameFragment!!.notes

                    val completedDate = Date(System.currentTimeMillis())

                    //saving a draft scout card
                    if (scoutCard != null)
                    {
                        scoutCard!!.matchId = match!!.key
                        scoutCard!!.teamId = teamNumber
                        scoutCard!!.eventId = eventId!!
                        scoutCard!!.allianceColor = allianceColor.name
                        scoutCard!!.completedBy = completedBy

                        scoutCard!!.preGameStartingLevel = preGameStartingLevel
                        scoutCard!!.preGameStartingPosition = preGameStartingPosition
                        scoutCard!!.preGameStartingPiece = preGameStartingPiece

                        scoutCard!!.autonomousExitHabitat = autonomousExitHabitat
                        scoutCard!!.autonomousHatchPanelsPickedUp = autonomousHatchPanelsPickedUp
                        scoutCard!!.autonomousHatchPanelsSecuredAttempts = autonomousHatchPanelsSecuredAttempts
                        scoutCard!!.autonomousHatchPanelsSecured = autonomousHatchPanelsSecured
                        scoutCard!!.autonomousCargoPickedUp = autonomousCargoPickedUp
                        scoutCard!!.autonomousCargoStoredAttempts = autonomousCargoStoredAttempts
                        scoutCard!!.autonomousCargoStored = autonomousCargoStored

                        scoutCard!!.teleopHatchPanelsPickedUp = teleopHatchPanelsPickedUp
                        scoutCard!!.teleopHatchPanelsSecuredAttempts = teleopHatchPanelsSecuredAttempts
                        scoutCard!!.teleopHatchPanelsSecured = teleopHatchPanelsSecured
                        scoutCard!!.teleopCargoPickedUp = teleopCargoPickedUp
                        scoutCard!!.teleopCargoStoredAttempts = teleopCargoStoredAttempts
                        scoutCard!!.teleopCargoStored = teleopCargoStored

                        scoutCard!!.endGameReturnedToHabitat = endGameReturnedToHabitat
                        scoutCard!!.endGameReturnedToHabitatAttempts = endGameReturnedToHabitatAttempts

                        scoutCard!!.defenseRating = defenseRating
                        scoutCard!!.offenseRating = offenseRating
                        scoutCard!!.driveRating = driveRating
                        scoutCard!!.notes = notes
                        scoutCard!!.completedDate = completedDate
                        scoutCard!!.isDraft = true

                        //save the scout card
                        if (scoutCard!!.save(database) > 0)
                        {
                            context.showSnackbar("Saved Successfully.")
                            context.supportFragmentManager.popBackStackImmediate()
                        } else
                        {
                            context.showSnackbar("Saved Failed.")
                        }


                    } else
                    {
                        val scoutCard = ScoutCard(
                                -1,
                                match!!.key,
                                team!!.id!!,
                                eventId!!,
                                allianceColor.name,
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
                                true)

                        //save the scout card
                        if (scoutCard.save(database) > 0)
                        {
                            context.showSnackbar("Saved Successfully.")
                            context.supportFragmentManager.popBackStackImmediate()
                        } else
                        {
                            context.showSnackbar("Saved Failed.")
                        }
                    }//saving a new scoutcard
                }
            }
        } else
            scoutCardSaveFloatingActionButton!!.hide()

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri)
    {
        if (mListener != null)
        {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            mListener = context
        } else
        {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }


    override fun onDetach()
    {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object
    {

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
        fun newInstance(match: Match, scoutCard: ScoutCard?, team: Team): ScoutCardFragment
        {
            val fragment = ScoutCardFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_MATCH_JSON, MasterFragment.toJson(match))
            args.putString(MasterFragment.ARG_PARAM_SCOUT_CARD_JSON, MasterFragment.toJson(scoutCard))
            args.putString(MasterFragment.ARG_TEAM_JSON, MasterFragment.toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard
import com.alphadevelopmentsolutions.frcscout.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoutCardEndGameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoutCardEndGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutCardEndGameFragment : MasterFragment()
{

    private var mListener: OnFragmentInteractionListener? = null

    //Exit Habitat
    private var endGameReturnedToHabitatTextView: TextView? = null
    private var endGameReturnedToHabitatAttemptsTextView: TextView? = null

    private var endGameReturnedToHabitatNoButton: Button? = null
    private var endGameReturnedToHabitatLevelOneButton: Button? = null
    private var endGameReturnedToHabitatLevelTwoButton: Button? = null
    private var endGameReturnedToHabitatLevelThreeButton: Button? = null

    private var endGameReturnedToHabitatAttemptsNoButton: Button? = null
    private var endGameReturnedToHabitatAttemptsLevelOneButton: Button? = null
    private var endGameReturnedToHabitatAttemptsLevelTwoButton: Button? = null
    private var endGameReturnedToHabitatAttemptsLevelThreeButton: Button? = null

    //region Getters

    var returnedToHabLevel: Int = 0
        private set
    var returnedToHabAttemptLevel: Int = 0
        private set

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_end_game, container, false)

        joinLoadingThread()

        //Exit Habitat
        endGameReturnedToHabitatTextView = view.findViewById(R.id.EndGameReturnedToHabitatTextView)
        endGameReturnedToHabitatAttemptsTextView = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsTextView)

        endGameReturnedToHabitatNoButton = view.findViewById(R.id.EndGameReturnedToHabitatNoButton)
        endGameReturnedToHabitatAttemptsNoButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsNoButton)
        endGameReturnedToHabitatLevelOneButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelOneButton)
        endGameReturnedToHabitatAttemptsLevelOneButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsLevelOneButton)
        endGameReturnedToHabitatLevelTwoButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelTwoButton)
        endGameReturnedToHabitatAttemptsLevelTwoButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsLevelTwoButton)
        endGameReturnedToHabitatLevelThreeButton = view.findViewById(R.id.EndGameReturnedToHabitatLevelThreeButton)
        endGameReturnedToHabitatAttemptsLevelThreeButton = view.findViewById(R.id.EndGameReturnedToHabitatAttemptsLevelThreeButton)

        endGameReturnedToHabitatNoButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatTextView!!.setText(R.string.no)
                returnedToHabLevel = 0
            }
        }

        endGameReturnedToHabitatLevelOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatTextView!!.setText(R.string.level_1)
                returnedToHabLevel = 1
            }
        }

        endGameReturnedToHabitatLevelTwoButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatTextView!!.setText(R.string.level_2)
                returnedToHabLevel = 2
            }
        }

        endGameReturnedToHabitatLevelThreeButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatTextView!!.setText(R.string.level_3)
                returnedToHabLevel = 3
            }
        }

        endGameReturnedToHabitatAttemptsNoButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatAttemptsTextView!!.setText(R.string.no)
                returnedToHabAttemptLevel = 0
            }
        }

        endGameReturnedToHabitatAttemptsLevelOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatAttemptsTextView!!.setText(R.string.level_1)
                returnedToHabAttemptLevel = 1
            }
        }

        endGameReturnedToHabitatAttemptsLevelTwoButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatAttemptsTextView!!.setText(R.string.level_2)
                returnedToHabAttemptLevel = 2
            }
        }

        endGameReturnedToHabitatAttemptsLevelThreeButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
            {
                endGameReturnedToHabitatAttemptsTextView!!.setText(R.string.level_3)
                returnedToHabAttemptLevel = 3
            }
        }

        //scoutcard loaded, populate fields
        if (scoutCard != null)
        {
            returnedToHabLevel = scoutCard!!.endGameReturnedToHabitat
            returnedToHabAttemptLevel = scoutCard!!.endGameReturnedToHabitatAttempts

            endGameReturnedToHabitatTextView!!.text = if (returnedToHabLevel == 0) "No" else "Level $returnedToHabLevel"
            endGameReturnedToHabitatAttemptsTextView!!.text = if (returnedToHabAttemptLevel == 0) "No" else "Level $returnedToHabAttemptLevel"
        }


        return view
    }


    //endregion

    /**
     * Validates all fields on the form for saving
     * @return boolean if fields valid
     */
    fun validateFields(): Boolean
    {
        //placeholder in case of future validation needed
        return true
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
         * @param scoutCard
         * @return A new instance of fragment ScoutCardEndGameFragment.
         */
        fun newInstance(scoutCard: ScoutCard?): ScoutCardEndGameFragment
        {
            val fragment = ScoutCardEndGameFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_PARAM_SCOUT_CARD_JSON, MasterFragment.toJson(scoutCard))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

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
 * [ScoutCardAutoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoutCardAutoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutCardAutoFragment : MasterFragment()
{

    private var mListener: OnFragmentInteractionListener? = null

    //Exit Habitat
    private var autonomousExitHabitatTextView: TextView? = null

    private var autonomousExitHabitatNoButton: Button? = null
    private var autonomousExitHabitatYesButton: Button? = null

    //Hatch Panels
    private var autonomousHatchPanelsPickupTextView: TextView? = null
    private var autonomousHatchPanelsSecuredAttemptsTextView: TextView? = null
    private var autonomousHatchPanelsSecuredTextView: TextView? = null

    private var autonomousHatchPanelsPickupMinusOneButton: Button? = null
    private var autonomousHatchPanelsPickupPlusOneButton: Button? = null

    private var autonomousHatchPanelsSecuredMinusOneButton: Button? = null
    private var autonomousHatchPanelsSecuredPlusOneButton: Button? = null

    private var autonomousHatchPanelsSecuredAttemptsMinusOneButton: Button? = null
    private var autonomousHatchPanelsSecuredAttemptsPlusOneButton: Button? = null

    //Cargo
    private var autonomousCargoPickupTextView: TextView? = null
    private var autonomousCargoStoredAttemptsTextView: TextView? = null
    private var autonomousCargoStoredTextView: TextView? = null


    private var autonomousCargoPickupMinusOneButton: Button? = null
    private var autonomousCargoPickupPlusOneButton: Button? = null

    private var autonomousCargoStoredAttemptsMinusOneButton: Button? = null
    private var autonomousCargoStoredAttemptsPlusOneButton: Button? = null

    private var autonomousCargoStoredMinusOneButton: Button? = null
    private var autonomousCargoStoredPlusOneButton: Button? = null

    //region Getters

    val autonomousExitHab: Boolean
        get() = autonomousExitHabitatTextView!!.text.toString() == context.getString(R.string.yes)

    val autonomousHatchPanelsSecured: Int
        get() = Integer.parseInt(autonomousHatchPanelsSecuredTextView!!.text.toString())

    val autonomousHatchPanelsDropped: Int
        get() = Integer.parseInt(autonomousHatchPanelsSecuredAttemptsTextView!!.text.toString())

    val autonomousHatchPanelsPickedUp: Int
        get() = Integer.parseInt(autonomousHatchPanelsPickupTextView!!.text.toString())

    val autonomousCargoStored: Int
        get() = Integer.parseInt(autonomousCargoStoredTextView!!.text.toString())

    val autonomousCargoDropped: Int
        get() = Integer.parseInt(autonomousCargoStoredAttemptsTextView!!.text.toString())

    val autonomousCargoPickedUp: Int
        get() = Integer.parseInt(autonomousCargoPickupTextView!!.text.toString())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_auto, container, false)

        joinLoadingThread()

        //Exit Habitat
        autonomousExitHabitatTextView = view.findViewById(R.id.AutonomousExitHabitatTextView)

        autonomousExitHabitatNoButton = view.findViewById(R.id.AutonomousExitHabitatNoButton)
        autonomousExitHabitatYesButton = view.findViewById(R.id.AutonomousExitHabitatYesButton)

        autonomousExitHabitatNoButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousExitHabitatTextView!!.setText(R.string.no)
        }

        autonomousExitHabitatYesButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousExitHabitatTextView!!.setText(R.string.yes)
        }


        //Hatch Panels
        autonomousHatchPanelsPickupTextView = view.findViewById(R.id.AutonomousHatchPanelsPickupTextView)
        autonomousHatchPanelsSecuredAttemptsTextView = view.findViewById(R.id.AutonomousHatchPanelsSecuredAttemptsTextView)
        autonomousHatchPanelsSecuredTextView = view.findViewById(R.id.AutonomousHatchPanelsSecuredTextView)


        autonomousHatchPanelsPickupMinusOneButton = view.findViewById(R.id.AutonomousHatchPanelsPickupMinusOneButton)
        autonomousHatchPanelsPickupPlusOneButton = view.findViewById(R.id.AutonomousHatchPanelsPickupPlusOneButton)

        autonomousHatchPanelsSecuredAttemptsMinusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredAttemptsMinusOneButton)
        autonomousHatchPanelsSecuredAttemptsPlusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredAttemptsPlusOneButton)

        autonomousHatchPanelsSecuredMinusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredMinusOneButton)
        autonomousHatchPanelsSecuredPlusOneButton = view.findViewById(R.id.AutonomousHatchPanelsSecuredPlusOneButton)

        autonomousHatchPanelsPickupMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousHatchPanelsPickupTextView!!.text = (Integer.parseInt(autonomousHatchPanelsPickupTextView!!.text.toString()) - 1).toString() + ""
        }

        autonomousHatchPanelsPickupPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousHatchPanelsPickupTextView!!.text = (Integer.parseInt(autonomousHatchPanelsPickupTextView!!.text.toString()) + 1).toString() + ""
        }

        autonomousHatchPanelsSecuredAttemptsMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousHatchPanelsSecuredAttemptsTextView!!.text = (Integer.parseInt(autonomousHatchPanelsSecuredAttemptsTextView!!.text.toString()) - 1).toString() + ""
        }

        autonomousHatchPanelsSecuredAttemptsPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousHatchPanelsSecuredAttemptsTextView!!.text = (Integer.parseInt(autonomousHatchPanelsSecuredAttemptsTextView!!.text.toString()) + 1).toString() + ""
        }

        autonomousHatchPanelsSecuredMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousHatchPanelsSecuredTextView!!.text = (Integer.parseInt(autonomousHatchPanelsSecuredTextView!!.text.toString()) - 1).toString() + ""
        }

        autonomousHatchPanelsSecuredPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousHatchPanelsSecuredTextView!!.text = (Integer.parseInt(autonomousHatchPanelsSecuredTextView!!.text.toString()) + 1).toString() + ""
        }


        //Cargo
        autonomousCargoPickupTextView = view.findViewById(R.id.AutonomousCargoPickupTextView)
        autonomousCargoStoredAttemptsTextView = view.findViewById(R.id.AutonomousCargoStoredAttemptsTextView)
        autonomousCargoStoredTextView = view.findViewById(R.id.AutonomousCargoStoredTextView)


        autonomousCargoPickupMinusOneButton = view.findViewById(R.id.AutonomousCargoPickupMinusOneButton)
        autonomousCargoPickupPlusOneButton = view.findViewById(R.id.AutonomousCargoPickupPlusOneButton)

        autonomousCargoStoredAttemptsPlusOneButton = view.findViewById(R.id.AutonomousCargoStoredAttemptsPlusOneButton)
        autonomousCargoStoredAttemptsMinusOneButton = view.findViewById(R.id.AutonomousCargoStoredAttemptsMinusOneButton)

        autonomousCargoStoredMinusOneButton = view.findViewById(R.id.AutonomousCargoStoredMinusOneButton)
        autonomousCargoStoredPlusOneButton = view.findViewById(R.id.AutonomousCargoStoredPlusOneButton)

        autonomousCargoPickupMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousCargoPickupTextView!!.text = (Integer.parseInt(autonomousCargoPickupTextView!!.text.toString()) - 1).toString() + ""
        }

        autonomousCargoPickupPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousCargoPickupTextView!!.text = (Integer.parseInt(autonomousCargoPickupTextView!!.text.toString()) + 1).toString() + ""
        }

        autonomousCargoStoredAttemptsMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousCargoStoredAttemptsTextView!!.text = (Integer.parseInt(autonomousCargoStoredAttemptsTextView!!.text.toString()) - 1).toString() + ""
        }

        autonomousCargoStoredAttemptsPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousCargoStoredAttemptsTextView!!.text = (Integer.parseInt(autonomousCargoStoredAttemptsTextView!!.text.toString()) + 1).toString() + ""
        }

        autonomousCargoStoredMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousCargoStoredTextView!!.text = (Integer.parseInt(autonomousCargoStoredTextView!!.text.toString()) - 1).toString() + ""
        }

        autonomousCargoStoredPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                autonomousCargoStoredTextView!!.text = (Integer.parseInt(autonomousCargoStoredTextView!!.text.toString()) + 1).toString() + ""
        }

        //scoutcard loaded, populate fields
        if (scoutCard != null)
        {
            autonomousHatchPanelsPickupTextView!!.text = scoutCard!!.autonomousHatchPanelsPickedUp.toString()
            autonomousHatchPanelsSecuredAttemptsTextView!!.text = scoutCard!!.autonomousHatchPanelsSecuredAttempts.toString()
            autonomousHatchPanelsSecuredTextView!!.text = scoutCard!!.autonomousHatchPanelsSecured.toString()

            autonomousCargoPickupTextView!!.text = scoutCard!!.autonomousCargoPickedUp.toString()
            autonomousCargoStoredAttemptsTextView!!.text = scoutCard!!.autonomousCargoStoredAttempts.toString()
            autonomousCargoStoredTextView!!.text = scoutCard!!.autonomousCargoStored.toString()

            autonomousExitHabitatTextView!!.setText(if (scoutCard!!.autonomousExitHabitat) R.string.yes else R.string.no)
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
         * @return A new instance of fragment ScoutCardAutoFragment.
         */
        fun newInstance(scoutCard: ScoutCard?): ScoutCardAutoFragment
        {
            val fragment = ScoutCardAutoFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_PARAM_SCOUT_CARD_JSON, MasterFragment.toJson(scoutCard))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

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
 * [ScoutCardTeleopFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoutCardTeleopFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutCardTeleopFragment : MasterFragment()
{

    private var mListener: OnFragmentInteractionListener? = null

    //Hatch Panels
    private var teleopHatchPanelsPickupTextView: TextView? = null
    private var teleopHatchPanelsSecuredAttemptsTextView: TextView? = null
    private var teleopHatchPanelsSecuredTextView: TextView? = null

    private var teleopHatchPanelsPickupMinusOneButton: Button? = null
    private var teleopHatchPanelsPickupPlusOneButton: Button? = null

    private var teleopHatchPanelsSecuredMinusOneButton: Button? = null
    private var teleopHatchPanelsSecuredPlusOneButton: Button? = null

    private var teleopHatchPanelsSecuredAttemptsMinusOneButton: Button? = null
    private var teleopHatchPanelsSecuredAttemptsPlusOneButton: Button? = null

    //Cargo
    private var teleopCargoPickupTextView: TextView? = null
    private var teleopCargoStoredAttemptsTextView: TextView? = null
    private var teleopCargoStoredTextView: TextView? = null


    private var teleopCargoPickupMinusOneButton: Button? = null
    private var teleopCargoPickupPlusOneButton: Button? = null

    private var teleopCargoStoredAttemptsMinusOneButton: Button? = null
    private var teleopCargoStoredAttemptsPlusOneButton: Button? = null

    private var teleopCargoStoredMinusOneButton: Button? = null
    private var teleopCargoStoredPlusOneButton: Button? = null

    //region Getters

    val teleopHatchPanelsSecured: Int
        get() = Integer.parseInt(teleopHatchPanelsSecuredTextView!!.text.toString())

    val teleopHatchPanelsDropped: Int
        get() = Integer.parseInt(teleopHatchPanelsSecuredAttemptsTextView!!.text.toString())

    val teleopHatchPanelsPickedUp: Int
        get() = Integer.parseInt(teleopHatchPanelsPickupTextView!!.text.toString())

    val teleopCargoStored: Int
        get() = Integer.parseInt(teleopCargoStoredTextView!!.text.toString())

    val teleopCargoDropped: Int
        get() = Integer.parseInt(teleopCargoStoredAttemptsTextView!!.text.toString())

    val teleopCargoPickedUp: Int
        get() = Integer.parseInt(teleopCargoPickupTextView!!.text.toString())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_teleop, container, false)

        joinLoadingThread()

        //Hatch Panels
        teleopHatchPanelsPickupTextView = view.findViewById(R.id.TeleopHatchPanelsPickupTextView)
        teleopHatchPanelsSecuredAttemptsTextView = view.findViewById(R.id.TeleopHatchPanelsSecuredAttemptsTextView)
        teleopHatchPanelsSecuredTextView = view.findViewById(R.id.TeleopHatchPanelsSecuredTextView)


        teleopHatchPanelsPickupMinusOneButton = view.findViewById(R.id.TeleopHatchPanelsPickupMinusOneButton)
        teleopHatchPanelsPickupPlusOneButton = view.findViewById(R.id.TeleopHatchPanelsPickupPlusOneButton)

        teleopHatchPanelsSecuredAttemptsMinusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredAttemptsMinusOneButton)
        teleopHatchPanelsSecuredAttemptsPlusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredAttemptsPlusOneButton)

        teleopHatchPanelsSecuredMinusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredMinusOneButton)
        teleopHatchPanelsSecuredPlusOneButton = view.findViewById(R.id.TeleopHatchPanelsSecuredPlusOneButton)

        teleopHatchPanelsPickupMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopHatchPanelsPickupTextView!!.text = (Integer.parseInt(teleopHatchPanelsPickupTextView!!.text.toString()) - 1).toString() + ""
        }

        teleopHatchPanelsPickupPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopHatchPanelsPickupTextView!!.text = (Integer.parseInt(teleopHatchPanelsPickupTextView!!.text.toString()) + 1).toString() + ""
        }

        teleopHatchPanelsSecuredAttemptsMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopHatchPanelsSecuredAttemptsTextView!!.text = (Integer.parseInt(teleopHatchPanelsSecuredAttemptsTextView!!.text.toString()) - 1).toString() + ""
        }

        teleopHatchPanelsSecuredAttemptsPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopHatchPanelsSecuredAttemptsTextView!!.text = (Integer.parseInt(teleopHatchPanelsSecuredAttemptsTextView!!.text.toString()) + 1).toString() + ""
        }

        teleopHatchPanelsSecuredMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopHatchPanelsSecuredTextView!!.text = (Integer.parseInt(teleopHatchPanelsSecuredTextView!!.text.toString()) - 1).toString() + ""
        }

        teleopHatchPanelsSecuredPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopHatchPanelsSecuredTextView!!.text = (Integer.parseInt(teleopHatchPanelsSecuredTextView!!.text.toString()) + 1).toString() + ""
        }


        //Cargo
        teleopCargoPickupTextView = view.findViewById(R.id.TeleopCargoPickupTextView)
        teleopCargoStoredAttemptsTextView = view.findViewById(R.id.TeleopCargoStoredAttemptsTextView)
        teleopCargoStoredTextView = view.findViewById(R.id.TeleopCargoStoredTextView)


        teleopCargoPickupMinusOneButton = view.findViewById(R.id.TeleopCargoPickupMinusOneButton)
        teleopCargoPickupPlusOneButton = view.findViewById(R.id.TeleopCargoPickupPlusOneButton)

        teleopCargoStoredAttemptsPlusOneButton = view.findViewById(R.id.TeleopCargoStoredAttemptsPlusOneButton)
        teleopCargoStoredAttemptsMinusOneButton = view.findViewById(R.id.TeleopCargoStoredAttemptsMinusOneButton)

        teleopCargoStoredMinusOneButton = view.findViewById(R.id.TeleopCargoStoredMinusOneButton)
        teleopCargoStoredPlusOneButton = view.findViewById(R.id.TeleopCargoStoredPlusOneButton)

        teleopCargoPickupMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopCargoPickupTextView!!.text = (Integer.parseInt(teleopCargoPickupTextView!!.text.toString()) - 1).toString() + ""
        }

        teleopCargoPickupPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopCargoPickupTextView!!.text = (Integer.parseInt(teleopCargoPickupTextView!!.text.toString()) + 1).toString() + ""
        }

        teleopCargoStoredAttemptsMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopCargoStoredAttemptsTextView!!.text = (Integer.parseInt(teleopCargoStoredAttemptsTextView!!.text.toString()) - 1).toString() + ""
        }

        teleopCargoStoredAttemptsPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopCargoStoredAttemptsTextView!!.text = (Integer.parseInt(teleopCargoStoredAttemptsTextView!!.text.toString()) + 1).toString() + ""
        }

        teleopCargoStoredMinusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopCargoStoredTextView!!.text = (Integer.parseInt(teleopCargoStoredTextView!!.text.toString()) - 1).toString() + ""
        }

        teleopCargoStoredPlusOneButton!!.setOnClickListener {
            if (scoutCard == null || scoutCard!!.isDraft)
                teleopCargoStoredTextView!!.text = (Integer.parseInt(teleopCargoStoredTextView!!.text.toString()) + 1).toString() + ""
        }

        //scoutcard loaded, populate fields
        if (scoutCard != null)
        {
            teleopHatchPanelsPickupTextView!!.text = scoutCard!!.teleopHatchPanelsPickedUp.toString()
            teleopHatchPanelsSecuredAttemptsTextView!!.text = scoutCard!!.teleopHatchPanelsSecuredAttempts.toString()
            teleopHatchPanelsSecuredTextView!!.text = scoutCard!!.teleopHatchPanelsSecured.toString()

            teleopCargoPickupTextView!!.text = scoutCard!!.teleopCargoPickedUp.toString()
            teleopCargoStoredAttemptsTextView!!.text = scoutCard!!.teleopCargoStoredAttempts.toString()
            teleopCargoStoredTextView!!.text = scoutCard!!.teleopCargoStored.toString()
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
         * @return A new instance of fragment ScoutCardTeleopFragment.
         */
        fun newInstance(scoutCard: ScoutCard?): ScoutCardTeleopFragment
        {
            val fragment = ScoutCardTeleopFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_PARAM_SCOUT_CARD_JSON, MasterFragment.toJson(scoutCard))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

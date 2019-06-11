package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RatingBar

import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard
import com.alphadevelopmentsolutions.frcscout.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoutCardPostGameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoutCardPostGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutCardPostGameFragment : MasterFragment()
{

    private var mListener: OnFragmentInteractionListener? = null

    private var onSaveButtonClickListener: View.OnClickListener? = null

    private var matchNotesEditText: EditText? = null

    private var offenseRatingBar: RatingBar? = null
    private var defenseRatingBar: RatingBar? = null
    private var driveRatingBar: RatingBar? = null

    //region Getters

    val defenseRating: Int
        get() = defenseRatingBar!!.rating.toInt()

    val offenseRating: Int
        get() = offenseRatingBar!!.rating.toInt()

    val driveRating: Int
        get() = driveRatingBar!!.rating.toInt()

    val notes: String
        get() = matchNotesEditText!!.text.toString()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_post_game, container, false)


        matchNotesEditText = view.findViewById(R.id.MatchNotesEditText)

        defenseRatingBar = view.findViewById(R.id.DefenseRatingBar)
        offenseRatingBar = view.findViewById(R.id.OffenseRatingBar)
        driveRatingBar = view.findViewById(R.id.DriveRatingBar)

        joinLoadingThread()

        if (scoutCard != null)
        {

            defenseRatingBar!!.rating = scoutCard!!.defenseRating.toFloat()
            offenseRatingBar!!.rating = scoutCard!!.offenseRating.toFloat()
            driveRatingBar!!.rating = scoutCard!!.driveRating.toFloat()

            matchNotesEditText!!.setText(scoutCard!!.notes)

            //only disable if card is not draft
            if (!scoutCard!!.isDraft)
            {
                defenseRatingBar!!.isEnabled = false
                offenseRatingBar!!.isEnabled = false
                driveRatingBar!!.isEnabled = false

                matchNotesEditText!!.isFocusable = false
                matchNotesEditText!!.inputType = InputType.TYPE_NULL

            }
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

        return true
    }

    /**
     * Stores the save button click listener used for calling upwards to the scoutcard frag
     * @param onSaveButtonClickListener used to set scout card save button
     */
    fun setOnSaveButtonClickListener(onSaveButtonClickListener: View.OnClickListener)
    {
        this.onSaveButtonClickListener = onSaveButtonClickListener
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
         * @return A new instance of fragment ScoutCardPostGameFragment.
         */
        fun newInstance(scoutCard: ScoutCard?): ScoutCardPostGameFragment
        {
            val fragment = ScoutCardPostGameFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_PARAM_SCOUT_CARD_JSON, MasterFragment.toJson(scoutCard))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

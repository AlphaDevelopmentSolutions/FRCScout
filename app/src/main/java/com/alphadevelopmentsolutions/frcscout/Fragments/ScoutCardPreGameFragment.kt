package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Spinner
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.User
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPiece
import com.alphadevelopmentsolutions.frcscout.Enums.StartingPosition
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoutCardPreGameFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoutCardPreGameFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutCardPreGameFragment : MasterFragment()
{

    private var mListener: OnFragmentInteractionListener? = null

    private var teamNumberTextInputEditText: TextInputEditText? = null
    private var scouterNameAutoCompleteTextView: AutoCompleteTextView? = null

    private var startingPositionSpinner: Spinner? = null
    private var startingLevelSpinner: Spinner? = null
    private var startingPieceSpinner: Spinner? = null

    private var scouterNames: ArrayList<String>? = null

    private var loadScouterNamesThread: Thread? = null
    private var scouterNameAdapter: ArrayAdapter<String>? = null

    //region Getters

    val teamId: Int
        get() = Integer.parseInt(teamNumberTextInputEditText!!.text!!.toString())

    val scouterName: String
        get() = scouterNameAutoCompleteTextView!!.text.toString()


    val startingPosition: StartingPosition
        get() = StartingPosition.getPositionFromString(startingPositionSpinner!!.selectedItem.toString())

    val startingLevel: Int
        get() = Integer.parseInt(startingLevelSpinner!!.selectedItem.toString().substring(startingLevelSpinner!!.selectedItem.toString().indexOf(" ") + 1))

    val startingPiece: StartingPiece
        get() = StartingPiece.getPieceFromString(startingPieceSpinner!!.selectedItem.toString())

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)


        //load the user auto complete data on a new thread
        loadScouterNamesThread = Thread(Runnable {
            joinLoadingThread()

            //populate the scouter name auto complete textview
            scouterNames = ArrayList()

            //get all users
            for (user in User.getObjects(null, database)!!)
                scouterNames!!.add(user.toString())

            scouterNameAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, scouterNames!!)
        })
        loadScouterNamesThread!!.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_pre_game, container, false)

        teamNumberTextInputEditText = view.findViewById(R.id.TeamNumberTextInputEditText)
        scouterNameAutoCompleteTextView = view.findViewById(R.id.ScouterNameAutoCompleteTextView)

        startingPositionSpinner = view.findViewById(R.id.StartingPositionSpinner)
        startingLevelSpinner = view.findViewById(R.id.StartingLevelSpinner)
        startingPieceSpinner = view.findViewById(R.id.StartingGamePieceSpinner)

        teamNumberTextInputEditText!!.setText(team!!.id.toString())
        teamNumberTextInputEditText!!.isFocusable = false
        teamNumberTextInputEditText!!.inputType = InputType.TYPE_NULL

        Thread(Runnable {
            try
            {
                loadScouterNamesThread!!.join()

                context.runOnUiThread { scouterNameAutoCompleteTextView!!.setAdapter<ArrayAdapter<String>>(scouterNameAdapter) }
            } catch (e: InterruptedException)
            {
                e.printStackTrace()
            }
        }).start()

        //scoutcard loaded, populate fields
        if (scoutCard != null)
        {
            scouterNameAutoCompleteTextView!!.setText(scoutCard!!.completedBy)

            startingLevelSpinner!!.setSelection(scoutCard!!.preGameStartingLevel - 1)
            startingPositionSpinner!!.setSelection(if (scoutCard!!.preGameStartingPosition == StartingPosition.LEFT) 0 else if (scoutCard!!.preGameStartingPosition == StartingPosition.CENTER) 1 else 2)
            startingPieceSpinner!!.setSelection(if (scoutCard!!.preGameStartingPiece == StartingPiece.HATCH) 0 else 1)

            //only disable fields if card is not draft
            if (!scoutCard!!.isDraft)
            {
                scouterNameAutoCompleteTextView!!.isFocusable = false
                scouterNameAutoCompleteTextView!!.inputType = InputType.TYPE_NULL

                startingLevelSpinner!!.isEnabled = false
                startingPositionSpinner!!.isEnabled = false
                startingPieceSpinner!!.isEnabled = false

            }
        }


        return view
    }

    //endregion

    fun validateFields(): Boolean
    {
        if (!teamNumberTextInputEditText!!.text!!.toString().matches("^[-+]?\\d*$".toRegex()) || teamNumberTextInputEditText!!.text!!.toString() == "")
        {
            context.showSnackbar("Invalid team number.")
            return false
        }

        if (scouterNameAutoCompleteTextView!!.text.toString() == "")
        {
            context.showSnackbar("Invalid scouter name.")
            return false
        }

        return true
    }

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
         * @param team
         * @return A new instance of fragment ScoutCardPreGameFragment.
         */
        fun newInstance(scoutCard: ScoutCard?, team: Team): ScoutCardPreGameFragment
        {
            val fragment = ScoutCardPreGameFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_PARAM_SCOUT_CARD_JSON, MasterFragment.toJson(scoutCard))
            args.putString(MasterFragment.ARG_TEAM_JSON, MasterFragment.toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

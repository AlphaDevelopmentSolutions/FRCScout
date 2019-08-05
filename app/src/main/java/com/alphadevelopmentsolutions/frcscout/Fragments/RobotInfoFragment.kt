package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TextInputLayout
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [RobotInfoFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [RobotInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RobotInfoFragment : MasterFragment()
{
    private var mListener: OnFragmentInteractionListener? = null


    private var robotInfoLinearLayout: LinearLayout? = null
    private var saveButton: Button? = null

    private var editTexts: ArrayList<EditText>? = null
    private var infoIds: ArrayList<Int>? = null
    private var infoKeys: ArrayList<String>? = null
    private var infoStates: ArrayList<String>? = null
    private var infoKeyIds: ArrayList<Int>? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_info, container, false)

        //gets rid of the shadow on the actionbar
        context.dropActionBar()



        robotInfoLinearLayout = view.findViewById(R.id.RobotInfoLinearLayout)
        saveButton = view.findViewById(R.id.SaveButton)

        saveButton!!.setTextColor(context.primaryColor)

        Thread(Runnable {
            editTexts = ArrayList()
            infoIds = ArrayList()
            infoKeys = ArrayList()
            infoStates = ArrayList()
            infoKeyIds = ArrayList()

            val year = Year(event!!.yearId, database)

            val robotInfoKeys = RobotInfoKey.getObjects(year, null, database)

            var robotInfoKey: RobotInfoKey
            var nextRobotInfoKey: RobotInfoKey?

            var robotInfos: ArrayList<RobotInfo>?
            var robotInfo: RobotInfo?

            var linearLayout = LinearLayout(context)

            var currentInfoKeyState: String? = ""

            val scale = resources.displayMetrics.density

            var j = 1

            for (i in robotInfoKeys!!.indices)
            {
                robotInfoKey = robotInfoKeys[i]
                nextRobotInfoKey = if (i + 1 < robotInfoKeys.size) robotInfoKeys[i + 1] else null

                robotInfos = RobotInfo.getObjects(null, event, team, robotInfoKey, null, false, database)
                robotInfo = if (robotInfos!!.size > 0) robotInfos[robotInfos.size - 1] else null

                if (currentInfoKeyState != robotInfoKey.keyState)
                {
                    if (i > 0)
                    {
                        val padding = (1 * scale + 0.5f).toInt()
                        val divider = LinearLayout(context)
                        divider.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, padding)
                        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.divider))

                        context.runOnUiThread { robotInfoLinearLayout!!.addView(divider) }
                    }

                    val textView = TextView(context)
                    textView.text = robotInfoKey.keyState
                    textView.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)

                    val padding = (8 * scale + 0.5f).toInt()
                    textView.setPadding(padding, padding, 0, 0)
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f)

                    context.runOnUiThread { robotInfoLinearLayout!!.addView(textView) }

                    currentInfoKeyState = robotInfoKey.keyState

                }

                val textInputLayout = TextInputLayout(context)

                val editText = EditText(context)
                editText.hint = robotInfoKey.keyName
                if (robotInfo != null)
                {
                    infoIds!!.add(robotInfo.id)
                    editText.setText(robotInfo.propertyValue)
                } else
                    infoIds!!.add(-1)


                textInputLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)

                textInputLayout.addView(editText)

                editTexts!!.add(editText)
                infoKeys!!.add(robotInfoKey.keyName)
                infoStates!!.add(robotInfoKey.keyState)
                infoKeyIds!!.add(robotInfoKey.serverId)

                linearLayout.addView(textInputLayout)

                if (j % 2 == 0 && currentInfoKeyState == robotInfoKey.keyState || nextRobotInfoKey == null || currentInfoKeyState != nextRobotInfoKey.keyState)
                {
                    j = 0
                    val padding = (8 * scale + 0.5f).toInt()
                    linearLayout.setPadding(padding, padding, padding, padding)

                    val finalLinearLayout = linearLayout
                    context.runOnUiThread { robotInfoLinearLayout!!.addView(finalLinearLayout) }

                    linearLayout = LinearLayout(context)
                }

                j++
            }

            saveButton!!.setOnClickListener {
                for (i in editTexts!!.indices)
                {
                    val nRobotInfo = RobotInfo(
                            -1,
                            event!!.yearId,
                            event!!.blueAllianceId!!,
                            team!!.id!!,
                            editTexts!![i].text.toString(),
                            infoKeyIds!![i],
                            true)

                    nRobotInfo.save(database)
                }

                context.runOnUiThread { context.showSnackbar("Robot Info Saved!") }
            }
        }).start()


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
         * @param team
         * @return A new instance of fragment RobotInfoFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(team: Team): RobotInfoFragment
        {
            val fragment = RobotInfoFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_TEAM_JSON, MasterFragment.toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

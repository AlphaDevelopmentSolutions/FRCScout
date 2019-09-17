package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_robot_info.view.*
import kotlinx.android.synthetic.main.layout_card_scout_card_info_form.view.*
import kotlinx.android.synthetic.main.layout_field_info.view.*
import java.util.*

class RobotInfoFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var layoutFields: ArrayList<View> = ArrayList()
    private var robotInfoKeyStates: LinkedHashMap<String, ArrayList<RobotInfoKey>> = LinkedHashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutCreationThread = Thread(Runnable {

            loadingThread.join()

            val robotInfoKeys = RobotInfoKey.getObjects(year, null, database)
            val robotInfos = RobotInfo.getObjects(year, event, team, null, null, false, database)

            for(robotInfoKey in robotInfoKeys)
            {
                if(robotInfoKeyStates[robotInfoKey.keyState] != null)
                    robotInfoKeyStates[robotInfoKey.keyState]!!.add(robotInfoKey)

                else
                    robotInfoKeyStates[robotInfoKey.keyState] = arrayListOf(robotInfoKey)
            }

            for((infoKeyName, infoKeyValueArray) in robotInfoKeyStates)
            {
                val view = context.layoutInflater.inflate(R.layout.layout_card_scout_card_info_form, null)
                view.ScoutCardInfoFormCardTitle.text = infoKeyName

                for(infoKey in infoKeyValueArray)
                {
                    val fieldLinearLayout = LinearLayout(context)
                    fieldLinearLayout.orientation = LinearLayout.VERTICAL
                    fieldLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    val infoKeyView = context.layoutInflater.inflate(R.layout.layout_field_info, null)

                    var robotInfo = RobotInfo(
                            -1,
                            year!!.serverId!!,
                            event!!.blueAllianceId!!,
                            team!!.id!!,
                            "",
                            infoKey.serverId,
                            true
                    )

                    for(info in robotInfos)
                    {
                        if(info.propertyKeyId == infoKey.serverId)
                            robotInfo = info
                    }

                    infoKeyView.InfoKeyTitle.text = infoKey.keyName
                    infoKeyView.TextLinearLayout.visibility = View.VISIBLE
                    infoKeyView.TextEditText.setText(robotInfo.propertyValue)
                    infoKeyView.TextEditText.addTextChangedListener(object : TextWatcher
                    {
                        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
                        {

                        }

                        override fun onTextChanged(searchText: CharSequence, start: Int, before: Int, count: Int)
                        {
                            robotInfo.propertyValue = searchText.toString()
                            robotInfo.save(database)
                        }

                        override fun afterTextChanged(editable: Editable)
                        {

                        }
                    })

                    fieldLinearLayout.addView(infoKeyView)

                    view.ScoutCardInfoFormFieldsLinearLayout.addView(fieldLinearLayout)
                }

                layoutFields.add(view)
            }
        })

        layoutCreationThread.start()
    }

    private lateinit var layoutCreationThread: Thread
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_info, container, false)

        //gets rid of the shadow on the actionbar
        context.dropActionBar()

        Thread(Runnable{
            layoutCreationThread.join()

            context.runOnUiThread{

                if(layoutFields.size > 0)
                    with(layoutFields[0].parent)
                    {
                        if(this != null)
                        {
                            if((this as ViewGroup).childCount > 0)
                                this.removeAllViews()
                        }
                    }

                //add all the dynamic form frags to the viewpager
                for(layoutField in layoutFields)
                {
                    view.RobotInfoFormListLinearLayout.addView(layoutField)
                }

                isLoading = false
            }

        }).start()

        loadingThread.join()
        isLoading = true

        return super.onCreateView(view, true)
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param team to grab robot info from
         * @return A new instance of fragment [RobotInfoFragment].
         */
        fun newInstance(team: Team): RobotInfoFragment
        {
            val fragment = RobotInfoFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

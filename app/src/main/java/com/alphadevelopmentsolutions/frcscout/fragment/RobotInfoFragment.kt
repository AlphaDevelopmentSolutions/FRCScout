package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alphadevelopmentsolutions.frcscout.classes.Tables.RobotInfo
import com.alphadevelopmentsolutions.frcscout.classes.Tables.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.Tables.Team
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        layoutCreationThread = Thread(Runnable {

            loadingThread.join()

            val robotInfoKeys = RobotInfoKey.getObjects(year, null, database)
            val robotInfos = RobotInfo.getObjects(year, event, team, null, null, false, database)
            val robotInfoKeyStates: LinkedHashMap<String, ArrayList<RobotInfoKey>> = LinkedHashMap()

            //split all info states with their keys
            for(robotInfoKey in robotInfoKeys)
            {
                if(robotInfoKeyStates[robotInfoKey.keyState] != null)
                    robotInfoKeyStates[robotInfoKey.keyState]!!.add(robotInfoKey)

                else
                    robotInfoKeyStates[robotInfoKey.keyState] = arrayListOf(robotInfoKey)
            }

            //iterate through each state in the array
            for((infoKeyName, infoKeyValueArray) in robotInfoKeyStates)
            {
                //inflate a new info form card and set all the values
                val view = context.layoutInflater.inflate(R.layout.layout_card_scout_card_info_form, null)
                view.ScoutCardInfoFormCardTitle.text = infoKeyName

                //iterate through each info key in the array
                for(infoKey in infoKeyValueArray)
                {
                    var blockTextChange = false

                    //inflate the field info layout and set the values
                    val fieldLinearLayout = LinearLayout(context)
                    fieldLinearLayout.orientation = LinearLayout.VERTICAL
                    fieldLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    with(context.layoutInflater.inflate(R.layout.layout_field_info, null))
                    {
                        //set the default robot info
                        var robotInfo: RobotInfo? = null

                        //set the robot info to the preloaded one if found
                        for(info in robotInfos)
                        {
                            if(info.propertyKeyId == infoKey.serverId && robotInfo == null)
                                robotInfo = info
                        }

                        if (robotInfo?.isDraft == true)
                            DeleteButton.visibility = View.VISIBLE

                        DeleteButton.imageTintList = this@RobotInfoFragment.context.buttonBackground

                        //set the delete onclick
                        DeleteButton.setOnClickListener {
                            robotInfo?.delete(database)
                            robotInfo = null

                            //get the recent items from the db to replace the deleted one
                            with(RobotInfo.getObjects(year, event, team, infoKey, null, false, database))
                            {

                                //replace with the most recent
                                if (size > 0)
                                {
                                    robotInfo = this[size - 1]
                                    blockTextChange = true
                                    TextEditText.setText(robotInfo?.propertyValue)
                                }

                                //reset robot info and textview
                                else
                                {
                                    TextEditText.setText("")
                                }

                                //hide delete button
                                DeleteButton.visibility = View.GONE
                            }
                        }

                        InfoKeyTitle.text = infoKey.keyName
                        TextLinearLayout.visibility = View.VISIBLE
                        TextEditText.setText(robotInfo?.propertyValue)
                        TextEditText.backgroundTintList = this@RobotInfoFragment.context.editTextBackground
                        TextEditText.addTextChangedListener(object : TextWatcher
                        {
                            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
                            {

                            }

                            override fun onTextChanged(searchText: CharSequence, start: Int, before: Int, count: Int)
                            {

                                if (!blockTextChange)
                                {
                                    //if the current robot info isn't a draft, create a new robot info item to save
                                    if (robotInfo?.isDraft != true)
                                    {
                                        robotInfo = RobotInfo(
                                                -1,
                                                year!!.serverId,
                                                event!!.blueAllianceId,
                                                team!!.id,
                                                "",
                                                infoKey.serverId,
                                                true
                                        )
                                    }

                                    robotInfo!!.propertyValue = searchText.toString()
                                    robotInfo!!.save(database)


                                    with(DeleteButton)
                                    {
                                        if (visibility != View.VISIBLE)
                                            visibility = View.VISIBLE
                                    }
                                }

                                blockTextChange = false
                            }

                            override fun afterTextChanged(editable: Editable)
                            {

                            }
                        })

                        fieldLinearLayout.addView(this)
                    }

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

        return super.onCreateView(view)
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

package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.extension.init
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
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



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_robot_info, container, false)

        //gets rid of the shadow on the actionbar
        activityContext.dropActionBar()

        Thread(Runnable{

            VMProvider.getInstance(activityContext).robotInfoViewModel.objsViewForTeam(teamId!!).init()
                    .subscribe(
                            { robotInfoViewList ->

                                val robotInfoKeyStates: LinkedHashMap<String, ArrayList<RobotInfoKey>> = LinkedHashMap()

                                //split all info states with their keys
                                for(robotInfoView in robotInfoViewList)
                                {
                                    if(robotInfoKeyStates[robotInfoView.robotInfoKey.state] != null)
                                        robotInfoKeyStates[robotInfoView.robotInfoKey.state]!!.add(robotInfoView.robotInfoKey)

                                    else
                                        robotInfoKeyStates[robotInfoView.robotInfoKey.state] = arrayListOf(robotInfoView.robotInfoKey)
                                }

                                //iterate through each state in the array
                                for((infoKeyName, infoKeyValueArray) in robotInfoKeyStates)
                                {
                                    //inflate a new info form card and set all the values
                                    val view = activityContext.layoutInflater.inflate(R.layout.layout_card_scout_card_info_form, null)
                                    view.ScoutCardInfoFormCardTitle.text = infoKeyName

                                    //iterate through each info key in the array
                                    for(infoKey in infoKeyValueArray)
                                    {
                                        var blockTextChange = false

                                        //inflate the field info layout and set the values
                                        val fieldLinearLayout = LinearLayout(activityContext)
                                        fieldLinearLayout.orientation = LinearLayout.VERTICAL
                                        fieldLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                                        with(activityContext.layoutInflater.inflate(R.layout.layout_field_info, null))
                                        {
                                            //set the default robot info
                                            var robotInfo: RobotInfo? = null

                                            //set the robot info to the preloaded one if found
                                            for(robotInfoView in robotInfoViewList)
                                            {
                                                if(robotInfoView.robotInfo.keyId == infoKey.id && robotInfo == null)
                                                    robotInfo = robotInfoView.robotInfo
                                            }

                                            if (robotInfo?.isDraft == true)
                                                DeleteButton.visibility = View.VISIBLE

                                            DeleteButton.imageTintList = this@RobotInfoFragment.activityContext.buttonBackground

                                            //set the delete onclick
                                            DeleteButton.setOnClickListener {
                                                robotInfo?.let {
                                                    VMProvider.getInstance(activityContext).robotInfoViewModel.delete(it)
                                                }

                                                robotInfo = null
                                            }

                                            InfoKeyTitle.text = infoKey.name
                                            TextLinearLayout.visibility = View.VISIBLE
                                            TextEditText.setText(robotInfo?.value)
                                            TextEditText.backgroundTintList = this@RobotInfoFragment.activityContext.editTextBackground
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
                                                                    eventId!!,
                                                                    teamId!!,
                                                                    infoKey.id,
                                                                    ""
                                                            )
                                                        }

                                                        robotInfo!!.value = searchText.toString()

                                                        VMProvider.getInstance(activityContext).robotInfoViewModel.insert(robotInfo!!)

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
                            },
                            {
                                AppLog.error(it)
                            }
                    )

            activityContext.runOnUiThread{

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

        isLoading = true

        return onCreateView(view)
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
            args.putString(ARG_TEAM_ID, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

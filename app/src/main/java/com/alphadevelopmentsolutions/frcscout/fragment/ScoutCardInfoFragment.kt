package com.alphadevelopmentsolutions.frcscout.fragment

import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.extension.init
import com.alphadevelopmentsolutions.frcscout.extension.putUUID
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_scout_card_info.view.*
import kotlinx.android.synthetic.main.layout_card_scout_card_info_form.view.*
import kotlinx.android.synthetic.main.layout_field_info.view.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.LinkedHashMap

class ScoutCardInfoFragment : MasterFragment()
{
    private var layoutFields: ArrayList<View> = ArrayList()
    private var scoutCardInfoKeyStates: LinkedHashMap<String, ArrayList<ScoutCardInfoKey>> = LinkedHashMap()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_info, container, false)

        Thread(Runnable{

            VMProvider.getInstance(activityContext).scoutCardInfoViewModel.objsViewForTeam(teamId!!, matchId!!).init()
                    .subscribe(
                            { scoutCardInfoViewList ->

                                for(scoutCardInfoView in scoutCardInfoViewList)
                                {
                                    if(scoutCardInfoKeyStates[scoutCardInfoView.scoutCardInfoKey.state] != null)
                                        scoutCardInfoKeyStates[scoutCardInfoView.scoutCardInfoKey.state]!!.add(scoutCardInfoView.scoutCardInfoKey)

                                    else
                                        scoutCardInfoKeyStates[scoutCardInfoView.scoutCardInfoKey.state] = arrayListOf(scoutCardInfoView.scoutCardInfoKey)
                                }


                                for((infoKeyName, infoKeyValueArray) in scoutCardInfoKeyStates)
                                {
                                    val view = activityContext.layoutInflater.inflate(R.layout.layout_card_scout_card_info_form, null)
                                    view.ScoutCardInfoFormCardTitle.text = infoKeyName

                                    for(infoKey in infoKeyValueArray)
                                    {
                                        var blockTextChange = false

                                        val fieldLinearLayout = LinearLayout(activityContext)
                                        fieldLinearLayout.orientation = LinearLayout.VERTICAL
                                        fieldLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                                        with(activityContext.layoutInflater.inflate(R.layout.layout_field_info, null))
                                        {

                                            var scoutCardInfo: ScoutCardInfo? = null

                                            for (scoutCardInfoView in scoutCardInfoViewList)
                                            {
                                                if (scoutCardInfoView.scoutCardInfo.keyId == infoKey.id && scoutCardInfo == null)
                                                    scoutCardInfo = scoutCardInfoView.scoutCardInfo
                                            }

                                            if (scoutCardInfo?.isDraft == true)
                                                DeleteButton.visibility = View.VISIBLE

                                            InfoKeyTitle.text = infoKey.name

                                            DeleteButton.imageTintList = this@ScoutCardInfoFragment.activityContext.buttonBackground

                                            //set the delete onclick
                                            DeleteButton.setOnClickListener {
                                                scoutCardInfo?.let {
                                                    VMProvider.getInstance(activityContext).scoutCardInfoViewModel.delete(it)
                                                }

                                                scoutCardInfo = null
                                            }

                                            when (infoKey.dataType)
                                            {
                                                ScoutCardInfoKey.DataType.TEXT ->
                                                {
                                                    TextLinearLayout.visibility = View.VISIBLE
                                                    TextEditText.setText(scoutCardInfo?.value)
                                                    TextEditText.backgroundTintList = this@ScoutCardInfoFragment.activityContext.editTextBackground
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
                                                                if (scoutCardInfo?.isDraft != true)
                                                                {
                                                                    scoutCardInfo = ScoutCardInfo(
                                                                            matchId!!,
                                                                            teamId!!,
                                                                            Table.DEFAULT_UUID,
                                                                            "",
                                                                            infoKey.id
                                                                    )
                                                                }

                                                                scoutCardInfo!!.value = searchText.toString()

                                                                VMProvider.getInstance(activityContext).scoutCardInfoViewModel.insert(scoutCardInfo!!)

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

                                                }

                                                //create an int layout
                                                ScoutCardInfoKey.DataType.INT ->
                                                {
                                                    IntegerLinearLayout.visibility = View.VISIBLE

                                                    InfoKeyValue.text = if (scoutCardInfo?.value?.isNotBlank() == true) scoutCardInfo?.value ?: infoKey.min?.toString() ?: "0" else infoKey.min?.toString() ?: "0"

                                                    PlusButton.backgroundTintList = this@ScoutCardInfoFragment.activityContext.buttonBackground
                                                    (PlusButton as MaterialButton).rippleColor = this@ScoutCardInfoFragment.activityContext.buttonRipple

                                                    //Add button click handlers
                                                    PlusButton.setOnClickListener {

                                                        val curr = Integer.parseInt(InfoKeyValue.text.toString())

                                                        //Check for maximum in key
                                                        if (infoKey.max == null || curr < infoKey.max!!)
                                                        {
                                                            InfoKeyValue.text = (curr + 1).toString()

                                                            //if the current robot info isn't a draft, create a new robot info item to save
                                                            if (scoutCardInfo?.isDraft != true)
                                                            {
                                                                scoutCardInfo = ScoutCardInfo(
                                                                        matchId!!,
                                                                        teamId!!,
                                                                        Table.DEFAULT_UUID,
                                                                        "",
                                                                        infoKey.id
                                                                )
                                                            }

                                                            scoutCardInfo!!.value = InfoKeyValue.text.toString()
                                                            VMProvider.getInstance(activityContext).scoutCardInfoViewModel.insert(scoutCardInfo!!)


                                                            with(DeleteButton)
                                                            {
                                                                if (visibility != View.VISIBLE)
                                                                    visibility = View.VISIBLE
                                                            }

                                                        }
                                                        else
                                                            this@ScoutCardInfoFragment.activityContext.showSnackbar("This field has a maximum of " + infoKey.max)

                                                    }

                                                    MinusButton.backgroundTintList = this@ScoutCardInfoFragment.activityContext.buttonBackground
                                                    (MinusButton as MaterialButton).rippleColor = this@ScoutCardInfoFragment.activityContext.buttonRipple

                                                    MinusButton.setOnClickListener {

                                                        val curr = Integer.parseInt(InfoKeyValue.text.toString())

                                                        if (infoKey.min == null || curr > infoKey.min!!)
                                                        {
                                                            InfoKeyValue.text = (curr - 1).toString()

                                                            //if the current robot info isn't a draft, create a new robot info item to save
                                                            if (scoutCardInfo?.isDraft != true)
                                                            {
                                                                scoutCardInfo = ScoutCardInfo(
                                                                        matchId!!,
                                                                        teamId!!,
                                                                        Table.DEFAULT_UUID,
                                                                        "",
                                                                        infoKey.id
                                                                )
                                                            }

                                                            scoutCardInfo!!.value = InfoKeyValue.text.toString()
                                                            VMProvider.getInstance(activityContext).scoutCardInfoViewModel.insert(scoutCardInfo!!)


                                                            with(DeleteButton)
                                                            {
                                                                if (visibility != View.VISIBLE)
                                                                    visibility = View.VISIBLE
                                                            }
                                                        }
                                                        else
                                                            this@ScoutCardInfoFragment.activityContext.showSnackbar("This field has a minimum of " + infoKey.min)
                                                    }

                                                }

                                                //create a bool layout
                                                ScoutCardInfoKey.DataType.BOOL ->
                                                {
                                                    BooleanLinearLayout.visibility = View.VISIBLE
                                                    BooleanCheckBox.buttonTintList = this@ScoutCardInfoFragment.activityContext.checkboxBackground
                                                    (BooleanCheckBox.background as RippleDrawable).setColor(this@ScoutCardInfoFragment.activityContext.checkboxRipple)
                                                    BooleanCheckBox.isChecked = if (scoutCardInfo?.value?.isNotBlank() == true) scoutCardInfo?.value == "1" else false
                                                    BooleanCheckBox.setOnCheckedChangeListener { _, checked ->
                                                        run {

                                                            //if the current robot info isn't a draft, create a new robot info item to save
                                                            if (scoutCardInfo?.isDraft != true)
                                                            {
                                                                scoutCardInfo = ScoutCardInfo(
                                                                        matchId!!,
                                                                        teamId!!,
                                                                        Table.DEFAULT_UUID,
                                                                        "",
                                                                        infoKey.id
                                                                )
                                                            }

                                                            scoutCardInfo!!.value = if (checked) "1" else "0"
                                                            VMProvider.getInstance(activityContext).scoutCardInfoViewModel.insert(scoutCardInfo!!)


                                                            with(DeleteButton)
                                                            {
                                                                if (visibility != View.VISIBLE)
                                                                    visibility = View.VISIBLE
                                                            }
                                                        }
                                                    }

                                                }
                                            }

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

                //add all the dynamic form frags to the viewpager
                for(layoutField in layoutFields)
                    view.ScoutCardInfoFormListLinearLayout.addView(layoutField)

                isLoading = false
            }

        }).start()

        //gets rid of the shadow on the actionbar
        activityContext.dropActionBar()
        activityContext.isToolbarScrollable = false
        activityContext.lockDrawerLayout(true, View.OnClickListener { activityContext.onBackPressed() })

        isLoading = true

        //update the title of the page to display the matchId
        activityContext.setToolbarTitle("Match")

        return onCreateView(view)
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param match to get scout cards from
         * @param team to get scout cards from
         * @return A new instance of fragment [ScoutCardInfoFragment].
         */
        fun newInstance(matchId: UUID, teamId: UUID): ScoutCardInfoFragment
        {
            val fragment = ScoutCardInfoFragment()
            val args = Bundle()
            args.putUUID(ARG_MATCH_ID, matchId)
            args.putUUID(ARG_TEAM_ID, teamId)
            fragment.arguments = args
            return fragment
        }
    }
}

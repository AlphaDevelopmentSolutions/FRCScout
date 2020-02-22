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
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_scout_card_info.view.*
import kotlinx.android.synthetic.main.layout_card_scout_card_info_form.view.*
import kotlinx.android.synthetic.main.layout_field_info.view.*

class ScoutCardInfoFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private lateinit var layoutCreationThread: Thread

    private var layoutFields: ArrayList<View> = ArrayList()
    private var scoutCardInfoKeyStates: LinkedHashMap<String, ArrayList<ScoutCardInfoKey>> = LinkedHashMap()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //start the creation of fragments on a new thread
        layoutCreationThread = Thread(Runnable {
            loadingThread.join()

            val scoutCardInfoKeys = ScoutCardInfoKey.getObjects(yearId, null, database)
            val scoutCardInfos = ScoutCardInfo.getObjects(eventId, matchId, teamId, null, null, false, database)

            for(scoutCardInfoKey in scoutCardInfoKeys!!)
            {
                if(scoutCardInfoKeyStates[scoutCardInfoKey.keyState] != null)
                    scoutCardInfoKeyStates[scoutCardInfoKey.keyState]!!.add(scoutCardInfoKey)

                else
                    scoutCardInfoKeyStates[scoutCardInfoKey.keyState] = arrayListOf(scoutCardInfoKey)
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

                        for (info in scoutCardInfos)
                        {
                            if (info.propertyKeyId == infoKey.serverId && scoutCardInfo == null)
                                scoutCardInfo = info
                        }

                        if (scoutCardInfo?.isDraft == true)
                            DeleteButton.visibility = View.VISIBLE

                        InfoKeyTitle.text = infoKey.name

                        DeleteButton.imageTintList = this@ScoutCardInfoFragment.activityContext.buttonBackground

                        //set the delete onclick
                        DeleteButton.setOnClickListener {
                            scoutCardInfo?.delete(database)
                            scoutCardInfo = null

                            //get the recent items from the db to replace the deleted one
                            with(ScoutCardInfo.getObjects(eventId, matchId, teamId, infoKey, null, false, database))
                            {

                                //replace with the most recent
                                if (size > 0)
                                {
                                    scoutCardInfo = this[size - 1]

                                    when(infoKey.dataType)
                                    {
                                        ScoutCardInfoKey.DataTypes.TEXT ->
                                        {
                                            blockTextChange = true
                                            TextEditText.setText(scoutCardInfo?.value)
                                        }

                                        ScoutCardInfoKey.DataTypes.INT ->
                                        {
                                            InfoKeyValue.text = scoutCardInfo?.value
                                        }

                                        ScoutCardInfoKey.DataTypes.BOOL ->
                                        {
                                            BooleanCheckBox.isChecked = scoutCardInfo?.value == "1"
                                        }
                                    }
                                }

                                //reset robot info and textview
                                else
                                {
                                    when(infoKey.dataType)
                                    {
                                        ScoutCardInfoKey.DataTypes.TEXT ->
                                        {
                                            TextEditText.setText("")
                                        }

                                        ScoutCardInfoKey.DataTypes.INT ->
                                        {
                                            InfoKeyValue.text = infoKey.min.toString()
                                        }

                                        ScoutCardInfoKey.DataTypes.BOOL ->
                                        {
                                            BooleanCheckBox.isChecked = false
                                        }
                                    }
                                }
                                //hide delete button
                                DeleteButton.visibility = View.GONE
                            }
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
                                                        -1,
                                                        yearId!!.serverId,
                                                        eventId!!.blueAllianceId,
                                                        matchId!!.key,
                                                        teamId!!.id,
                                                        "",
                                                        "",
                                                        infoKey.serverId,
                                                        true
                                                )
                                            }

                                            scoutCardInfo!!.value = searchText.toString()
                                            scoutCardInfo!!.save(database)


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
                                                    -1,
                                                    yearId!!.serverId,
                                                    eventId!!.blueAllianceId,
                                                    matchId!!.key,
                                                    teamId!!.id,
                                                    "",
                                                    "",
                                                    infoKey.serverId,
                                                    true
                                            )
                                        }

                                        scoutCardInfo!!.value = InfoKeyValue.text.toString()
                                        scoutCardInfo!!.save(database)


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
                                                    -1,
                                                    yearId!!.serverId,
                                                    eventId!!.blueAllianceId,
                                                    matchId!!.key,
                                                    teamId!!.id,
                                                    "",
                                                    "",
                                                    infoKey.serverId,
                                                    true
                                            )
                                        }

                                        scoutCardInfo!!.value = InfoKeyValue.text.toString()
                                        scoutCardInfo!!.save(database)


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
                                                    -1,
                                                    yearId!!.serverId,
                                                    eventId!!.blueAllianceId,
                                                    matchId!!.key,
                                                    teamId!!.id,
                                                    "",
                                                    "",
                                                    infoKey.serverId,
                                                    true
                                            )
                                        }

                                        scoutCardInfo!!.value = if (checked) "1" else "0"
                                        scoutCardInfo!!.save(database)


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
        })
        layoutCreationThread.start()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_info, container, false)

        Thread(Runnable{
            layoutCreationThread.join()

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

        loadingThread.join()
        isLoading = true

        //update the title of the page to display the matchId
        activityContext.setToolbarTitle(matchId!!.matchType.toString(matchId!!))

        return super.onCreateView(view)
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param match to get scout cards from
         * @param team to get scout cards from
         * @return A new instance of fragment [ScoutCardInfoFragment].
         */
        fun newInstance(match: Match, team: Team): ScoutCardInfoFragment
        {
            val fragment = ScoutCardInfoFragment()
            val args = Bundle()
            args.putString(ARG_MATCH_ID, toJson(match))
            args.putString(ARG_TEAM_ID, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

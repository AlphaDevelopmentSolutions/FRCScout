package com.alphadevelopmentsolutions.frcscout.fragment

import android.graphics.drawable.RippleDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alphadevelopmentsolutions.frcscout.classes.table.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.table.Team
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

            val scoutCardInfoKeys = ScoutCardInfoKey.getObjects(year, null, database)
            val scoutCardInfos = ScoutCardInfo.getObjects(event, match, team, null, null, false, database)

            for(scoutCardInfoKey in scoutCardInfoKeys!!)
            {
                if(scoutCardInfoKeyStates[scoutCardInfoKey.keyState] != null)
                    scoutCardInfoKeyStates[scoutCardInfoKey.keyState]!!.add(scoutCardInfoKey)

                else
                    scoutCardInfoKeyStates[scoutCardInfoKey.keyState] = arrayListOf(scoutCardInfoKey)
            }


            for((infoKeyName, infoKeyValueArray) in scoutCardInfoKeyStates)
            {
                val view = context.layoutInflater.inflate(R.layout.layout_card_scout_card_info_form, null)
                view.ScoutCardInfoFormCardTitle.text = infoKeyName

                for(infoKey in infoKeyValueArray)
                {
                    var blockTextChange = false

                    val fieldLinearLayout = LinearLayout(context)
                    fieldLinearLayout.orientation = LinearLayout.VERTICAL
                    fieldLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    with(context.layoutInflater.inflate(R.layout.layout_field_info, null))
                    {

                        var scoutCardInfo: ScoutCardInfo? = null

                        for (info in scoutCardInfos)
                        {
                            if (info.propertyKeyId == infoKey.serverId && scoutCardInfo == null)
                                scoutCardInfo = info
                        }

                        if (scoutCardInfo?.isDraft == true)
                            DeleteButton.visibility = View.VISIBLE

                        InfoKeyTitle.text = infoKey.keyName

                        DeleteButton.imageTintList = this@ScoutCardInfoFragment.context.buttonBackground

                        //set the delete onclick
                        DeleteButton.setOnClickListener {
                            scoutCardInfo?.delete(database)
                            scoutCardInfo = null

                            //get the recent items from the db to replace the deleted one
                            with(ScoutCardInfo.getObjects(event, match, team, infoKey, null, false, database))
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
                                            TextEditText.setText(scoutCardInfo?.propertyValue)
                                        }

                                        ScoutCardInfoKey.DataTypes.INT ->
                                        {
                                            InfoKeyValue.text = scoutCardInfo?.propertyValue
                                        }

                                        ScoutCardInfoKey.DataTypes.BOOL ->
                                        {
                                            BooleanCheckBox.isChecked = scoutCardInfo?.propertyValue == "1"
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
                                            InfoKeyValue.text = infoKey.minValue.toString()
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
                            ScoutCardInfoKey.DataTypes.TEXT ->
                            {
                                TextLinearLayout.visibility = View.VISIBLE
                                TextEditText.setText(scoutCardInfo?.propertyValue)
                                TextEditText.backgroundTintList = this@ScoutCardInfoFragment.context.editTextBackground
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
                                                        year!!.serverId,
                                                        event!!.blueAllianceId,
                                                        match!!.key,
                                                        team!!.id,
                                                        "",
                                                        "",
                                                        infoKey.serverId,
                                                        true
                                                )
                                            }

                                            scoutCardInfo!!.propertyValue = searchText.toString()
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
                            ScoutCardInfoKey.DataTypes.INT ->
                            {
                                IntegerLinearLayout.visibility = View.VISIBLE

                                InfoKeyValue.text = if (scoutCardInfo?.propertyValue?.isNotBlank() == true) scoutCardInfo?.propertyValue ?: infoKey.minValue?.toString() ?: "0" else infoKey.minValue?.toString() ?: "0"

                                PlusButton.backgroundTintList = this@ScoutCardInfoFragment.context.buttonBackground
                                (PlusButton as MaterialButton).rippleColor = this@ScoutCardInfoFragment.context.buttonRipple

                                //Add button click handlers
                                PlusButton.setOnClickListener {

                                    val curr = Integer.parseInt(InfoKeyValue.text.toString())

                                    //Check for maximum in key
                                    if (infoKey.maxValue == null || curr < infoKey.maxValue!!)
                                    {
                                        InfoKeyValue.text = (curr + 1).toString()

                                        //if the current robot info isn't a draft, create a new robot info item to save
                                        if (scoutCardInfo?.isDraft != true)
                                        {
                                            scoutCardInfo = ScoutCardInfo(
                                                    -1,
                                                    year!!.serverId,
                                                    event!!.blueAllianceId,
                                                    match!!.key,
                                                    team!!.id,
                                                    "",
                                                    "",
                                                    infoKey.serverId,
                                                    true
                                            )
                                        }

                                        scoutCardInfo!!.propertyValue = InfoKeyValue.text.toString()
                                        scoutCardInfo!!.save(database)


                                        with(DeleteButton)
                                        {
                                            if (visibility != View.VISIBLE)
                                                visibility = View.VISIBLE
                                        }

                                    }
                                    else
                                        this@ScoutCardInfoFragment.context.showSnackbar("This field has a maximum of " + infoKey.maxValue)

                                }

                                MinusButton.backgroundTintList = this@ScoutCardInfoFragment.context.buttonBackground
                                (MinusButton as MaterialButton).rippleColor = this@ScoutCardInfoFragment.context.buttonRipple

                                MinusButton.setOnClickListener {

                                    val curr = Integer.parseInt(InfoKeyValue.text.toString())

                                    if (infoKey.minValue == null || curr > infoKey.minValue!!)
                                    {
                                        InfoKeyValue.text = (curr - 1).toString()

                                        //if the current robot info isn't a draft, create a new robot info item to save
                                        if (scoutCardInfo?.isDraft != true)
                                        {
                                            scoutCardInfo = ScoutCardInfo(
                                                    -1,
                                                    year!!.serverId,
                                                    event!!.blueAllianceId,
                                                    match!!.key,
                                                    team!!.id,
                                                    "",
                                                    "",
                                                    infoKey.serverId,
                                                    true
                                            )
                                        }

                                        scoutCardInfo!!.propertyValue = InfoKeyValue.text.toString()
                                        scoutCardInfo!!.save(database)


                                        with(DeleteButton)
                                        {
                                            if (visibility != View.VISIBLE)
                                                visibility = View.VISIBLE
                                        }
                                    }
                                    else
                                        this@ScoutCardInfoFragment.context.showSnackbar("This field has a minimum of " + infoKey.minValue)
                                }

                            }

                            //create a bool layout
                            ScoutCardInfoKey.DataTypes.BOOL ->
                            {
                                BooleanLinearLayout.visibility = View.VISIBLE
                                BooleanCheckBox.buttonTintList = this@ScoutCardInfoFragment.context.checkboxBackground
                                (BooleanCheckBox.background as RippleDrawable).setColor(this@ScoutCardInfoFragment.context.checkboxRipple)
                                BooleanCheckBox.isChecked = if (scoutCardInfo?.propertyValue?.isNotBlank() == true) scoutCardInfo?.propertyValue == "1" else false
                                BooleanCheckBox.setOnCheckedChangeListener { _, checked ->
                                    run {

                                        //if the current robot info isn't a draft, create a new robot info item to save
                                        if (scoutCardInfo?.isDraft != true)
                                        {
                                            scoutCardInfo = ScoutCardInfo(
                                                    -1,
                                                    year!!.serverId,
                                                    event!!.blueAllianceId,
                                                    match!!.key,
                                                    team!!.id,
                                                    "",
                                                    "",
                                                    infoKey.serverId,
                                                    true
                                            )
                                        }

                                        scoutCardInfo!!.propertyValue = if (checked) "1" else "0"
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

            context.runOnUiThread{

                //add all the dynamic form frags to the viewpager
                for(layoutField in layoutFields)
                    view.ScoutCardInfoFormListLinearLayout.addView(layoutField)

                isLoading = false
            }

        }).start()

        //gets rid of the shadow on the actionbar
        context.dropActionBar()
        context.isToolbarScrollable = false
        context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })

        loadingThread.join()
        isLoading = true

        //update the title of the page to display the match
        context.setToolbarTitle(match!!.matchType.toString(match!!))

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
            args.putString(ARG_MATCH_JSON, toJson(match))
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

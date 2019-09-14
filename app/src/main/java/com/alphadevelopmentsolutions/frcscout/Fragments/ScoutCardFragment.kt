package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_scout_card.view.*
import kotlinx.android.synthetic.main.layout_card_scout_card_info_form.view.*
import kotlinx.android.synthetic.main.layout_field_scout_card_info.view.*

class ScoutCardFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var fragCreationThread: Thread? = null

    private var layoutFields: ArrayList<View> = ArrayList()
    private var scoutCardInfoKeyStates: LinkedHashMap<String, ArrayList<ScoutCardInfoKey>> = LinkedHashMap()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //start the creation of fragments on a new thread
        fragCreationThread = Thread(Runnable {
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
                    val fieldLinearLayout = LinearLayout(context)
                    fieldLinearLayout.orientation = LinearLayout.VERTICAL
                    fieldLinearLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                    val infoKeyView = context.layoutInflater.inflate(R.layout.layout_field_scout_card_info, null)

                    var scoutCardInfo = ScoutCardInfo(
                            -1,
                            year!!.serverId!!,
                            event!!.blueAllianceId!!,
                            match!!.key,
                            team!!.id!!,
                            "",
                            "",
                            infoKey.serverId,
                            true
                    )

                    for(info in scoutCardInfos)
                    {
                        if(info.propertyKeyId == infoKey.serverId)
                            scoutCardInfo = info
                    }

                    infoKeyView.InfoKeyTitle.text = infoKey.keyName

                    when(infoKey.dataType)
                    {
                        ScoutCardInfoKey.DataTypes.TEXT ->
                        {
                            infoKeyView.TextLinearLayout.visibility = View.VISIBLE

                            infoKeyView.TextEditText.setText(scoutCardInfo.propertyValue)

                            if (!scoutCardInfo.isDraft)
                            {
                                infoKeyView.TextEditText.isEnabled = false
                            }
                            else
                            {
                                infoKeyView.TextEditText.addTextChangedListener(object : TextWatcher
                                {
                                    override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
                                    {

                                    }

                                    override fun onTextChanged(searchText: CharSequence, start: Int, before: Int, count: Int)
                                    {
                                        scoutCardInfo.propertyValue = searchText.toString()
                                        scoutCardInfo.save(database)
                                    }

                                    override fun afterTextChanged(editable: Editable)
                                    {

                                    }
                                })
                            }
                        }

                        //create an int layout
                        ScoutCardInfoKey.DataTypes.INT ->
                        {
                            infoKeyView.IntegerLinearLayout.visibility = View.VISIBLE

                            infoKeyView.InfoKeyValue.text = if(scoutCardInfo.propertyValue.isNotBlank()) scoutCardInfo.propertyValue else infoKey.minValue?.toString() ?: "0"

                            if (!scoutCardInfo.isDraft)
                            {
                                infoKeyView.PlusButton.visibility = View.GONE
                                infoKeyView.MinusButton.visibility = View.GONE
                            }
                            else
                            {
                                //Add button click handlers
                                infoKeyView.PlusButton.setOnClickListener{

                                    val curr = Integer.parseInt(infoKeyView.InfoKeyValue.text.toString())

                                    //Check for maximum in key
                                    if(infoKey.maxValue == null || curr < infoKey.maxValue!!)
                                    {
                                        infoKeyView.InfoKeyValue.text = (curr + 1).toString()

                                        scoutCardInfo.propertyValue = infoKeyView.InfoKeyValue.text.toString()
                                        scoutCardInfo.save(database)
                                    }
                                    else
                                        context.showSnackbar("This field has a maximum of " + infoKey.maxValue)

                                }

                                infoKeyView.MinusButton.setOnClickListener{

                                    val curr = Integer.parseInt(infoKeyView.InfoKeyValue.text.toString())

                                    if(infoKey.minValue == null  || curr > infoKey.minValue!!)
                                    {
                                        infoKeyView.InfoKeyValue.text = (curr - 1).toString()

                                        scoutCardInfo.propertyValue = infoKeyView.InfoKeyValue.text.toString()
                                        scoutCardInfo.save(database)
                                    }
                                    else
                                        context.showSnackbar("This field has a minimum of " + infoKey.minValue)
                                }
                            }
                        }

                        //create a bool layout
                        ScoutCardInfoKey.DataTypes.BOOL ->
                        {
                            infoKeyView.BooleanLinearLayout.visibility = View.VISIBLE

                            infoKeyView.BooleanCheckBox.isChecked = if(scoutCardInfo.propertyValue.isNotBlank()) scoutCardInfo.propertyValue == "1" else false

                            if (!scoutCardInfo.isDraft)
                            {
                                infoKeyView.BooleanCheckBox.isEnabled = false
                            }
                            else
                            {
                                infoKeyView.BooleanCheckBox.setOnCheckedChangeListener { _, checked ->
                                    run {

                                        scoutCardInfo.propertyValue = if (checked) "1" else "0"
                                        scoutCardInfo.save(database)
                                    }
                                }
                            }
                        }
                    }

                    fieldLinearLayout.addView(infoKeyView)

                    view.ScoutCardInfoFormFieldsLinearLayout.addView(fieldLinearLayout)
                }

                layoutFields.add(view)
            }
        })
        fragCreationThread!!.start()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card, container, false)
        view.z = zIndex

        //gets rid of the shadow on the actionbar
        context.dropActionBar()
        context.isToolbarScrollable = false
        context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })




        fragCreationThread!!.join()

        //add all the dynamic form frags to the viewpager
        for(layoutField in layoutFields)
            view.ScoutCardInfoFormListLinearLayout.addView(layoutField)


        //update the title of the page to display the match
        context.setToolbarTitle(match!!.matchType.toString(match!!))

        return view
    }

    override fun onDestroyView()
    {
        context.unlockDrawerLayout()
        super.onDestroyView()
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param match to get scout cards from
         * @param team to get scout cards from
         * @return A new instance of fragment [ScoutCardFragment].
         */
        fun newInstance(match: Match, team: Team): ScoutCardFragment
        {
            val fragment = ScoutCardFragment()
            val args = Bundle()
            args.putString(ARG_MATCH_JSON, toJson(match))
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

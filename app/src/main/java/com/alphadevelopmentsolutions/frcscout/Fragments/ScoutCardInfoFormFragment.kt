package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.TextInputLayout
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R

class ScoutCardInfoFormFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    private var scoutCardInfoKeyState: String? = null

    private lateinit var getDataThread: Thread
    private var scoutCardInfoKeys: ArrayList<ScoutCardInfoKey>? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        arguments?.let {
            scoutCardInfoKeyState = it.getString(ARG_SCOUT_CARD_INFO_KEY_STATE)
        }

        getDataThread = Thread(Runnable
        {
            loadingThread.join()

            scoutCardInfoKeys = ScoutCardInfoKey.getObjects(year, null, database)

            val iterator = scoutCardInfoKeys!!.iterator()

            while(iterator.hasNext())
            {
                val scoutCardInfoKey = iterator.next()

                if (scoutCardInfoKey.keyState != scoutCardInfoKeyState)
                    iterator.remove()

            }

        })
        getDataThread.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_info_form, container, false)

        var scoutCardInfoLinearLayout: LinearLayout = view.findViewById(R.id.ScoutCardInfoLinearLayout)

        val populateFormThread = Thread(Runnable
        {
            getDataThread.join()

            var linearLayout = LinearLayout(context)

            var currentInfoKeyState: String? = ""

            val scale = resources.displayMetrics.density

            var j = 1

            for (i in scoutCardInfoKeys!!.indices)
            {
                val scoutCardInfoKey = scoutCardInfoKeys!![i]
                val nextScoutCardInfoKey = if (i + 1 < scoutCardInfoKeys!!.size) scoutCardInfoKeys!![i + 1] else null

                val scoutCardInfos = ScoutCardInfo.getObjects(event, match, team, scoutCardInfoKey, null, false, database)
                val scoutCardInfo = if (scoutCardInfos!!.size > 0) scoutCardInfos[scoutCardInfos.size - 1] else
                    ScoutCardInfo(
                            -1,
                            year!!.serverId!!,
                            event!!.blueAllianceId!!,
                            match!!.key,
                            team!!.id!!,
                            "",
                            "",
                            scoutCardInfoKey.serverId,
                            true
                            )


                if (currentInfoKeyState != scoutCardInfoKey.keyState)
                {
                    if (i > 0)
                    {
                        val padding = (1 * scale + 0.5f).toInt()
                        val divider = LinearLayout(context)
                        divider.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, padding)
                        divider.setBackgroundColor(ContextCompat.getColor(context, R.color.divider))

                        context.runOnUiThread { scoutCardInfoLinearLayout.addView(divider) }
                    }

                    currentInfoKeyState = scoutCardInfoKey.keyState

                }

                when(scoutCardInfoKey.dataType)
                {
                    ScoutCardInfoKey.DataTypes.TEXT ->
                    {
                        val textInputLayout = TextInputLayout(context)
                        val editText = EditText(context)

                        editText.hint = scoutCardInfoKey.keyName
                        editText.setText(scoutCardInfo.propertyValue)

                        if (!scoutCardInfo.isDraft)
                        {
                            editText.isEnabled = false
                        }
                        else
                        {
                            editText.addTextChangedListener(object : TextWatcher
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

                        textInputLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)

                        textInputLayout.addView(editText)

                        linearLayout.addView(textInputLayout)
                    }

                    //create an int layout
                    ScoutCardInfoKey.DataTypes.INT ->
                    {
                        //create and set all buttons and margins
                        val titleTextView = TextView(context)
                        val valTextView = TextView(context)
                        val plusButton = MaterialButton(context)
                        val minusButton = MaterialButton(context)
                        val itemLayout = LinearLayout(context)
                        val valueLayout = LinearLayout(context, null, R.style.Widget_MaterialComponents_Button)
                        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f)
                        params.setMargins(8, 8, 8, 8)

                        //Set margins and styling
                        valTextView.textSize = resources.getDimension(R.dimen.text_card_value)
                        valTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER

                        plusButton.layoutParams = params
                        plusButton.backgroundTintList = ColorStateList.valueOf(context.primaryColorDark)
                        minusButton.layoutParams = params
                        minusButton.backgroundTintList = ColorStateList.valueOf(context.primaryColorDark)

                        itemLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
                        itemLayout.orientation = LinearLayout.VERTICAL

                        valueLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
                        valueLayout.orientation = LinearLayout.HORIZONTAL

                        //Set default text
                        titleTextView.text = scoutCardInfoKey.keyName
                        plusButton.text = getString(R.string.plus_one)
                        minusButton.text = getString(R.string.minus_one)
                        valTextView.text = if (scoutCardInfo.propertyValue == "") scoutCardInfoKey.minValue.toString() else scoutCardInfo.propertyValue

                        if (!scoutCardInfo.isDraft)
                        {
                            plusButton.visibility = View.GONE
                            minusButton.visibility = View.GONE
                        }
                        else
                        {
                            //Add button click handlers
                            plusButton.setOnClickListener{

                                val curr = Integer.parseInt(valTextView.text.toString())

                                if(scoutCardInfoKey.maxValue == null || curr < scoutCardInfoKey.maxValue!!)
                                {
                                    valTextView.text = (curr + 1).toString()

                                    scoutCardInfo.propertyValue = valTextView.text.toString()
                                    scoutCardInfo.save(database)
                                }
                                else
                                    context.showSnackbar("This field has a maximum of " + scoutCardInfoKey.maxValue)

                            }

                            minusButton.setOnClickListener{

                                val curr = Integer.parseInt(valTextView.text.toString())

                                if(scoutCardInfoKey.minValue == null  || curr > scoutCardInfoKey.minValue!!)
                                {
                                    valTextView.text = (curr - 1).toString()

                                    scoutCardInfo.propertyValue = valTextView.text.toString()
                                    scoutCardInfo.save(database)
                                }
                                else
                                    context.showSnackbar("This field has a minimum of " + scoutCardInfoKey.minValue)
                            }
                        }

                        //add views to layout
                        valueLayout.addView(minusButton)
                        valueLayout.addView(plusButton)

                        itemLayout.addView(titleTextView)
                        itemLayout.addView(valTextView)
                        itemLayout.addView(valueLayout)
                        itemLayout.setPadding(8, 8, 8, 8)

                        linearLayout.addView(itemLayout)
                    }

                    //create a bool layout
                    ScoutCardInfoKey.DataTypes.BOOL ->
                    {
                        //create and set all buttons and margins
                        val titleTextView = TextView(context)
                        val valTextView = TextView(context)
                        val yesButton = MaterialButton(context)
                        val noButton = MaterialButton(context)
                        val itemLayout = LinearLayout(context)
                        val valueLayout = LinearLayout(context, null, R.style.Widget_MaterialComponents_Button)
                        val params = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f)
                        params.setMargins(8, 8, 8, 8)

                        //Set margins and styling
                        valTextView.textSize = resources.getDimension(R.dimen.text_card_value)
                        valTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER

                        yesButton.layoutParams = params
                        yesButton.backgroundTintList = ColorStateList.valueOf(context.primaryColorDark)
                        noButton.layoutParams = params
                        noButton.backgroundTintList = ColorStateList.valueOf(context.primaryColorDark)

                        itemLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
                        itemLayout.orientation = LinearLayout.VERTICAL

                        valueLayout.layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f)
                        valueLayout.orientation = LinearLayout.HORIZONTAL

                        //Set default text
                        titleTextView.text = scoutCardInfoKey.keyName
                        yesButton.text = getString(R.string.yes)
                        noButton.text = getString(R.string.no)
                        valTextView.text = if (scoutCardInfo.propertyValue == "1") getString(R.string.yes) else getString(R.string.no)

                        if (!scoutCardInfo.isDraft)
                        {
                            yesButton.visibility = View.GONE
                            noButton.visibility = View.GONE
                        }
                        else
                        {
                            //Add button click handlers
                            yesButton.setOnClickListener {
                                valTextView.text = getString(R.string.yes)

                                scoutCardInfo.propertyValue = "1"
                                scoutCardInfo.save(database)
                            }

                            noButton.setOnClickListener {
                                valTextView.text = getString(R.string.no)

                                scoutCardInfo.propertyValue = "0"
                                scoutCardInfo.save(database)
                            }
                        }

                        //add views to layout
                        valueLayout.addView(noButton)
                        valueLayout.addView(yesButton)

                        itemLayout.addView(titleTextView)
                        itemLayout.addView(valTextView)
                        itemLayout.addView(valueLayout)
                        itemLayout.setPadding(8, 8, 8, 8)

                        linearLayout.addView(itemLayout)
                    }
                }

                if (j % 2 == 0 && currentInfoKeyState == scoutCardInfoKey.keyState || nextScoutCardInfoKey == null || currentInfoKeyState != nextScoutCardInfoKey.keyState)
                {
                    j = 0
                    val padding = (8 * scale + 0.5f).toInt()
                    linearLayout.setPadding(padding, padding, padding, padding)

                    val finalLinearLayout = linearLayout
                    context.runOnUiThread { scoutCardInfoLinearLayout.addView(finalLinearLayout) }

                    linearLayout = LinearLayout(context)
                }

                j++
            }
        })
        populateFormThread.start()

        return view
    }

    companion object
    {
        private const val ARG_SCOUT_CARD_INFO_KEY_STATE = "INFO_KEY_STATE"

        /**
         * Creates a new instance
         * @param scoutCardInfoKeyState state to get scout card infos from
         * @param match to save scout card infos to
         * @param team to save scout card infos to
         * @return A new instance of fragment [ScoutCardInfoFormFragment].
         */
        fun newInstance(scoutCardInfoKeyState: String, match: Match?, team: Team?) =
                ScoutCardInfoFormFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_SCOUT_CARD_INFO_KEY_STATE, scoutCardInfoKeyState)
                        putString(ARG_MATCH_JSON, toJson(match))
                        putString(ARG_TEAM_JSON, toJson(team))
                    }
                }
    }
}

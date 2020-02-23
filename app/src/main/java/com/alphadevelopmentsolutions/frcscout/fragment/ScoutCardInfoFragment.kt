package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardScoutCardInfoFormBinding
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutFieldInfoBinding
import com.alphadevelopmentsolutions.frcscout.enums.DataType
import com.alphadevelopmentsolutions.frcscout.extension.init
import com.alphadevelopmentsolutions.frcscout.extension.putUUID
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import kotlinx.android.synthetic.main.fragment_scout_card_info.view.*
import java.util.*
import kotlin.collections.ArrayList

class ScoutCardInfoFragment : MasterFragment()
{
    private var layoutFields: ArrayList<View> = ArrayList()
//    private var scoutCardInfoKeyStates: LinkedHashMap<String, ArrayList<ScoutCardInfoKey>> = LinkedHashMap()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_info, container, false)

        val d = VMProvider.getInstance(activityContext).scoutCardInfoViewModel.objsViewForTeam(teamId!!, matchId!!).init()
                .subscribe(
                        { scoutCardInfoViewList ->

                            var previousScoutCardInfoKey: ScoutCardInfoKey? = null
                            var groupLayout = DataBindingUtil.inflate<LayoutCardScoutCardInfoFormBinding>(inflater, R.layout.layout_card_scout_card_info_form, null, false)

                            scoutCardInfoViewList.forEach { scoutCardInfoView ->

                                // Add the group layout to the view if the state has changed, also reset the group view
                                previousScoutCardInfoKey?.let {
                                    if(scoutCardInfoView.scoutCardInfoKey.state != it.state)
                                        view.ScoutCardInfoFormListLinearLayout.addView(groupLayout.root)

                                    groupLayout = DataBindingUtil.inflate(inflater, R.layout.layout_card_scout_card_info_form, null, false)
                                }

                                previousScoutCardInfoKey = scoutCardInfoView.scoutCardInfoKey

                                val fieldView = DataBindingUtil.inflate<LayoutFieldInfoBinding>(inflater, R.layout.layout_field_info, null, false)

                                val scoutCardInfo =
                                        scoutCardInfoView.scoutCardInfo.let { scoutCardInfoList ->
                                            if(scoutCardInfoList.isNotEmpty())
                                                scoutCardInfoList[scoutCardInfoList.size - 1]
                                            else
                                                ScoutCardInfo(
                                                        matchId!!,
                                                        teamId!!,
                                                        Table.DEFAULT_UUID,
                                                        "",
                                                        scoutCardInfoView.scoutCardInfoKey.id
                                                )

                                        }

                                fieldView.scoutCardInfoKey = scoutCardInfoView.scoutCardInfoKey
                                fieldView.scoutCardInfo = scoutCardInfo

                                groupLayout.ScoutCardInfoFormFieldsLinearLayout.addView(groupLayout.root)
                            }

                            isLoading = false
                        },
                        {
                            AppLog.error(it)
                        }
                )

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

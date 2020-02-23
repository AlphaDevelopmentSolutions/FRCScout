package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alphadevelopmentsolutions.frcscout.adapter.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.adapter.TeamListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.extension.init
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_match.view.*
import kotlinx.android.synthetic.main.fragment_team_list.view.*
import java.util.*

class MatchFragment : MasterFragment()
{
    override fun onBackPressed() = false


    private var allianceColor: AllianceColor? = null

    private lateinit var teamListAdapter: TeamListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //check if any args were passed, specifically for teamId and matchId json
        arguments?.let {

            it.getString(ARG_ALLIANCE_COLOR)?.let { allianceColorString ->
                if (matchId != null && allianceColorString.isNotEmpty())
                    allianceColor = AllianceColor.getColorFromString(allianceColorString)
            }
        }
    }

    private lateinit var flowable: Flowable<List<Team>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_list, container, false)

        matchId?.let {

            //gets rid of the shadow on the actionbar
            activityContext.dropActionBar()

            activityContext.setToolbarTitle("Match Overview")
            activityContext.isSearchViewVisible = false
            activityContext.isToolbarScrollable = false

            view.AllianceTabLayout.setBackgroundColor(activityContext.primaryColor)

            val viewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

            viewPagerAdapter.addFragment(TeamListFragment.newInstance(matchId, AllianceColor.BLUE), activityContext.getString(R.string.blue_alliance))
            viewPagerAdapter.addFragment(TeamListFragment.newInstance(matchId, AllianceColor.RED), activityContext.getString(R.string.red_alliance))

            view.AllianceViewPager.adapter = viewPagerAdapter
            view.AllianceTabLayout.setupWithViewPager(view.AllianceViewPager)
            view.AllianceTabLayout.setSelectedTabIndicatorColor(activityContext.primaryColorDark)

            activityContext.lockDrawerLayout(true, View.OnClickListener { activityContext.onBackPressed() })
        }

        return onCreateView(view)
    }

    fun setData() {

    }

    override fun onPause()
    {
        super.onPause()
        if(activityContext.isSearchViewVisible)
            activityContext.isSearchViewVisible = false
    }

    override fun onResume()
    {
        super.onResume()
        if(!activityContext.isSearchViewVisible && matchId == null)
            activityContext.isSearchViewVisible = true

        teamListAdapter.notifyItemChanged(0)
    }

    override fun onDestroyView()
    {
        if(activityContext.isSearchViewVisible)
            activityContext.isSearchViewVisible = false

        super.onDestroyView()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        activityContext.setSearchViewOnTextChangeListener(null)
    }

    companion object
    {
        internal const val ARG_ALLIANCE_COLOR = "ALLIANCE_COLOR"

        /**
         * Creates a new instance
         * @param match to get teams from
         * @param allianceColor to get teams from
         * @return A new instance of fragment [MatchFragment].
         */
        fun newInstance(match: Match?): MatchFragment
        {
            val fragment = MatchFragment()
            val args = Bundle()
            args.putString(ARG_MATCH_ID, toJson(match))
            fragment.arguments = args
            return fragment
        }
    }
}

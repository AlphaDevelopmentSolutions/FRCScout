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
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_team_list.view.*
import java.util.*

class TeamListFragment : MasterFragment()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_list, container, false)

        //gets rid of the shadow on the actionbar
        activityContext.dropActionBar()

        activityContext.setToolbarTitle(if (matchId == null) activityContext.getString(R.string.teams) else matchId.toString())
        activityContext.isSearchViewVisible = matchId == null
        activityContext.isToolbarScrollable = matchId == null

        view.AllianceTabLayout.setBackgroundColor(activityContext.primaryColor)

        if (matchId == null || allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED)
        {
            //GET TEAMS AT EVENT ON ALLIANCE
            val disposable = VMProvider(this).teamViewModel.objs
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { teams ->

                                var previousSearchLength = 0

                                //if a matchId and alliance color was specified,
                                //remove any teams that are not in that matchId or alliance color
                                val searchedTeams = if (matchId != null && allianceColor != null)
                                {
                                    for (team in ArrayList(teams!!))
                                    {
                                        //teamId not in matchId / alliance color
                                        if (matchId!!.getTeamAllianceColor(team) != allianceColor)
                                            teams!!.remove(team)
                                    }

                                    //add all the teams to the searchedTeams arraylist
                                    ArrayList(teams!!)
                                } else
                                //add all the teams to the searchedTeams arraylist
                                    ArrayList(teams!!)

                                teamListAdapter = TeamListRecyclerViewAdapter(matchId, searchedTeams!!, activityContext)
                            },
                            {
                                AppLog.error(it)
                            }
                    )

            Thread(Runnable{

                val layoutManager = LinearLayoutManager(activityContext)

                activityContext.runOnUiThread{

                    view.TeamsRecyclerView.layoutManager = layoutManager
                    view.TeamsRecyclerView.adapter = teamListAdapter

                    isLoading = false
                }

            }).start()

            if(matchId == null)
            {
                activityContext.setSearchViewOnTextChangeListener(object: SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(p0: String?): Boolean
                    {
                        return false
                    }

                    override fun onQueryTextChange(searchText: String?): Boolean
                    {
                        val searchLength = searchText?.length ?: 0

                        //You only need to reset the list if you are removing from your search, adding the objects back
                        if (searchLength < previousSearchLength)
                        {
                            //Reset the list
                            for (i in teams!!.indices)
                            {
                                val team = teams!![i]

                                //check if the contact doesn't exist in the viewable list
                                if (!searchedTeams!!.contains(team))
                                {
                                    //add it and notify the recyclerview
                                    searchedTeams!!.add(i, team)
                                    teamListAdapter.notifyItemInserted(i)
                                    teamListAdapter.notifyItemRangeChanged(i, searchedTeams!!.size)
                                }

                            }
                        }

                        //Delete from the list
                        var i = 0
                        while (i < searchedTeams!!.size)
                        {
                            val team = searchedTeams!![i]
                            val name = team.id.toString() + " - " + team.name

                            //If the contacts name doesn't equal the searched name
                            if (!name.toLowerCase().contains(searchText.toString().toLowerCase()))
                            {
                                //remove it from the list and notify the recyclerview
                                searchedTeams!!.removeAt(i)
                                teamListAdapter.notifyItemRemoved(i)
                                teamListAdapter.notifyItemRangeChanged(i, searchedTeams!!.size)

                                //this prevents the index from passing the size of the list,
                                //stays on the same index until you NEED to move to the next one
                                i--
                            }
                            i++
                        }

                        previousSearchLength = searchLength

                        return false
                    }

                })
            }

        }

        else
        {
            //GET TEAMS IN MATCH
            val disposable = VMProvider(this).teamViewModel.objs
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            { teams ->

                                var previousSearchLength = 0

                                //if a matchId and alliance color was specified,
                                //remove any teams that are not in that matchId or alliance color
                                val searchedTeams = if (matchId != null && allianceColor != null)
                                {
                                    for (team in ArrayList(teams!!))
                                    {
                                        //teamId not in matchId / alliance color
                                        if (matchId!!.getTeamAllianceColor(team) != allianceColor)
                                            teams!!.remove(team)
                                    }

                                    //add all the teams to the searchedTeams arraylist
                                    ArrayList(teams!!)
                                } else
                                //add all the teams to the searchedTeams arraylist
                                    ArrayList(teams!!)

                                teamListAdapter = TeamListRecyclerViewAdapter(matchId, searchedTeams!!, activityContext)
                            },
                            {
                                AppLog.error(it)
                            }
                    )

            activityContext.setToolbarTitle(matchId.toString())
            view.AllianceViewPagerLinearLayout.visibility = View.VISIBLE
            view.TeamsRecyclerView.visibility = View.GONE

            val viewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

            val blueAllianceFrag = newInstance(matchId, AllianceColor.BLUE)
            val redAllianceFrag = newInstance(matchId, AllianceColor.RED)

            viewPagerAdapter.addFragment(blueAllianceFrag, activityContext.getString(R.string.blue_alliance))
            viewPagerAdapter.addFragment(redAllianceFrag, activityContext.getString(R.string.red_alliance))

            view.AllianceViewPager.adapter = viewPagerAdapter
            view.AllianceTabLayout.setupWithViewPager(view.AllianceViewPager)
            view.AllianceTabLayout.setSelectedTabIndicatorColor(activityContext.primaryColorDark)

            activityContext.lockDrawerLayout(true, View.OnClickListener { activityContext.onBackPressed() })

        }//if matchId specified, setup the viewpager and hide the recyclerview

        return super.onCreateView(view)
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
         * @return A new instance of fragment [TeamListFragment].
         */
        fun newInstance(match: Match?, allianceColor: AllianceColor?): TeamListFragment
        {
            val fragment = TeamListFragment()
            val args = Bundle()
            args.putString(ARG_MATCH_ID, toJson(match))
            args.putString(ARG_ALLIANCE_COLOR, allianceColor?.name ?: AllianceColor.NONE.name)
            fragment.arguments = args
            return fragment
        }
    }
}

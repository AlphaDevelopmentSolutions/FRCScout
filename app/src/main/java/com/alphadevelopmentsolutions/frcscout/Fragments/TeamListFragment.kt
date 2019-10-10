package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Adapters.TeamListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_team_list.view.*
import java.util.*

class TeamListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var allianceColorString: String? = null

    private var teams: ArrayList<Team>? = null
    private var searchedTeams: ArrayList<Team>? = null

    private var loadTeamsThread: Thread? = null

    private var allianceColor: AllianceColor? = null

    private var previousSearchLength: Int = 0

    private lateinit var teamListAdapter: TeamListRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //check if any args were passed, specifically for team and match json
        if (arguments != null)
        {
            matchJson = arguments!!.getString(ARG_MATCH_JSON)
            allianceColorString = arguments!!.getString(ARG_ALLIANCE_COLOR)
        }

        loadTeamsThread = Thread(Runnable {
            loadingThread.join()

            if (match != null && allianceColorString != null && allianceColorString != "")
                allianceColor = AllianceColor.getColorFromString(allianceColorString!!)

            //get all teams at event
            teams = event!!.getTeams(null, null, database)

            //if a match and alliance color was specified,
            //remove any teams that are not in that match or alliance color
            searchedTeams = if (match != null && allianceColor != null)
            {
                for (team in ArrayList(teams!!))
                {
                    //team not in match / alliance color
                    if (match!!.getTeamAllianceColor(team) != allianceColor)
                        teams!!.remove(team)
                }

                //add all the teams to the searchedTeams arraylist
                ArrayList(teams!!)
            } else
                //add all the teams to the searchedTeams arraylist
                ArrayList(teams!!)

            teamListAdapter = TeamListRecyclerViewAdapter(match, searchedTeams!!, context)
        })

        loadTeamsThread!!.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_team_list, container, false)

        //gets rid of the shadow on the actionbar
        context.dropActionBar()

        loadTeamsThread!!.join()

        context.setToolbarTitle(if (match == null) context.getString(R.string.teams) else match.toString())
        context.isSearchViewVisible = match == null
        context.isToolbarScrollable = match == null

        view.AllianceTabLayout.setBackgroundColor(context.primaryColor)

        if (match == null || allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED)
        {
            Thread(Runnable{

                val layoutManager = LinearLayoutManager(context)

                context.runOnUiThread{

                    view.TeamsRecyclerView.layoutManager = layoutManager
                    view.TeamsRecyclerView.adapter = teamListAdapter

                    isLoading = false
                }

            }).start()

            if(match == null)
            {
                context.setSearchViewOnTextChangeListener(object: SearchView.OnQueryTextListener{
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
            context.setToolbarTitle(match.toString())
            view.AllianceViewPagerLinearLayout.visibility = View.VISIBLE
            view.TeamsRecyclerView.visibility = View.GONE

            val viewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

            val blueAllianceFrag = newInstance(match, AllianceColor.BLUE)
            val redAllianceFrag = newInstance(match, AllianceColor.RED)

            viewPagerAdapter.addFragment(blueAllianceFrag, context.getString(R.string.blue_alliance))
            viewPagerAdapter.addFragment(redAllianceFrag, context.getString(R.string.red_alliance))

            view.AllianceViewPager.adapter = viewPagerAdapter
            view.AllianceTabLayout.setupWithViewPager(view.AllianceViewPager)
            view.AllianceTabLayout.setSelectedTabIndicatorColor(context.primaryColorDark)

            context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })

        }//if match specified, setup the viewpager and hide the recyclerview

        return super.onCreateView(view, !(match == null || allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED))
    }

    override fun onPause()
    {
        super.onPause()
        if(context.isSearchViewVisible)
            context.isSearchViewVisible = false
    }

    override fun onResume()
    {
        super.onResume()
        if(!context.isSearchViewVisible && match == null)
            context.isSearchViewVisible = true

        teamListAdapter.notifyItemChanged(0)
    }

    override fun onDestroyView()
    {
        if (match != null)
            context.unlockDrawerLayout()

        if(context.isSearchViewVisible)
            context.isSearchViewVisible = false

        super.onDestroyView()
    }

    override fun onDestroy()
    {
        super.onDestroy()
        context.setSearchViewOnTextChangeListener(null)
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
            args.putString(ARG_MATCH_JSON, toJson(match))
            args.putString(ARG_ALLIANCE_COLOR, allianceColor?.name ?: AllianceColor.NONE.name)
            fragment.arguments = args
            return fragment
        }
    }
}

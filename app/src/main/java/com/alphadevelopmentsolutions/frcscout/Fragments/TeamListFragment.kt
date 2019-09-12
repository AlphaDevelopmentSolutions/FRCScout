package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Adapters.TeamListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

class TeamListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var allianceColorString: String? = null
    
    private var teamsRecyclerView: RecyclerView? = null

    private var teams: ArrayList<Team>? = null
    private var searchedTeams: ArrayList<Team>? = null

    private var loadTeamsThread: Thread? = null

    private var allianceColor: AllianceColor? = null

    private var allianceViewPagerLinearLayout: LinearLayout? = null
    private var allianceTabLayout: TabLayout? = null
    private var allianceViewPager: ViewPager? = null

    private var previousSearchLength: Int = 0

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

        teamsRecyclerView = view.findViewById(R.id.TeamsRecyclerView)
        allianceViewPagerLinearLayout = view.findViewById(R.id.AllianceViewPagerLinearLayout)
        allianceTabLayout = view.findViewById(R.id.AllianceTabLayout)
        allianceViewPager = view.findViewById(R.id.AllianceViewPager)

        allianceTabLayout!!.setBackgroundColor(context.primaryColor)

        //join back up with the load thread
        loadTeamsThread!!.join()

        context.setToolbarTitle(if (match == null) event!!.name!! else match.toString())
        context.isSearchViewVisible = match == null
        context.isToolbarScrollable = match == null


        if (match == null || allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED)
        {
            val teamListRecyclerViewAdapter = TeamListRecyclerViewAdapter(match, searchedTeams!!, context)

            teamsRecyclerView!!.layoutManager = LinearLayoutManager(context)
            teamsRecyclerView!!.adapter = teamListRecyclerViewAdapter

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
                                    teamListRecyclerViewAdapter.notifyItemInserted(i)
                                    teamListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedTeams!!.size)
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
                                teamListRecyclerViewAdapter.notifyItemRemoved(i)
                                teamListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedTeams!!.size)

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

        } else
        {
            view.z = zIndex
            context.setToolbarTitle(match.toString())
            allianceViewPagerLinearLayout!!.visibility = View.VISIBLE
            teamsRecyclerView!!.visibility = View.GONE

            val viewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

            viewPagerAdapter.addFragment(newInstance(match, AllianceColor.BLUE), getString(R.string.blue_alliance))
            viewPagerAdapter.addFragment(newInstance(match, AllianceColor.RED), getString(R.string.red_alliance))

            allianceViewPager!!.adapter = viewPagerAdapter
            allianceTabLayout!!.setupWithViewPager(allianceViewPager)
            allianceTabLayout!!.setSelectedTabIndicatorColor(context.primaryColorDark)

            context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })
        }//if match specified, setup the viewpager and hide the recyclerview

        return view
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

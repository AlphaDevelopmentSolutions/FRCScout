package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout

import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Adapters.TeamListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.R

import java.util.ArrayList

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [TeamListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [TeamListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TeamListFragment : MasterFragment()
{

    private var allianceColorString: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var teamsRecyclerView: RecyclerView? = null

    private var teamSearchEditText: EditText? = null

    private var teams: ArrayList<Team>? = null
    private var searchedTeams: ArrayList<Team>? = null

    private var loadTeamsThread: Thread? = null

    private var allianceColor: AllianceColor? = null

    private var allianceViewPagerLinearLayout: LinearLayout? = null
    private var allianceTabLayout: TabLayout? = null
    private var allianceViewPager: ViewPager? = null

    private var searchTeamsToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //check if any args were passed, specifically for team and match json
        if (arguments != null)
        {
            matchJson = arguments!!.getString(MasterFragment.ARG_MATCH_JSON)
            allianceColorString = arguments!!.getString(ARG_ALLIANCE_COLOR)
        }

        loadTeamsThread = Thread(Runnable {
            joinLoadingThread()

            if (match != null && allianceColorString != null && allianceColorString != "")
                allianceColor = AllianceColor.getColorFromString(allianceColorString!!)

            //get all teams at event
            teams = event!!.getTeams(null, null, database)

            //if a match and alliance color was specified,
            //remove any teams that are not in that match or alliance color
            if (match != null && allianceColor != null)
            {
                for (team in ArrayList(teams!!))
                {
                    //team not in match / alliance color
                    if (match!!.getTeamAllianceColor(team) != allianceColor)
                        teams!!.remove(team)
                }

                //add all the teams to the searchedTeams arraylist
                searchedTeams = ArrayList(teams!!)
            } else
            //add all the teams to the searchedTeams arraylist
                searchedTeams = ArrayList(teams!!)
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
        teamSearchEditText = view.findViewById(R.id.TeamSearchEditText)
        allianceViewPagerLinearLayout = view.findViewById(R.id.AllianceViewPagerLinearLayout)
        allianceTabLayout = view.findViewById(R.id.AllianceTabLayout)
        allianceViewPager = view.findViewById(R.id.AllianceViewPager)
        searchTeamsToolbar = view.findViewById(R.id.SearchTeamsToolbar)


        //join back up with the load teams thread
        try
        {
            loadTeamsThread!!.join()
        } catch (e: InterruptedException)
        {
            e.printStackTrace()
        }

        context.title = if (match == null) event!!.name else match.toString()

        //hide search bar if match is specified
        if (match != null)
            searchTeamsToolbar!!.visibility = View.GONE

        if (match == null || allianceColor == AllianceColor.BLUE || allianceColor == AllianceColor.RED)
        {
            val teamListRecyclerViewAdapter = TeamListRecyclerViewAdapter(match, searchedTeams!!, context)

            teamsRecyclerView!!.layoutManager = LinearLayoutManager(context)
            teamsRecyclerView!!.adapter = teamListRecyclerViewAdapter

            teamSearchEditText!!.addTextChangedListener(object : TextWatcher
            {
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int)
                {

                }

                override fun onTextChanged(searchText: CharSequence, start: Int, before: Int, count: Int)
                {
                    //You only need to reset the list if you are removing from your search, adding the objects back
                    if (count < before)
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
                }

                override fun afterTextChanged(editable: Editable)
                {

                }
            })
        } else
        {
            allianceViewPagerLinearLayout!!.visibility = View.VISIBLE
            teamsRecyclerView!!.visibility = View.GONE

            val viewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

            viewPagerAdapter.addFragment(TeamListFragment.newInstance(match, AllianceColor.BLUE), getString(R.string.blue_alliance))
            viewPagerAdapter.addFragment(TeamListFragment.newInstance(match, AllianceColor.RED), getString(R.string.red_alliance))

            allianceViewPager!!.adapter = viewPagerAdapter
            allianceTabLayout!!.setupWithViewPager(allianceViewPager)
        }//if match specified, setup the viewpager and hide the recyclerview

        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri)
    {
        if (mListener != null)
        {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            mListener = context
        } else
        {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object
    {
        internal val ARG_ALLIANCE_COLOR = "ALLIANCE_COLOR"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment TeamListFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(match: Match?, allianceColor: AllianceColor?): TeamListFragment
        {
            val fragment = TeamListFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_MATCH_JSON, MasterFragment.toJson(match))
            args.putString(ARG_ALLIANCE_COLOR, allianceColor?.name ?: AllianceColor.NONE.name)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

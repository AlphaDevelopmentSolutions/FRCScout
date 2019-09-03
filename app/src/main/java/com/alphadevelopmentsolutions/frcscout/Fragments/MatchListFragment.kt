package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.MatchListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R

class MatchListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var matchListRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_match_list, container, false)

        matchListRecyclerView = view.findViewById(R.id.MatchListRecyclerView)

        loadingThread.join()

        if (team == null)
            context.setTitle(event.toString())

        val scoutCardsRecyclerViewAdapter = MatchListRecyclerViewAdapter(event!!, team, context, if (team == null) TeamListFragment::class.java else ScoutCardFragment::class.java)
        matchListRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        matchListRecyclerView!!.adapter = scoutCardsRecyclerViewAdapter

        return view
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param team to show matches for
         * @return A new instance of fragment [MatchListFragment].
         */
        fun newInstance(team: Team?): MatchListFragment
        {
            val fragment = MatchListFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

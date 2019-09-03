package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.ScoutCardsRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R

class ScoutCardListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    private var scoutCardListRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card_list, container, false)

        scoutCardListRecyclerView = view.findViewById(R.id.ScoutCardListRecyclerView)

        loadingThread.join()

        val scoutCardsRecyclerViewAdapter = ScoutCardsRecyclerViewAdapter(team!!, team!!.getScoutCards(event, null, null, false, database)!!, context)
        scoutCardListRecyclerView!!.layoutManager = LinearLayoutManager(activity)
        scoutCardListRecyclerView!!.adapter = scoutCardsRecyclerViewAdapter

        return view
    }
    
    companion object
    {

        /**
         * Creates a new instance
         * @param team to get scout card list from
         * @return A new instance of fragment [ScoutCardListFragment].
         */
        fun newInstance(team: Team): ScoutCardListFragment
        {
            val fragment = ScoutCardListFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_TEAM_JSON, MasterFragment.toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

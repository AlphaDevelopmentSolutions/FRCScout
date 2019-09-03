package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.ChecklistItemListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Adapters.MatchListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R

class ChecklistFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view: View

        val recyclerView: RecyclerView

        loadingThread.join()

        //no match selected, show match list
        if (match == null)
        {
            view = inflater.inflate(R.layout.fragment_match_list, container, false)

            recyclerView = view.findViewById(R.id.MatchListRecyclerView)

            val matchListRecyclerViewAdapter = MatchListRecyclerViewAdapter(event!!, team, context, this.javaClass)
            recyclerView.adapter = matchListRecyclerViewAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            context.setTitle(event!!.name)
        }
        else
        {
            context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })
            view = inflater.inflate(R.layout.fragment_checklist, container, false)

            recyclerView = view.findViewById(R.id.ChecklistItemsRecyclerView)

            val checklistItemListRecyclerViewAdapter = ChecklistItemListRecyclerViewAdapter(match!!, ChecklistItem.getObjects(null, database)!!, context)
            recyclerView.adapter = checklistItemListRecyclerViewAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            context.setTitle(match.toString())
        }//match and event selected, show the checklist


        return view
    }


    override fun onDetach()
    {
        if (match != null)
            context.unlockDrawerLayout()

        super.onDetach()
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param team to be shown checklist items for
         * @param match to be shown checklist items for
         * @return A new instance of fragment [ChecklistFragment].
         */
        fun newInstance(team: Team, match: Match?): ChecklistFragment
        {
            val fragment = ChecklistFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_JSON, toJson(team))
            args.putString(ARG_MATCH_JSON, toJson(match))
            fragment.arguments = args
            return fragment
        }
    }
}
package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.adapter.ChecklistItemListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.adapter.MatchListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.classes.table.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.classes.table.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.Team
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.R
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_match_list.*
import kotlin.collections.ArrayList

class ChecklistFragment : MasterFragment()
{
    /**
     * @see MasterFragment.onBackPressed
     */
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

        context.isToolbarScrollable = true

        //no matchId selected, show match list
        if (matchId == null)
        {
            view = inflater.inflate(R.layout.fragment_match_list, container, false)

            recyclerView = MatchListRecyclerView

            VMProvider(this).matchViewModel.objWithCustom(eventId, null, teamId)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(
                            {
                                val searchedMatches = ArrayList(it)
                                var previousSearchLength = 0

                                val matchListRecyclerViewAdapter = MatchListRecyclerViewAdapter(eventId!!, teamId, searchedMatches, context, this.javaClass)
                                recyclerView.adapter = matchListRecyclerViewAdapter
                                recyclerView.layoutManager = LinearLayoutManager(context)

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
                                            for (i in it.indices)
                                            {
                                                val match = it[i]

                                                //check if the contact doesn't exist in the viewable list
                                                if (!searchedMatches.contains(match))
                                                {
                                                    //add it and notify the recyclerview
                                                    searchedMatches.add(i, match)
                                                    matchListRecyclerViewAdapter.notifyItemInserted(i)
                                                    matchListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedMatches.size)
                                                }
                                            }
                                        }

                                        //Delete from the list
                                        var i = 0
                                        while (i < searchedMatches.size)
                                        {
                                            val match = searchedMatches[i]
                                            val name = match.toString()

                                            //If the contacts name doesn't equal the searched name
                                            if (!name.toLowerCase().contains(searchText.toString().toLowerCase()))
                                            {
                                                //remove it from the list and notify the recyclerview
                                                searchedMatches.removeAt(i)
                                                matchListRecyclerViewAdapter.notifyItemRemoved(i)
                                                matchListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedMatches.size)

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
                            },
                            {
                                Log.e("Err", "SUBSCRIBE MATCHES ERR")
                            }
                    ) ?: Log.e("Err", "NULL MATCHES ERR")


            context.setToolbarTitle(context.getString(R.string.checklist))
            context.isToolbarScrollable = true
            context.isSearchViewVisible = true
        }
        else
        {

            context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })
            view = inflater.inflate(R.layout.fragment_checklist, container, false)
            recyclerView = view.findViewById(R.id.ChecklistItemsRecyclerView)

            val checklistItemListRecyclerViewAdapter = ChecklistItemListRecyclerViewAdapter(matchId!!, ChecklistItem.getObjects(null, database)!!, context)
            recyclerView.adapter = checklistItemListRecyclerViewAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            context.setToolbarTitle(matchId.toString())
            context.isToolbarScrollable = false
            context.isSearchViewVisible = false
        }//matchId and eventId selected, show the checklist


        return view
    }

    override fun onDestroyView()
    {
        if (matchId != null)
            context.unlockDrawerLayout()
        
        super.onDestroyView()
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
            args.putString(ARG_TEAM_ID, toJson(team))
            args.putString(ARG_MATCH_ID, toJson(match))
            fragment.arguments = args
            return fragment
        }
    }
}
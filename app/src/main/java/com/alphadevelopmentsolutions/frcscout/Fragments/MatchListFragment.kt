package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.MatchListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R
import kotlinx.android.synthetic.main.fragment_match_list.view.*
import java.util.*

class MatchListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        loadMatchesThread = Thread(Runnable {
            loadingThread.join()

            matches = Match.getObjects(event, null, team, database)
            searchedMatches = ArrayList(matches)

            matchListRecyclerViewAdapter = MatchListRecyclerViewAdapter(event!!, team, searchedMatches, context, if (team == null) TeamListFragment::class.java else ScoutCardInfoFragment::class.java)

        })

        loadMatchesThread.start()
    }

    private lateinit var matches: ArrayList<Match>
    private lateinit var searchedMatches: ArrayList<Match>

    private lateinit var loadMatchesThread: Thread

    private var previousSearchLength: Int = 0

    private lateinit var matchListRecyclerViewAdapter: MatchListRecyclerViewAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_match_list, container, false)

        Thread(Runnable
        {
            loadMatchesThread.join()

            val layoutManager = LinearLayoutManager(activity)

            context.runOnUiThread {
                view.MatchListRecyclerView.layoutManager = layoutManager
                view.MatchListRecyclerView.adapter = matchListRecyclerViewAdapter


                if (team == null)
                {
                    context.setToolbarTitle(event.toString())
                    context.isToolbarScrollable = true
                    context.isSearchViewVisible = true

                    context.setSearchViewOnTextChangeListener(object : SearchView.OnQueryTextListener
                    {
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
                                for (i in matches.indices)
                                {
                                    val match = matches[i]

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
                } else
                    context.isToolbarScrollable = false

                isLoading = false
            }

        }).start()

        isLoading = true

        return super.onCreateView(view, false)
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

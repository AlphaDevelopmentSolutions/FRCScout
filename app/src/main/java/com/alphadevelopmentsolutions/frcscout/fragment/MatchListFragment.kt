package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alphadevelopmentsolutions.frcscout.adapter.MatchListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.classes.table.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.Team
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants.TableNames.Companion.EVENT
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_match_list.view.*
import java.util.*

class MatchListFragment : MasterFragment()
{
    override fun onBackPressed() = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_match_list, container, false)

        val layoutManager = LinearLayoutManager(activity)

        view.MatchListRecyclerView.layoutManager = layoutManager

        val disposable = VMProvider(this).matchViewModel.objWithCustom(eventId, null, teamId)
                ?.subscribeOn(Schedulers.io())
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe(
                        { matches ->

                            if (teamId == null)
                            {
                                context.setToolbarTitle(context.getString(R.string.matches))
                                context.isToolbarScrollable = true
                                context.isSearchViewVisible = true

                                val searchedMatches = ArrayList(matches)
                                var previousSearchLength = 0

                                val matchListRecyclerViewAdapter = MatchListRecyclerViewAdapter(
                                        EVENT GOES HERE,
                                        TEAM GOES HERE,
                                        searchedMatches,
                                        context,
                                        if (teamId == null) TeamListFragment::class.java else ScoutCardInfoFragment::class.java
                                )

                                view.MatchListRecyclerView.adapter = matchListRecyclerViewAdapter

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
                        },
                        {
                            AppLog.error(it)
                        }
                )

        isLoading = true

        return onCreateView(view)
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
        if(teamId == null && !context.isSearchViewVisible)
            context.isSearchViewVisible = true

        if(teamId == null)
            context.unlockDrawerLayout()
    }

    override fun onDestroyView()
    {
        if(context.isSearchViewVisible)
            context.isSearchViewVisible = false

        super.onDestroyView()
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
            args.putString(ARG_TEAM_ID, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

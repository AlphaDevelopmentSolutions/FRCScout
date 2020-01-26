package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.adapter.EventListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.classes.table.Event
import com.alphadevelopmentsolutions.frcscout.classes.table.Team
import com.alphadevelopmentsolutions.frcscout.classes.table.Year
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.KeyStore
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.extension.getUUIDOrNull
import com.alphadevelopmentsolutions.frcscout.extension.putUUID
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_event_list.view.*
import java.util.*


class EventListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        context.changeFragment(YearListFragment.newInstance(), false, true, true)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //check if any args were passed, specifically for teamId and matchId json
        arguments?.let {
            yearId = it.getUUIDOrNull(ARG_YEAR_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)

        context.lockDrawerLayout(true, View.OnClickListener { context.changeFragment(YearListFragment.newInstance(), false, true, true) })
        context.isToolbarScrollable = true

        //showing this view means the user has not selected an eventId, clear the shared pref
        KeyStore.getInstance(context).setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, "")

        val eventListRecyclerView = view.EventListRecyclerView

        //GET EVENTS FOR YEAR AND TEAM
        val disposable = VMProvider(this).eventViewModel.objs
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { events ->
                            context.setToolbarTitle("TITLE") //TODO: YEAR TITLE

                            val searchedEvents = ArrayList(events)

                            var previousSearchLength = 0

                            val eventListRecyclerViewAdapter = EventListRecyclerViewAdapter(searchedEvents, context)
                            eventListRecyclerView.adapter = eventListRecyclerViewAdapter
                            eventListRecyclerView.layoutManager = LinearLayoutManager(context)

                            context.isToolbarScrollable = true
                            context.isSearchViewVisible = true

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
                                        for (i in events.indices)
                                        {
                                            val event = events[i]

                                            //check if the contact doesn't exist in the viewable list
                                            if (!searchedEvents.contains(event))
                                            {
                                                //add it and notify the recyclerview
                                                searchedEvents.add(i, event)
                                                eventListRecyclerViewAdapter.notifyItemInserted(i)
                                                eventListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedEvents.size)
                                            }
                                        }
                                    }

                                    //Delete from the list
                                    var i = 0
                                    while (i < searchedEvents.size)
                                    {
                                        val event = searchedEvents[i]
                                        val name = event.toString()

                                        //If the contacts name doesn't equal the searched name
                                        if (!name.toLowerCase().contains(searchText.toString().toLowerCase()))
                                        {
                                            //remove it from the list and notify the recyclerview
                                            searchedEvents.removeAt(i)
                                            eventListRecyclerViewAdapter.notifyItemRemoved(i)
                                            eventListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedEvents.size)

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
                            AppLog.error(it)
                        }
                )

        return view
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        context.unlockDrawerLayout()
    }
    
    companion object
    {

        private const val ARG_YEAR_ID = "YEAR_ID"

        /**
         * Creates a new instance
         * @param year to grab events from
         * @return A new instance of fragment [EventListFragment].
         */
        fun newInstance(year: Year) = EventListFragment().apply {
            arguments = Bundle().apply {
                putUUID(ARG_YEAR_ID, year.id)
            }
        }
    }
}

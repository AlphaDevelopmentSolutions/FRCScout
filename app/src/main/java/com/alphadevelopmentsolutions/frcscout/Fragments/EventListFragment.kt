package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.EventListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.gson.Gson
import java.util.*


class EventListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        context.changeFragment(YearListFragment.newInstance(), false, true, true)
        return true
    }

    private var yearJson: String? = null

    private var eventListRecyclerView: RecyclerView? = null

    private lateinit var loadYearThread: Thread

    private lateinit var events: ArrayList<Event>
    private lateinit var searchedEvents: ArrayList<Event>

    private var previousSearchLength: Int = 0

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //check if any args were passed, specifically for team and match json
        if (arguments != null)
        {
            yearJson = arguments!!.getString(ARG_YEAR_JSON)
        }

        //create and start the thread to load the json vars
        loadYearThread = Thread(Runnable {
            loadingThread.join()

            //load the scout card from json, if available
            if (yearJson != null && yearJson != "")
                year = Gson().fromJson(yearJson, Year::class.java)

            events = Event.getObjects(year, null, Team(context.keyStore.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1) as Int, database), database)
            searchedEvents = ArrayList(events)
        })

        loadYearThread.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)
        view.z = zIndex + 4

        context.lockDrawerLayout(true, View.OnClickListener { context.changeFragment(YearListFragment.newInstance(), false, true, true) })
        context.isToolbarScrollable = true

        loadYearThread.join()

        context.setToolbarTitle(year!!.toString())

        //showing this view means the user has not selected an event, clear the shared pref
        context.keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1)

        eventListRecyclerView = view.findViewById(R.id.EventListRecyclerView)

        val eventListRecyclerViewAdapter = EventListRecyclerViewAdapter(searchedEvents, context)
        eventListRecyclerView!!.adapter = eventListRecyclerViewAdapter
        eventListRecyclerView!!.layoutManager = LinearLayoutManager(context)

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

        return view
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        context.unlockDrawerLayout()
    }
    
    companion object
    {

        private const val ARG_YEAR_JSON = "YEAR_JSON"

        /**
         * Creates a new instance
         * @param year to grab events from
         * @return A new instance of fragment [EventListFragment].
         */
        fun newInstance(year: Year): EventListFragment
        {
            val fragment = EventListFragment()
            val args = Bundle()
            args.putString(ARG_YEAR_JSON, toJson(year))
            fragment.arguments = args
            return fragment
        }
    }
}

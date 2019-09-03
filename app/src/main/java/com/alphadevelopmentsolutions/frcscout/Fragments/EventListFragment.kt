package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

class EventListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        context.changeFragment(YearListFragment.newInstance(), false)
        return true
    }

    private var yearJson: String? = null

    private var eventListRecyclerView: RecyclerView? = null

    private var loadYearThread: Thread? = null

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
            //load the scout card from json, if available
            if (yearJson != null && yearJson != "")
                year = Gson().fromJson(yearJson, Year::class.java)
        })

        loadYearThread!!.start()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)
        context.lockDrawerLayout(true, View.OnClickListener { context.changeFragment(YearListFragment.newInstance(), false) })

        loadingThread.join()

        //join back up with the load year thread
        try
        {
            loadYearThread!!.join()
        } catch (e: InterruptedException)
        {
            e.printStackTrace()
        }

        context.setTitle(year!!.toString())

        //showing this view means the user has not selected an event, clear the shared pref
        context.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1)

        eventListRecyclerView = view.findViewById(R.id.EventListRecyclerView)

        val eventListRecyclerViewAdapter = EventListRecyclerViewAdapter(Event.getObjects(year, null, Team(context.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1) as Int, database), database)!!, context)
        eventListRecyclerView!!.adapter = eventListRecyclerViewAdapter
        eventListRecyclerView!!.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onStop()
    {
        context.unlockDrawerLayout()
        super.onStop()
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

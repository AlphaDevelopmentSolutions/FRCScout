package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
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
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [EventListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [EventListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EventListFragment : MasterFragment()
{
    private var mListener: OnFragmentInteractionListener? = null

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

        joinLoadingThread()

        //join back up with the load year thread
        try
        {
            loadYearThread!!.join()
        } catch (e: InterruptedException)
        {
            e.printStackTrace()
        }

        context.setTitle(year!!.toString())
        context.setChangeButtonOnClickListener(View.OnClickListener{
            context.changeFragment(YearListFragment.newInstance(), false)
        }, getString(R.string.change_year), false)


        //showing this view means the user has not selected an event, clear the shared pref
        context.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1)

        eventListRecyclerView = view.findViewById(R.id.EventListRecyclerView)

        val eventListRecyclerViewAdapter = EventListRecyclerViewAdapter(Event.getObjects(year, null, Team(context.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1) as Int, database), database)!!, context)
        eventListRecyclerView!!.adapter = eventListRecyclerViewAdapter
        eventListRecyclerView!!.layoutManager = LinearLayoutManager(context)

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
        context.setChangeButtonOnClickListener(View.OnClickListener{
            val year = Year(context.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int, database)

            //send to eventlist frag
            context.changeFragment(EventListFragment.newInstance(year), false)
        }, getString(R.string.change_event), true)
    }

    override fun onStop()
    {
        context.unlockDrawerLayout()
        super.onStop()
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

        private val ARG_YEAR_JSON = "YEAR_JSON"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @param year to grab events from
         * @return A new instance of fragment EventListFragment.
         */
        fun newInstance(year: Year): EventListFragment
        {
            val fragment = EventListFragment()
            val args = Bundle()
            args.putString(ARG_YEAR_JSON, MasterFragment.toJson(year))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

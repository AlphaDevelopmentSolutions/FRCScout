package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.google.gson.Gson
import java.util.*

abstract class MasterFragment : Fragment()
{
    //store the context and database in the master fragment which all other fragments extend from
    protected lateinit var context: MainActivity
    protected lateinit var database: Database

    protected var year: Year? = null

    protected var event: Event? = null

    private var teamJson: String? = null
    protected var matchJson: String? = null
    private var scoutCardJson: String? = null
    private var pitCardJson: String? = null

    protected var team: Team? = null
    protected var match: Match? = null
    protected var scoutCardInfo: ScoutCardInfo? = null

    private lateinit var gson: Gson

    protected lateinit var loadingThread: Thread

    private var mListener: OnFragmentInteractionListener? = null

    protected var zIndex: Float = -1f

    override fun onAttach(context: Context?)
    {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
        {
            mListener = context
        }
        else
        {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach()
    {
        super.onDetach()
        mListener = null
        context.currentZIndex--
        context.currentZIndex--
    }

    interface OnFragmentInteractionListener
    {
        fun onFragmentInteraction(uri: Uri)
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //assign context and database vars
        context = activity as MainActivity
        database = context.database
        gson = Gson()
        context.currentZIndex++
        context.currentZIndex++

        zIndex = context.currentZIndex.toFloat()

        //check if any args were passed, specifically for team and match json
        if (arguments != null)
        {
            teamJson = arguments!!.getString(ARG_TEAM_JSON)
            matchJson = arguments!!.getString(ARG_MATCH_JSON)
            scoutCardJson = arguments!!.getString(ARG_PARAM_SCOUT_CARD_JSON)
            pitCardJson = arguments!!.getString(ARG_PARAM_PIT_CARD_JSON)
        }

        //create and start the thread to load the json vars
        loadingThread = Thread(Runnable {

            year = Year(context.keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) as Int, database)

            val eventId = context.keyStore.getPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1) as Int
            if (eventId > 0)
                event = Event(eventId, database)

            //load the team from json, if available
            if (teamJson != null && teamJson != "")
                team = gson.fromJson(teamJson, Team::class.java)

            //load the match from json, if available
            if (matchJson != null && matchJson != "")
                match = gson.fromJson(matchJson, Match::class.java)

            //load the scout card from json, if available
            if (scoutCardJson != null && scoutCardJson != "")
                scoutCardInfo = gson.fromJson(scoutCardJson, ScoutCardInfo::class.java)
        })

        loadingThread.start()
    }

    /**
     * Used to override the activities back pressed event
     * @return [Boolean] true to override activities back press event
     */
    abstract fun onBackPressed() : Boolean

    companion object
    {
        @JvmStatic
        protected val ARG_TEAM_JSON = "TEAM_JSON"

        @JvmStatic
        protected val ARG_MATCH_JSON = "MATCH_JSON"

        @JvmStatic
        protected val ARG_PARAM_SCOUT_CARD_JSON = "SCOUT_CARD_JSON"

        @JvmStatic
        protected val ARG_PARAM_PIT_CARD_JSON = "PIT_CARD_JSON"

        @JvmStatic
        protected var staticGson: Gson? = null

        /**
         * Converts fields to json regardless if they are null or not
         * @param object to convert to json
         * @return null | string json object
         */
        @JvmStatic
        protected fun toJson(`object`: Any?): String?
        {
            if (staticGson == null)
                staticGson = Gson()

            return if (`object` == null)
                null
            else
                staticGson!!.toJson(`object`)
        }
    }
}

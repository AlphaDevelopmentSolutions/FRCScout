package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v4.app.Fragment

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.gson.Gson

open class MasterFragment : Fragment()
{
    //store the context and database in the master fragment which all other fragmets extend from
    protected lateinit var context: MainActivity
    protected lateinit var database: Database

    protected var event: Event? = null

    protected var teamJson: String? = null
    protected var matchJson: String? = null
    protected var scoutCardJson: String? = null
    protected var pitCardJson: String? = null

    protected var team: Team? = null
    protected var match: Match? = null
    protected var scoutCard: ScoutCard? = null

    protected lateinit var gson: Gson

    protected lateinit var loadingThread: Thread

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //assign context and database vars
        context = activity as MainActivity
        database = context.getDatabase()
        gson = Gson()

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
            //check if the event id is set properly, and load the event
            val eventId = context.getPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1) as Int
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
                scoutCard = Gson().fromJson(scoutCardJson, ScoutCard::class.java)
        })

        loadingThread.start()
    }

    override fun onDetach()
    {
        //reset the title of the app upon detach
        context.supportActionBar!!.setTitle(R.string.app_name)
        super.onDetach()
    }

    /**
     * Joins back up with the loading thread
     */
    protected fun joinLoadingThread()
    {
        //join back up with the loading thread
        try
        {
            loadingThread.join()
        } catch (e: InterruptedException)
        {
            e.printStackTrace()
        }

    }

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

package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*
import kotlin.collections.HashMap

class ScoutCardFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }
    
    private var scoutCardTabLayout: TabLayout? = null
    private var scoutCardViewPager: ViewPager? = null

    private var fragCreationThread: Thread? = null

    private var scoutCardInfoFormFragments: ArrayList<HashMap<String, ScoutCardInfoFormFragment>> = ArrayList()
    private var scoutCardInfoKeyStates: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        //start the creation of fragments on a new thread
        fragCreationThread = Thread(Runnable {
            loadingThread.join()

            var scoutCardInfoKeys = ScoutCardInfoKey.getObjects(Year(event!!.yearId, database), null, database)

            for(scoutCardInfoKey in scoutCardInfoKeys!!)
            {
                if(!scoutCardInfoKeyStates.contains(scoutCardInfoKey.keyState))
                    scoutCardInfoKeyStates.add(scoutCardInfoKey.keyState)
            }

            var tempMap : HashMap<String, ScoutCardInfoFormFragment>

            for(name in scoutCardInfoKeyStates)
            {
                tempMap = HashMap()
                tempMap[name] = ScoutCardInfoFormFragment.newInstance(name, match, team)

                scoutCardInfoFormFragments.add(tempMap)
            }
        })
        fragCreationThread!!.start()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_scout_card, container, false)

        //gets rid of the shadow on the actionbar
        context.dropActionBar()

        scoutCardTabLayout = view.findViewById(R.id.ScoutCardTabLayout)
        scoutCardViewPager = view.findViewById(R.id.ScoutCardViewPager)

        scoutCardTabLayout!!.setBackgroundColor(context.primaryColor)
        scoutCardTabLayout!!.setSelectedTabIndicatorColor(context.primaryColorDark)

        val scoutCardViewPagerAdapter = FragmentViewPagerAdapter(childFragmentManager)

        //join back with the frag creation thread
        try
        {
            fragCreationThread!!.join()
        } catch (e: InterruptedException)
        {
            e.printStackTrace()
        }

        //add all the dynamic form frags to the viewpager
        for(map in scoutCardInfoFormFragments)
        {
            for((key, value) in map)
                scoutCardViewPagerAdapter.addFragment(value, key)
        }

        //update the title of the page to display the match
        context.setTitle(match!!.matchType.toString(match!!))


        scoutCardViewPager!!.adapter = scoutCardViewPagerAdapter
        scoutCardViewPager!!.offscreenPageLimit = 5
        scoutCardTabLayout!!.setupWithViewPager(scoutCardViewPager)

        return view
    }

    companion object
    {

        /**
         * Creates a new instance
         * @param match to get scout cards from
         * @param scoutCardInfo to display on the fragment
         * @param team to get scout cards from
         * @return A new instance of fragment [ScoutCardFragment].
         */
        fun newInstance(match: Match, scoutCardInfo: ScoutCardInfo?, team: Team): ScoutCardFragment
        {
            val fragment = ScoutCardFragment()
            val args = Bundle()
            args.putString(ARG_MATCH_JSON, toJson(match))
            args.putString(ARG_PARAM_SCOUT_CARD_JSON, toJson(scoutCardInfo))
            args.putString(ARG_TEAM_JSON, toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}

package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*
import kotlin.collections.HashMap

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ScoutCardFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ScoutCardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ScoutCardFragment : MasterFragment()
{
    private var mListener: OnFragmentInteractionListener? = null

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
            joinLoadingThread()

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

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param match
         * @param scoutCard
         * @param team
         * @return A new instance of fragment ScoutCardFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(match: Match, scoutCardInfo: ScoutCardInfo?, team: Team): ScoutCardFragment
        {
            val fragment = ScoutCardFragment()
            val args = Bundle()
            args.putString(MasterFragment.ARG_MATCH_JSON, MasterFragment.toJson(match))
            args.putString(MasterFragment.ARG_PARAM_SCOUT_CARD_JSON, MasterFragment.toJson(scoutCardInfo))
            args.putString(MasterFragment.ARG_TEAM_JSON, MasterFragment.toJson(team))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

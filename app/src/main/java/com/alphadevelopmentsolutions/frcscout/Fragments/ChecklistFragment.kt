package com.alphadevelopmentsolutions.frcscout.Fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.alphadevelopmentsolutions.frcscout.Adapters.ChecklistItemListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Adapters.MatchListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ChecklistFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ChecklistFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChecklistFragment : MasterFragment()
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private var mListener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view: View

        val recyclerView: RecyclerView

        joinLoadingThread()

        //no match selected, show match list
        if (match == null)
        {
            view = inflater.inflate(R.layout.fragment_match_list, container, false)

            recyclerView = view.findViewById(R.id.MatchListRecyclerView)

            val matchListRecyclerViewAdapter = MatchListRecyclerViewAdapter(event!!, team, context, this.javaClass)
            recyclerView.adapter = matchListRecyclerViewAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            context.setTitle(event!!.name)
        } else
        {
            context.lockDrawerLayout(true, View.OnClickListener { context.onBackPressed() })
            view = inflater.inflate(R.layout.fragment_checklist, container, false)

            recyclerView = view.findViewById(R.id.ChecklistItemsRecyclerView)

            val checklistItemListRecyclerViewAdapter = ChecklistItemListRecyclerViewAdapter(match!!, ChecklistItem.getObjects(null, database)!!, context)
            recyclerView.adapter = checklistItemListRecyclerViewAdapter
            recyclerView.layoutManager = LinearLayoutManager(context)

            context.setTitle(match.toString())
        }//match and event selected, show the checklist


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
        if(match != null)
            context.unlockDrawerLayout()

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
         * @param team
         * @param match
         * @return A new instance of fragment ChecklistFragment.
         */
        fun newInstance(team: Team, match: Match?): ChecklistFragment
        {
            val fragment = ChecklistFragment()
            val args = Bundle()
            args.putString(ARG_TEAM_JSON, toJson(team))
            args.putString(ARG_MATCH_JSON, toJson(match))
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

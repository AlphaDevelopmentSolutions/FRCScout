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

import com.alphadevelopmentsolutions.frcscout.Adapters.YearListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R

import java.util.Calendar

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [YearListFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [YearListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YearListFragment : MasterFragment()
{

    // TODO: Rename and change types of parameters
    private val mParam1: String? = null
    private val mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null

    private var yearListRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_year_list, container, false)

        context.setTitle(R.string.years)
        context.lockDrawerLayout()

        joinLoadingThread()

        //showing this view means the user has not selected an event or year, clear the shared pref
        context.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1)
        context.setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) //default to current calendar year

        yearListRecyclerView = view.findViewById(R.id.YearListRecyclerView)

        val yearListRecyclerViewAdapter = YearListRecyclerViewAdapter(Year.getYears(null, database)!!, context)
        yearListRecyclerView!!.adapter = yearListRecyclerViewAdapter
        yearListRecyclerView!!.layoutManager = LinearLayoutManager(context)


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
        context.unlockDrawerLayout()
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
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment YearListFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): YearListFragment
        {
            val fragment = YearListFragment()
            val args = Bundle()

            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

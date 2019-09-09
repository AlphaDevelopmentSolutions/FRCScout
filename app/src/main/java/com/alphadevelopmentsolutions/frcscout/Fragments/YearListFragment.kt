package com.alphadevelopmentsolutions.frcscout.Fragments

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alphadevelopmentsolutions.frcscout.Adapters.YearListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

class YearListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    private var yearListRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_year_list, container, false)

        context.setToolbarTitle(R.string.years)
        context.lockDrawerLayout()

        loadingThread.join()

        //showing this view means the user has not selected an event or year, clear the shared pref
        context.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1)
        context.setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) //default to current calendar year

        yearListRecyclerView = view.findViewById(R.id.YearListRecyclerView)

        val yearListRecyclerViewAdapter = YearListRecyclerViewAdapter(Year.getObjects(null, database)!!, context)
        yearListRecyclerView!!.adapter = yearListRecyclerViewAdapter
        yearListRecyclerView!!.layoutManager = LinearLayoutManager(context)


        return view
    }

    override fun onDetach()
    {
        super.onDetach()
        context.unlockDrawerLayout()
    }

    companion object
    {
        /**
         * Creates a new instance
         * @return A new instance of fragment [YearListFragment].
         */
        fun newInstance(): YearListFragment
        {
            val fragment = YearListFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}

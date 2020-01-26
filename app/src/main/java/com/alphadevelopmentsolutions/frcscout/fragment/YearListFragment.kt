package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.adapter.YearListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.classes.Tables.Year
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import java.util.*

class YearListFragment : MasterFragment()
{
    override fun onBackPressed(): Boolean
    {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        loadYearsThread = Thread(Runnable {
            loadingThread.join()

            years = Year.getObjects(null, database)
            searchedYears = ArrayList(years)
        })

        loadYearsThread.start()
    }

    private lateinit var years: ArrayList<Year>
    private lateinit var searchedYears: ArrayList<Year>

    private lateinit var loadYearsThread: Thread

    private var previousSearchLength: Int = 0

    private var yearListRecyclerView: RecyclerView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_year_list, container, false)

        context.setToolbarTitle(R.string.years)
        context.lockDrawerLayout()
        context.isToolbarScrollable = true

        //showing this view means the user has not selected an event or year, clear the shared pref
        context.keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, -1)
        context.keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR)) //default to current calendar year

        yearListRecyclerView = view.findViewById(R.id.YearListRecyclerView)

        loadYearsThread.join()

        val yearListRecyclerViewAdapter = YearListRecyclerViewAdapter(searchedYears, context)
        yearListRecyclerView!!.adapter = yearListRecyclerViewAdapter
        yearListRecyclerView!!.layoutManager = LinearLayoutManager(context)

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
                    for (i in years.indices)
                    {
                        val year = years[i]

                        //check if the contact doesn't exist in the viewable list
                        if (!searchedYears.contains(year))
                        {
                            //add it and notify the recyclerview
                            searchedYears.add(i, year)
                            yearListRecyclerViewAdapter.notifyItemInserted(i)
                            yearListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedYears.size)
                        }
                    }
                }

                //Delete from the list
                var i = 0
                while (i < searchedYears.size)
                {
                    val match = searchedYears[i]
                    val name = match.toString()

                    //If the contacts name doesn't equal the searched name
                    if (!name.toLowerCase().contains(searchText.toString().toLowerCase()))
                    {
                        //remove it from the list and notify the recyclerview
                        searchedYears.removeAt(i)
                        yearListRecyclerViewAdapter.notifyItemRemoved(i)
                        yearListRecyclerViewAdapter.notifyItemRangeChanged(i, searchedYears.size)

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

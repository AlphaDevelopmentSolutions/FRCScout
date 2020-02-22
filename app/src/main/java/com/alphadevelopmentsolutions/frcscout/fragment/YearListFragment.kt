package com.alphadevelopmentsolutions.frcscout.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.alphadevelopmentsolutions.frcscout.adapter.YearListRecyclerViewAdapter
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.KeyStore
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_year_list.view.*
import kotlin.collections.ArrayList

class YearListFragment : MasterFragment()
{
    private var previousSearchLength: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View?
    {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_year_list, container, false)

        activityContext.setToolbarTitle(R.string.years)
        activityContext.lockDrawerLayout()
        activityContext.isToolbarScrollable = true

        //showing this view means the user has not selected an eventId or yearId, clear the shared pref
        KeyStore.getInstance(activityContext).setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, "")
        KeyStore.getInstance(activityContext).setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, "")

        val disposable = VMProvider(this).yearViewModel.objs
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { years ->
                            val yearListRecyclerView = view.YearListRecyclerView

                            val searchedYears = ArrayList(years)

                            val yearListRecyclerViewAdapter = YearListRecyclerViewAdapter(searchedYears, activityContext) // COPY OF YEARS
                            yearListRecyclerView!!.adapter = yearListRecyclerViewAdapter
                            yearListRecyclerView.layoutManager = LinearLayoutManager(activityContext)

                            activityContext.isToolbarScrollable = true
                            activityContext.isSearchViewVisible = true

                            activityContext.setSearchViewOnTextChangeListener(object: SearchView.OnQueryTextListener{
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
                        },
                        {
                            AppLog.error(it)
                        }
                )
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

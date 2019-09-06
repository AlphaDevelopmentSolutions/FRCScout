package com.alphadevelopmentsolutions.frcscout.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

internal class FragmentViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager)
{

    private val fragmentList: ArrayList<Fragment>
    private val titleList: ArrayList<String>

    init
    {

        fragmentList = ArrayList()
        titleList = ArrayList()
    }

    /**
     * Adds a fragment to the viewpager
     * @param fragment to add
     * @param title of the fragment
     */
    fun addFragment(fragment: Fragment, title: String)
    {
        fragmentList.add(fragment)
        titleList.add(title)
    }

    override fun getPageTitle(position: Int): CharSequence?
    {
        return titleList[position]
    }

    override fun getItem(position: Int): Fragment
    {
        return fragmentList[position]
    }

    override fun getCount(): Int
    {
        return fragmentList.size
    }
}

package com.alphadevelopmentsolutions.frcscout.Adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.alphadevelopmentsolutions.frcscout.Fragments.MasterFragment
import java.util.*

internal class FragmentViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager)
{

    private val fragmentList: ArrayList<MasterFragment> = ArrayList()
    private val titleList: ArrayList<String> = ArrayList()

    /**
     * Adds a fragment to the viewpager
     * @param fragment to add
     * @param title of the fragment
     */
    fun addFragment(fragment: MasterFragment, title: String)
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

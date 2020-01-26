package com.alphadevelopmentsolutions.frcscout.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alphadevelopmentsolutions.frcscout.fragment.MasterFragment
import java.util.*

internal class FragmentViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)
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

package com.alphadevelopmentsolutions.frcscout.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alphadevelopmentsolutions.frcscout.classes.ViewPagerFragment

class FragmentViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList: ArrayList<Fragment> = ArrayList()
    private val titleList: ArrayList<String> = ArrayList()

    /**
     * Adds a fragment to the viewpager
     * @param fragment to add
     */
    fun addFragment(fragment: ViewPagerFragment) {
        fragmentList.add(fragment.fragment)
        titleList.add(fragment.title)
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titleList[position]
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
package com.alphadevelopmentsolutions.frcscout.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.alphadevelopmentsolutions.frcscout.classes.ViewPagerFragment

class FragmentViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
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

    fun getTitle(position: Int): String {
        return titleList[position]
    }

    override fun getItemCount(): Int {
        return fragmentList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
}
package com.alphadevelopmentsolutions.frcscout.bindings

import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.alphadevelopmentsolutions.frcscout.adapters.FragmentViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@BindingAdapter(value = ["vpAdapter", "vpTabLayout"])
fun ViewPager2.setAdapter(adapter: FragmentViewPagerAdapter, tabLayout: TabLayout) {
    this.adapter = adapter

    TabLayoutMediator(tabLayout, this) { tab, position ->
        tab.text = adapter.getTitle(position)
    }.attach()
}

@BindingAdapter("vpPageChangeCallback")
fun ViewPager2.registerOnPageChangeCallback(callback: ViewPager2.OnPageChangeCallback) {
    this.registerOnPageChangeCallback(callback)
}

package com.alphadevelopmentsolutions.frcscout.bindings

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.alphadevelopmentsolutions.frcscout.adapters.FragmentViewPagerAdapter

@BindingAdapter("vpAdapter")
fun ViewPager2.setImage(adapter: FragmentViewPagerAdapter) {
    setAdapter(adapter)
}

@BindingAdapter("vpChangeListener")
fun ViewPager2.addOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
    addOnPageChangeListener(listener)
}

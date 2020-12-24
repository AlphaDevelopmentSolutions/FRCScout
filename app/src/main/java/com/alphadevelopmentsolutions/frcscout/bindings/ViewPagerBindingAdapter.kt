package com.alphadevelopmentsolutions.frcscout.bindings

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.alphadevelopmentsolutions.frcscout.adapters.FragmentViewPagerAdapter

@BindingAdapter("vpAdapter")
fun ViewPager.setImage(adapter: FragmentViewPagerAdapter) {
    setAdapter(adapter)
}

@BindingAdapter("vpChangeListener")
fun ViewPager.addOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
    addOnPageChangeListener(listener)
}

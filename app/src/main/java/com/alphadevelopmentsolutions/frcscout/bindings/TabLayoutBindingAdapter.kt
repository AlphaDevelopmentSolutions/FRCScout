package com.alphadevelopmentsolutions.frcscout.bindings

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

@BindingAdapter("tlViewPager")
fun TabLayout.setImage(viewPager: (() -> ViewPager)) {
    setupWithViewPager(viewPager())
}

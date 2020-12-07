package com.alphadevelopmentsolutions.frcscout.bindings

import android.text.TextWatcher

import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.alphadevelopmentsolutions.frcscout.adapters.FragmentViewPagerAdapter
import com.alphadevelopmentsolutions.frcscout.singletons.GlideInstance
import com.alphadevelopmentsolutions.frcscout.ui.fragments.teams.TeamListRecyclerViewAdapter

@BindingAdapter("vpAdapter")
fun ViewPager.setImage(adapter: FragmentViewPagerAdapter) {
    setAdapter(adapter)
}

@BindingAdapter("vpChangeListener")
fun ViewPager.addOnPageChangeListener(listener: ViewPager.OnPageChangeListener) {
    addOnPageChangeListener(listener)
}

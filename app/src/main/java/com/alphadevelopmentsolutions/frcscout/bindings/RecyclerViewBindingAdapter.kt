package com.alphadevelopmentsolutions.frcscout.bindings

import android.text.TextWatcher

import android.widget.EditText
import android.widget.LinearLayout

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.ui.fragments.teams.TeamListRecyclerViewAdapter

@BindingAdapter("rvAdapter")
fun <T: RecyclerView.ViewHolder> RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<T>) {
    layoutManager = LinearLayoutManager(context)

    setAdapter(adapter)
}

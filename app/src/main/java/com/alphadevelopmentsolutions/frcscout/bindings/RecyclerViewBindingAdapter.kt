package com.alphadevelopmentsolutions.frcscout.bindings

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.*

@BindingAdapter("rvAdapter")
fun <T: RecyclerView.ViewHolder> RecyclerView.bindRecyclerViewAdapter(adapter: RecyclerView.Adapter<T>) {
    layoutManager = LinearLayoutManager(context)

    setAdapter(adapter)
}

@BindingAdapter("rvFlexAdapter")
fun <T: RecyclerView.ViewHolder> RecyclerView.bindRecy(adapter: RecyclerView.Adapter<T>) {
    layoutManager =
        FlexboxLayoutManager(context).apply {
            flexWrap = FlexWrap.WRAP
            flexDirection = FlexDirection.ROW
            alignItems = AlignItems.STRETCH
            addItemDecoration(
                FlexboxItemDecoration(context).apply {
                    setOrientation(FlexboxItemDecoration.BOTH)
                }
            )
        }

    setAdapter(adapter)
}
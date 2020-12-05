package com.alphadevelopmentsolutions.frcscout.bindings

import android.text.TextWatcher

import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.singletons.GlideInstance
import com.alphadevelopmentsolutions.frcscout.ui.fragments.teams.TeamListRecyclerViewAdapter

@BindingAdapter("glideSrc")
fun ImageView.setImage(uri: String?) {
    uri?.let {
        GlideInstance.loadPhoto(
            context,
            this,
            it
        )
    }
}

package com.alphadevelopmentsolutions.frcscout.bindings

import android.text.TextWatcher
import android.view.View

import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
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

        if (visibility != View.VISIBLE)
            visibility = View.VISIBLE
    }
}

@BindingAdapter(value = ["liveGlideSrcOwner", "liveGlideSrc"])
fun ImageView.setImage(lifecycleOwner: LifecycleOwner, uri: MutableLiveData<String?>) {
    uri.observe(
        lifecycleOwner
    ) {
        setImage(it)
    }
}

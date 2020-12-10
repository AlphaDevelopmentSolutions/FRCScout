package com.alphadevelopmentsolutions.frcscout.bindings

import android.view.View

import android.widget.ImageView

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.alphadevelopmentsolutions.frcscout.singletons.GlideInstance

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

@BindingAdapter("onLongClick")
fun ImageView.setOnLongClick(callback: () -> Boolean) {
    setOnLongClickListener {
        callback()
    }
}

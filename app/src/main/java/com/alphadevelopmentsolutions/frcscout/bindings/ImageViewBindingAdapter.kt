package com.alphadevelopmentsolutions.frcscout.bindings

import android.graphics.drawable.Drawable
import android.view.View

import android.widget.ImageView
import androidx.annotation.RawRes

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import com.alphadevelopmentsolutions.frcscout.singletons.GlideConstants
import com.bumptech.glide.Glide

@BindingAdapter("glideSrc")
fun ImageView.setImage(uri: String?) {
    uri?.let {
        Glide
            .with(context)
            .load(it)
            .transition(GlideConstants.TRANSITION_OPTIONS)
            .thumbnail(GlideConstants.THUMB_SIZE)
            .diskCacheStrategy(GlideConstants.DISK_CACHE_STRATEGY)
            .skipMemoryCache(false)
            .into(this)

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

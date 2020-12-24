package com.alphadevelopmentsolutions.frcscout.singletons

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object GlideConstants {
    private const val FADE_DURATION = 300
    const val THUMB_SIZE = 0.05f
    val DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL

    val TRANSITION_OPTIONS = DrawableTransitionOptions().crossFade(FADE_DURATION)
}
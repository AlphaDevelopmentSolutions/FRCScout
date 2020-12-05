package com.alphadevelopmentsolutions.frcscout.singletons

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

object GlideInstance {
    private const val FADE_DURATION = 300
    private const val THUMB_SIZE = 0.05f
    private val DISK_CACHE_STRATEGY = DiskCacheStrategy.ALL

    private val TRANSITION_OPTIONS = DrawableTransitionOptions().crossFade(FADE_DURATION)

    /**
     * Loads a photo into the provided [view]
     * @param view [ImageView] to load [fileUri] into
     * @param fileUri [String] file uri to load into [view]
     * @param isThumbnail [Boolean] should the image be loaded as a thumbnail
     * @param cacheStrategy [DiskCacheStrategy] cache strategy after the image is loaded
     * @param skipMemoryCache [Boolean] should the load skip the memory cache strategy
     */
    fun loadPhoto(context: Context, view: ImageView, fileUri: String, isThumbnail: Boolean = true, cacheStrategy: DiskCacheStrategy = DISK_CACHE_STRATEGY, skipMemoryCache: Boolean = false) {
        Glide
            .with(context)
            .load(fileUri)
            .transition(TRANSITION_OPTIONS)
            .thumbnail(if (isThumbnail) THUMB_SIZE else 1f)
            .diskCacheStrategy(cacheStrategy)
            .skipMemoryCache(skipMemoryCache)
            .into(view)
    }

    /**
     * Loads a photo into the provided [view]
     * @param view [ImageView] to load [bitmap] into
     * @param bitmap [Bitmap] bitmap file to load into [view]
     * @param isThumbnail [Boolean] should the image be loaded as a thumbnail
     * @param cacheStrategy [DiskCacheStrategy] cache strategy after the image is loaded
     * @param skipMemoryCache [Boolean] should the load skip the memory cache strategy
     */
    fun loadPhoto(context: Context, view: ImageView, bitmap: Bitmap, isThumbnail: Boolean = true, cacheStrategy: DiskCacheStrategy = DISK_CACHE_STRATEGY, skipMemoryCache: Boolean = false) {
        Glide
            .with(context)
            .load(bitmap)
            .transition(TRANSITION_OPTIONS)
            .thumbnail(if (isThumbnail) THUMB_SIZE else 1f)
            .diskCacheStrategy(cacheStrategy)
            .skipMemoryCache(skipMemoryCache)
            .into(view)
    }

    /**
     * Loads a photo into the provided [view]
     * @param view [ImageView] to load [resourceId] into
     * @param resourceId [Int] resource ID to load into [view]
     * @param isThumbnail [Boolean] should the image be loaded as a thumbnail
     * @param cacheStrategy [DiskCacheStrategy] cache strategy after the image is loaded
     * @param skipMemoryCache [Boolean] should the load skip the memory cache strategy
     */
    fun loadPhoto(context: Context, view: ImageView, @RawRes @DrawableRes resourceId: Int, isThumbnail: Boolean = true, cacheStrategy: DiskCacheStrategy = DISK_CACHE_STRATEGY, skipMemoryCache: Boolean = false) {
        Glide
            .with(context)
            .load(resourceId)
            .transition(TRANSITION_OPTIONS)
            .thumbnail(if (isThumbnail) THUMB_SIZE else 1f)
            .diskCacheStrategy(cacheStrategy)
            .skipMemoryCache(skipMemoryCache)
            .into(view)
    }
}
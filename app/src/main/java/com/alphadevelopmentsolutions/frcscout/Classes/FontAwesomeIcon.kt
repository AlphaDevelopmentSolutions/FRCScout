package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View

abstract class FontAwesomeIcon : android.support.v7.widget.AppCompatTextView
{
    constructor(context: Context) : super(context)
    {
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    {
    }

    /**
     * Hides the fontawesome icon from the screen
     */
    fun hide()
    {
        visibility = View.GONE
    }

    /**
     * Sets the URL to go to when the icon has been clicked
     */
    fun setURL(URL: String, context: Context)
    {
        setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(URL)
            context.startActivity(intent)
        }
    }

    internal abstract fun init()
}

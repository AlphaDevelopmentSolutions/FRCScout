package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.Context
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet

import com.alphadevelopmentsolutions.frcscout.R

/**
 * This class is used to display FontAwesome Solid icons to the app as textviews
 */
class FontAwesomeSolidIcon : FontAwesomeIcon
{

    private var ctx: Context? = null

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)
    {

        this.ctx = context

        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    {

        this.ctx = context
        init()
    }

    constructor(context: Context) : super(context)
    {

        this.ctx = context

        init()
    }

    /**
     * Sets the typeface to the FontAwesomeBrands.otf font
     * Icon ascii vals are stored in the font_awesome_image_codes.xml under values dir
     */
    internal override fun init()
    {
        typeface = ResourcesCompat.getFont(ctx!!, R.font.font_awesome_solids)
    }

}

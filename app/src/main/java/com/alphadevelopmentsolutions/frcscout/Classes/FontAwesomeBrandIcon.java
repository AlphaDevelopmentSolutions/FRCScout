package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.alphadevelopmentsolutions.frcscout.R;

/**
 * This class is used to display FontAwesome Brand icons to the app as textviews
 */
public class FontAwesomeBrandIcon extends FontAwesomeIcon
{

    private Context context;

    public FontAwesomeBrandIcon(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        this.context = context;

        init();
    }
    public FontAwesomeBrandIcon(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;
        init();
    }
    public FontAwesomeBrandIcon(Context context)
    {
        super(context);

        this.context = context;

        init();
    }

    /**
     * Sets the typeface to the FontAwesomeBrands.otf font
     * Icon ascii vals are stored in the font_awesome_image_codes.xml under values dir
     */
    void init()
    {
        setTypeface(ResourcesCompat.getFont(context, R.font.font_awesome_brands));
    }

}

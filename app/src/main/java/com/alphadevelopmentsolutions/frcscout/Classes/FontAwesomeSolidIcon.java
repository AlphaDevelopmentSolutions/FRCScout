package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.util.AttributeSet;

import com.alphadevelopmentsolutions.frcscout.R;

public class FontAwesomeSolidIcon extends android.support.v7.widget.AppCompatTextView
{

    private Context context;

    public FontAwesomeSolidIcon(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);

        this.context = context;

        init();
    }
    public FontAwesomeSolidIcon(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        this.context = context;
        init();
    }
    public FontAwesomeSolidIcon(Context context)
    {
        super(context);

        this.context = context;

        init();
    }

    /**
     * Sets the typeface to the FontAwesomeBrands.otf font
     * Icon ascii vals are stored in the font_awesome_image_codes.xml under values dir
     */
    private void init()
    {
        setTypeface(ResourcesCompat.getFont(context, R.font.font_awesome_solids));
    }

}

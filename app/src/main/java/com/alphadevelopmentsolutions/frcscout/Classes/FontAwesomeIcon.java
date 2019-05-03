package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

public abstract class FontAwesomeIcon extends android.support.v7.widget.AppCompatTextView
{
    public FontAwesomeIcon(Context context)
    {
        super(context);
    }

    public FontAwesomeIcon(Context context, AttributeSet attrs)
    {
        super(context, attrs);
    }

    public FontAwesomeIcon(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
    }

    /**
     * Hides the fontawesome icon from the screen
     */
    public void hide()
    {
        setVisibility(GONE);
    }

    /**
     * Sets the URL to go to when the icon has been clicked
     */
    public void setURL(final String URL, final Context context)
    {
        setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                context.startActivity(intent);
            }
        });
    }

    abstract void init();
}

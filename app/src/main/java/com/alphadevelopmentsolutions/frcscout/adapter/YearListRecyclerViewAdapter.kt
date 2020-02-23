package com.alphadevelopmentsolutions.frcscout.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Year
import com.alphadevelopmentsolutions.frcscout.fragment.EventListFragment
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.KeyStore
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

internal class YearListRecyclerViewAdapter(private val yearList: ArrayList<Year>, private val context: MainActivity) : RecyclerView.Adapter<YearListRecyclerViewAdapter.ViewHolder>()
{
    class ViewHolder(view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        var yearTitleTextView: TextView
        var yearDateTextView: TextView
        var yearLogoImageView: ImageView
        var viewButton: Button

        init
        {

            yearTitleTextView = view.findViewById(R.id.YearTitleTextView)
            yearDateTextView = view.findViewById(R.id.YearDateTextView)
            yearLogoImageView = view.findViewById(R.id.YearLogoImageView)
            viewButton = view.findViewById(R.id.ViewButton)
            viewButton.setTextColor(context.primaryColor)
            (viewButton as MaterialButton).rippleColor = context.buttonRipple
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the eventId layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_year, viewGroup, false)

        return YearListRecyclerViewAdapter.ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: YearListRecyclerViewAdapter.ViewHolder, position: Int)
    {
        val year = yearList[viewHolder.adapterPosition]

        //Set the content on the card
        viewHolder.yearTitleTextView.text = year.toString()
        viewHolder.yearDateTextView.text = String.format("%s - %s", year.startDate?.toString(), year.endDate?.toString())

        //load the photo if the file exists
        if (year.imageUri != "")
            Picasso.get()
                    .load(Uri.fromFile(File(year.imageUri)))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.yearLogoImageView)

        //Sends you to the eventId list fragment
        viewHolder.viewButton.setOnClickListener {
            KeyStore.getInstance(context).selectedYearId = yearList[viewHolder.adapterPosition].id
            context.changeFragment(
                    EventListFragment.newInstance(
                            yearList[viewHolder.adapterPosition].id
                    ),
                    false
            )
        }

    }

    override fun getItemCount(): Int
    {
        return yearList.size
    }
}

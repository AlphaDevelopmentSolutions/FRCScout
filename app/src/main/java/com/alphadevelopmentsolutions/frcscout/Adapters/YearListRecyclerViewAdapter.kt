package com.alphadevelopmentsolutions.frcscout.Adapters

import android.net.Uri
import android.support.design.button.MaterialButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year
import com.alphadevelopmentsolutions.frcscout.Fragments.EventListFragment
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.squareup.picasso.Picasso
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

internal class YearListRecyclerViewAdapter(private val yearList: ArrayList<Year>, private val context: MainActivity) : RecyclerView.Adapter<YearListRecyclerViewAdapter.ViewHolder>()
{

    private val simpleDateFormat: SimpleDateFormat


    init
    {

        simpleDateFormat = SimpleDateFormat("MMM d, yyyy")
    }

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
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_year, viewGroup, false)

        return YearListRecyclerViewAdapter.ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: YearListRecyclerViewAdapter.ViewHolder, position: Int)
    {
        val year = yearList[viewHolder.adapterPosition]

        //Set the content on the card
        viewHolder.yearTitleTextView.text = year.toString()
        viewHolder.yearDateTextView.text = String.format("%s - %s", simpleDateFormat.format(year.startDate.time), simpleDateFormat.format(year.endDate.time))

        //load the photo if the file exists
        if (year.imageUri != "")
            Picasso.get()
                    .load(Uri.fromFile(File(year.imageUri)))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.yearLogoImageView)
        else
            viewHolder.yearLogoImageView.setImageDrawable(context.getDrawable(R.drawable.frc_logo))


        //Sends you to the event list fragment
        viewHolder.viewButton.setOnClickListener {
            context.keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, yearList[viewHolder.adapterPosition].serverId!!)
            context.changeFragment(EventListFragment.newInstance(yearList[viewHolder.adapterPosition]), false)
        }

    }

    override fun getItemCount(): Int
    {
        return yearList.size
    }
}

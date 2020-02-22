package com.alphadevelopmentsolutions.frcscout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import com.alphadevelopmentsolutions.frcscout.fragment.MatchListFragment
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_card_event.view.*
import java.text.SimpleDateFormat
import java.util.*

internal class EventListRecyclerViewAdapter(private val eventList: ArrayList<Event>, private val context: MainActivity) : RecyclerView.Adapter<EventListRecyclerViewAdapter.ViewHolder>()
{

    private val simpleDateFormat: SimpleDateFormat

    init
    {

        simpleDateFormat = SimpleDateFormat("MMM d, yyyy")
    }

    internal class ViewHolder(val view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        init
        {
            view.ViewEventButton.setTextColor(context.primaryColor)
            (view.ViewEventButton as MaterialButton).rippleColor = context.buttonRipple
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the eventId layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_event, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val event = eventList[viewHolder.adapterPosition]

        //Set the content on the card
        viewHolder.view.EventTitleTextView.text = event.name
        viewHolder.view.EventLocationTextView.text = String.format("%s, %s, %s", event.city, event.stateProvince, event.country)
        viewHolder.view.EventDateTextView.text = String.format("%s - %s", simpleDateFormat.format(event.startDate.time), simpleDateFormat.format(event.endDate.time))

        //Sends you to the teamlist fragment
        viewHolder.view.ViewEventButton.setOnClickListener {
            //store the selected eventId in the shared pref
            context.keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, eventList[viewHolder.adapterPosition].id)
            context.setCheckedMenuItem(0)
            context.updateNavText(eventList[viewHolder.adapterPosition])
            context.changeFragment(MatchListFragment.newInstance(null), false, false)
        }
    }

    override fun getItemCount(): Int
    {
        return eventList.size
    }
}

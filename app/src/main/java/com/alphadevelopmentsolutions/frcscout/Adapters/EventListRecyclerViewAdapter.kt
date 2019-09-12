package com.alphadevelopmentsolutions.frcscout.Adapters

import android.support.design.button.MaterialButton
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event
import com.alphadevelopmentsolutions.frcscout.Fragments.MatchListFragment
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.R
import java.text.SimpleDateFormat
import java.util.*

internal class EventListRecyclerViewAdapter(private val eventList: ArrayList<Event>, private val context: MainActivity) : RecyclerView.Adapter<EventListRecyclerViewAdapter.ViewHolder>()
{

    private val simpleDateFormat: SimpleDateFormat

    init
    {

        simpleDateFormat = SimpleDateFormat("MMM d, yyyy")
    }

    internal class ViewHolder(view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        var eventTitleTextView: TextView
        var eventLocationTextView: TextView
        var eventDateTextView: TextView
        var viewEventButton: TextView

        init
        {

            eventTitleTextView = view.findViewById(R.id.EventTitleTextView)
            eventLocationTextView = view.findViewById(R.id.EventLocationTextView)
            eventDateTextView = view.findViewById(R.id.EventDateTextView)
            viewEventButton = view.findViewById(R.id.ViewEventButton)
            viewEventButton.setTextColor(context.primaryColor)
            (viewEventButton as MaterialButton).rippleColor = context.buttonRipple
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_event, viewGroup, false)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val event = eventList[viewHolder.adapterPosition]

        //Set the content on the card
        viewHolder.eventTitleTextView.text = event.name
        viewHolder.eventLocationTextView.text = String.format("%s, %s, %s", event.city, event.stateProvince, event.country)
        viewHolder.eventDateTextView.text = String.format("%s - %s", simpleDateFormat.format(event.startDate!!.time), simpleDateFormat.format(event.endDate!!.time))

        //Sends you to the teamlist fragment
        viewHolder.viewEventButton.setOnClickListener {
            //store the selected event in the shared pref
            context.keyStore.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, eventList[viewHolder.adapterPosition].id)
            context.setCheckedMenuItem(0)
            context.changeFragment(MatchListFragment.newInstance(null), false, false)
        }
    }

    override fun getItemCount(): Int
    {
        return eventList.size
    }
}

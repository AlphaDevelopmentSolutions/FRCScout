package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

public class EventsRecyclerViewAdapter extends RecyclerView.Adapter<EventsRecyclerViewAdapter.ViewHolder>
{

    private Context context;

    private ArrayList<Event> eventList;

    public EventsRecyclerViewAdapter(ArrayList<Event> eventList, Context context)
    {
        this.context = context;
        this.eventList = eventList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView eventTitleTextView;
        TextView eventLocationTextView;
        TextView eventDateTextView;

        ViewHolder(@NonNull View view)
        {
            super(view);

            eventTitleTextView = view.findViewById(R.id.EventTitleTextView);
            eventLocationTextView = view.findViewById(R.id.EventLocationTextView);
            eventDateTextView = view.findViewById(R.id.EventDateTextView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_event, viewGroup, false);

        return new EventsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventsRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        viewHolder.eventTitleTextView.setText("test");
        viewHolder.eventLocationTextView.setText("test2");
        viewHolder.eventDateTextView.setText("test3");
    }

    @Override
    public int getItemCount()
    {
        return eventList.size();
    }
}

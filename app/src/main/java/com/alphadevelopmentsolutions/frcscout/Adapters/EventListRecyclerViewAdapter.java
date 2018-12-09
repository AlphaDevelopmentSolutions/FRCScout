package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

public class EventListRecyclerViewAdapter extends RecyclerView.Adapter<EventListRecyclerViewAdapter.ViewHolder>
{

    private MainActivity context;

    private ArrayList<Event> eventList;

    public EventListRecyclerViewAdapter(ArrayList<Event> eventList, MainActivity context)
    {
        this.context = context;
        this.eventList = eventList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView eventTitleTextView;
        TextView eventLocationTextView;
        TextView eventDateTextView;
        TextView viewEventButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            eventTitleTextView = view.findViewById(R.id.EventTitleTextView);
            eventLocationTextView = view.findViewById(R.id.EventLocationTextView);
            eventDateTextView = view.findViewById(R.id.EventDateTextView);
            viewEventButton = view.findViewById(R.id.ViewEventButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_event_card, viewGroup, false);

        return new EventListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        //Set the content on the card
        viewHolder.eventTitleTextView.setText(eventList.get(position).getName());
        viewHolder.eventLocationTextView.setText(eventList.get(position).getCity() + ", " + eventList.get(position).getStateProvince() + ", " + eventList.get(position).getCountry());
        viewHolder.eventDateTextView.setText(eventList.get(position).getStartDate().getTime() + " - " + eventList.get(position).getEndDate().getTime()); //TODO: Format date

        //Sends you to the teamlist fragment
        viewHolder.viewEventButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //swap fragments
                FragmentManager fragmentManager = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.MainFrame, new TeamListFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return eventList.size();
    }
}

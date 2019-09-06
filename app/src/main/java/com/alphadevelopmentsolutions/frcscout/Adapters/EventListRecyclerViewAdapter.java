package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Fragments.ChecklistFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventListRecyclerViewAdapter extends RecyclerView.Adapter<EventListRecyclerViewAdapter.ViewHolder>
{

    private MainActivity context;

    private ArrayList<Event> eventList;

    private SimpleDateFormat simpleDateFormat;

    private Type fragmentOnClick;

    private Gson gson;

    public EventListRecyclerViewAdapter(ArrayList<Event> eventList, MainActivity context, Type fragmentOnClick)
    {
        this.context = context;
        this.eventList = eventList;
        this.fragmentOnClick = fragmentOnClick;

        this.gson = new Gson();

        simpleDateFormat = new SimpleDateFormat("MMM d, yyyy");
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
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_card_event, viewGroup, false);

        return new EventListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EventListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        Event event = eventList.get(viewHolder.getAdapterPosition());
        
        //Set the content on the card
        viewHolder.eventTitleTextView.setText(event.getName());
        viewHolder.eventLocationTextView.setText(event.getCity() + ", " + event.getStateProvince() + ", " + event.getCountry());
        viewHolder.eventDateTextView.setText(simpleDateFormat.format(event.getStartDate().getTime()) + " - " + simpleDateFormat.format(event.getEndDate().getTime())); //TODO: Format date

        //Sends you to the teamlist fragment
        viewHolder.viewEventButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //store the selected event in the shared pref
                context.setPreference(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, eventList.get(viewHolder.getAdapterPosition()).getId());

                if(fragmentOnClick.equals(TeamListFragment.class))
                    context.changeFragment(TeamListFragment.newInstance(), false);

                else if(fragmentOnClick.equals(ChecklistFragment.class))
                {
                    Team team = new Team((int) context.getPreference(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, -1), context.getDatabase());

                    context.changeFragment(ChecklistFragment.newInstance(gson.toJson(team), null), false);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return eventList.size();
    }
}

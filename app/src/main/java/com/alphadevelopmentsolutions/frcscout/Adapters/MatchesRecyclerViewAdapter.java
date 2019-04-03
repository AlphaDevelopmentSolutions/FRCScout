package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class MatchesRecyclerViewAdapter extends RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder>
{

    private ArrayList<Match> matches;

    private MainActivity context;

    private String eventJson;

    private Event event;

    private Gson gson;

    public MatchesRecyclerViewAdapter(String eventJson, ArrayList<Match> matches, MainActivity context)
    {
        this.matches = matches;
        this.context = context;
        this.eventJson = eventJson;

        gson = new Gson();

        this.event = gson.fromJson(eventJson, Event.class);

    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView matchIdTextView;
        ImageView matchOptionsImageView;
        Button viewMatchButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            matchIdTextView = view.findViewById(R.id.MatchIdTextView);
            matchOptionsImageView = view.findViewById(R.id.MatchOptionsImageView);
            viewMatchButton = view.findViewById(R.id.ViewMatchButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.layout_scout_card, viewGroup, false);

        return new MatchesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchesRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {

        final Match match = matches.get(viewHolder.getAdapterPosition());

        //set scores
        viewHolder.matchIdTextView.setText(String.valueOf(match.getMatchNumber()));

        //Opens an option menu for various options on that score card
        viewHolder.matchOptionsImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        }); //TODO: options menu


        //Sends you to the scout card fragment
        viewHolder.viewMatchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //get the scout card from the match
                ArrayList<ScoutCard> scoutCards = match.getScoutCards(context.getDatabase(), event);

                if(scoutCards.size() > 1)
                {

                }
                else
                {
                    //swap fragments
                    context.changeFragment(ScoutCardFragment.newInstance(gson.toJson(scoutCards.get(0)), eventJson, -1), true);
                }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return matches.size();
    }
}

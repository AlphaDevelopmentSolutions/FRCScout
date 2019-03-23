package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class ScoutCardsRecyclerViewAdapter extends RecyclerView.Adapter<ScoutCardsRecyclerViewAdapter.ViewHolder>
{

    private Team team;

    private ArrayList<ScoutCard> scoutCards;

    private MainActivity context;

    public ScoutCardsRecyclerViewAdapter(ArrayList<Match> matchList, MainActivity context)
    {
        this.context = context;
    }

    public ScoutCardsRecyclerViewAdapter(Team team, ArrayList<ScoutCard> scoutCards, MainActivity context)
    {
        this.team = team;
        this.scoutCards = scoutCards;
        this.context = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView matchIdTextView;
        TextView blueAllianceScoreTextView;
        TextView redAllianceScoreTextView;
        ImageView matchOptionsImageView;
        TextView viewMatchButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            matchIdTextView = view.findViewById(R.id.MatchIdTextView);
            blueAllianceScoreTextView = view.findViewById(R.id.BlueAllianceScoreTextView);
            redAllianceScoreTextView = view.findViewById(R.id.RedAllianceScoreTextView);
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

        return new ScoutCardsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScoutCardsRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {

        ScoutCard scoutCard = scoutCards.get(viewHolder.getAdapterPosition());

        //set scores
        viewHolder.blueAllianceScoreTextView.setText(String.valueOf(scoutCard.getBlueAllianceFinalScore()));
        viewHolder.redAllianceScoreTextView.setText(String.valueOf(scoutCard.getRedAllianceFinalScore()));
        viewHolder.matchIdTextView.setText(String.valueOf(scoutCard.getMatchId()));

        //Opens an option menu for various options on that score card
        viewHolder.matchOptionsImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        }); //TODO: options menu


        //Sends you to the match fragment
        viewHolder.viewMatchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //swap fragments
               context.changeFragment(ScoutCardFragment.newInstance(new Gson().toJson(scoutCards.get(viewHolder.getAdapterPosition())), -1), true);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return scoutCards.size();
    }
}

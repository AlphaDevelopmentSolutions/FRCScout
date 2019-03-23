package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.PitCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Fragments.PitCardFragment;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

public class PitCardsRecyclerViewAdapter extends RecyclerView.Adapter<PitCardsRecyclerViewAdapter.ViewHolder>
{

    private Team team;

    private ArrayList<PitCard> pitCards;

    private MainActivity context;

    public PitCardsRecyclerViewAdapter(Team team, ArrayList<PitCard> pitCards, MainActivity context)
    {
        this.team = team;
        this.pitCards = pitCards;
        this.context = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView pitCardIdTextView;
        TextView completedByTextView;
        TextView redAllianceScoreTextView;
        ImageView pitCardOptionsImageView;
        TextView viewPitCardButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            pitCardIdTextView = view.findViewById(R.id.PitCardIdTextView);
            completedByTextView = view.findViewById(R.id.CompletedByTextView);
            pitCardOptionsImageView = view.findViewById(R.id.PitCardOptionsImageView);
            viewPitCardButton = view.findViewById(R.id.ViewPitCardButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.layout_pit_card, viewGroup, false);

        return new PitCardsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PitCardsRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {

        PitCard pitCard = pitCards.get(viewHolder.getAdapterPosition());


        //set scores
        viewHolder.completedByTextView.setText(pitCard.getCompletedBy());
        viewHolder.pitCardIdTextView.setText(String.valueOf(pitCard.getId()));

        //Opens an option menu for various options on that score card
        viewHolder.pitCardOptionsImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        }); //TODO: options menu


        //Sends you to the match fragment
        viewHolder.viewPitCardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //swap fragments
               context.changeFragment(PitCardFragment.newInstance(pitCards.get(viewHolder.getAdapterPosition()).getId(), -1), true);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return pitCards.size();
    }
}

package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.GameScoreStatuses;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

public class ScoutCardListRecyclerViewAdapter extends RecyclerView.Adapter<ScoutCardListRecyclerViewAdapter.ViewHolder>
{

    private MainActivity context;

    private ArrayList<ScoutCard> scoutCardList;

    public ScoutCardListRecyclerViewAdapter(ArrayList<ScoutCard> scoutCardList, MainActivity context)
    {
        this.context = context;
        this.scoutCardList = scoutCardList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView scoutCardDateTextView;
        TextView scoutCardWinLoseScoreTextView;
        ImageView scoutCardptionsImageView;
        TextView viewScoutCardButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            scoutCardDateTextView = view.findViewById(R.id.ScoutCardDateTextView);
            scoutCardWinLoseScoreTextView = view.findViewById(R.id.ScoutCardWinLoseScoreTextView);
            scoutCardptionsImageView = view.findViewById(R.id.ScoutCardOptionsImageView);
            viewScoutCardButton = view.findViewById(R.id.ViewScoutCardButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_scout_card_card, viewGroup, false);

        return new ScoutCardListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoutCardListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        //Set the content on the card
        viewHolder.scoutCardDateTextView.setText(scoutCardList.get(position).getCompletedDate().getTime() + ""); //TODO: Format completed date

        //format a nice viewable string based on whether the team won or lost
        String winLostText = (scoutCardList.get(position).teamWon()) ? GameScoreStatuses.WIN : (scoutCardList.get(position).teamLost()) ? GameScoreStatuses.LOSE : GameScoreStatuses.TIE;
        String scoreText = scoutCardList.get(position).getScore() + " - " + scoutCardList.get(position).getOpponentScore();
        viewHolder.scoutCardWinLoseScoreTextView.setText(winLostText + " " + scoreText);

        //Opens an option menu for various options on that score card
        viewHolder.scoutCardptionsImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        }); //TODO: options menu


        //Sends you to the scoutcard fragment
        viewHolder.viewScoutCardButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //swap fragments
                FragmentManager fragmentManager = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.MainFrame, new TeamFragment());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return scoutCardList.size();
    }
}

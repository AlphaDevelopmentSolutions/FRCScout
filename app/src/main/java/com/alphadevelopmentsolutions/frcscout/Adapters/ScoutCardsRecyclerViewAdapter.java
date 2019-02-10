package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.graphics.Typeface;
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
import com.alphadevelopmentsolutions.frcscout.Classes.AllianceColor;
import com.alphadevelopmentsolutions.frcscout.Classes.GameScoreStatus;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.R;

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

        TextView matchOutcomeTextView;
        TextView blueAllianceScoreTextView;
        TextView redAllianceScoreTextView;
        TextView blueAllianceTeamOneNumberTextView;
        TextView blueAllianceTeamTwoNumberTextView;
        TextView blueAllianceTeamThreeNumberTextView;
        TextView redAllianceTeamOneNumberTextView;
        TextView redAllianceTeamTwoNumberTextView;
        TextView redAllianceTeamThreeNumberTextView;
        ImageView matchOptionsImageView;
        TextView viewMatchButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            matchOutcomeTextView = view.findViewById(R.id.MatchOutcomeTextView);
            blueAllianceScoreTextView = view.findViewById(R.id.BlueAllianceScoreTextView);
            redAllianceScoreTextView = view.findViewById(R.id.RedAllianceScoreTextView);
            blueAllianceTeamOneNumberTextView = view.findViewById(R.id.BlueAllianceTeamOneNumberTextView);
            blueAllianceTeamTwoNumberTextView = view.findViewById(R.id.BlueAllianceTeamTwoNumberTextView);
            blueAllianceTeamThreeNumberTextView = view.findViewById(R.id.BlueAllianceTeamThreeNumberTextView);
            redAllianceTeamOneNumberTextView = view.findViewById(R.id.RedAllianceTeamOneNumberTextView);
            redAllianceTeamTwoNumberTextView = view.findViewById(R.id.RedAllianceTeamTwoNumberTextView);
            redAllianceTeamThreeNumberTextView = view.findViewById(R.id.RedAllianceTeamThreeNumberTextView);
            matchOptionsImageView = view.findViewById(R.id.MatchOptionsImageView);
            viewMatchButton = view.findViewById(R.id.ViewMatchButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.layout_match_card, viewGroup, false);

        return new ScoutCardsRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ScoutCardsRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        //Set the OutcomeTextView
        if(team != null)
        {
            //gets the alliance color of the current team you are viewing
            //used for WIN / LOSE statuses
//            AllianceColor allianceColor = scoutCards.get(position).getTeamAllianceColor(team.getId());
            viewHolder.matchOutcomeTextView.setText("LOSE");

        }
        //hide the OutcomeTextView
        else
        {
            viewHolder.matchOutcomeTextView.setVisibility(View.GONE);
        }

        //set scores
        viewHolder.blueAllianceScoreTextView.setText(String.valueOf(scoutCards.get(position).getBlueAllianceFinalScore()));
        viewHolder.redAllianceScoreTextView.setText(String.valueOf(scoutCards.get(position).getRedAllianceFinalScore()));

//        //blue team won, bold the score textview
//        if(scoutCards.get(position).getOutcomeStatus(AllianceColor.BLUE).equals(GameScoreStatus.WIN)) viewHolder.blueAllianceScoreTextView.setTypeface(viewHolder.blueAllianceScoreTextView.getTypeface(), Typeface.BOLD);
//
//        //red team won, bold the score textview
//        if(scoutCards.get(position).getOutcomeStatus(AllianceColor.RED).equals(GameScoreStatus.WIN)) viewHolder.redAllianceScoreTextView.setTypeface(viewHolder.redAllianceScoreTextView.getTypeface(), Typeface.BOLD);


//        //set blue team numbers
//        viewHolder.blueAllianceTeamOneNumberTextView.setText(String.valueOf(scoutCards.get(position).getBlueAllianceTeamOneId()));
//        viewHolder.blueAllianceTeamTwoNumberTextView.setText(String.valueOf(matchList.get(position).getBlueAllianceTeamTwoId()));
//        viewHolder.blueAllianceTeamThreeNumberTextView.setText(String.valueOf(matchList.get(position).getBlueAllianceTeamThreeId()));
//
//        //set red team numbers
//        viewHolder.redAllianceTeamOneNumberTextView.setText(String.valueOf(matchList.get(position).getRedAllianceTeamOneId()));
//        viewHolder.redAllianceTeamTwoNumberTextView.setText(String.valueOf(matchList.get(position).getRedAllianceTeamTwoId()));
//        viewHolder.redAllianceTeamThreeNumberTextView.setText(String.valueOf(matchList.get(position).getRedAllianceTeamThreeId()));


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
                FragmentManager fragmentManager = context.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.MainFrame, ScoutCardFragment.newInstance(scoutCards.get(viewHolder.getAdapterPosition()).getId()));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return scoutCards.size();
    }
}

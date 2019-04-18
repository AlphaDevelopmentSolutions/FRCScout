package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.AllianceColor;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

public class MatchesRecyclerViewAdapter extends RecyclerView.Adapter<MatchesRecyclerViewAdapter.ViewHolder>
{

    private ArrayList<Match> matches;

    private MainActivity context;

    private Event event;

    private Team team;

    private Gson gson;

    private HashMap<Match, ArrayList<ScoutCard>> scoutCards;

    public MatchesRecyclerViewAdapter(Event event, Team team, ArrayList<Match> matches, MainActivity context)
    {
        this.matches = matches;
        this.context = context;
        this.event = event;
        this.team = team;

        gson = new Gson();

        scoutCards = new HashMap<>();

        for(Match match : matches)
            //get the scout card from the match
            scoutCards.put(match, match.getScoutCards(context.getDatabase(), event));
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView matchIdTextView;
        
        TextView blueAllianceTeamOneIdTextView;
        TextView blueAllianceTeamTwoIdTextView;
        TextView blueAllianceTeamThreeIdTextView;

        TextView redAllianceTeamOneIdTextView;
        TextView redAllianceTeamTwoIdTextView;
        TextView redAllianceTeamThreeIdTextView;

        TextView blueAllianceScoreTextView;
        TextView redAllianceScoreTextView;

        ImageView matchOptionsImageView;
        
        Button viewMatchButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            matchIdTextView = view.findViewById(R.id.MatchIdTextView);
            
            blueAllianceTeamOneIdTextView = view.findViewById(R.id.BlueAllianceTeamOneIdTextView);
            blueAllianceTeamTwoIdTextView = view.findViewById(R.id.BlueAllianceTeamTwoIdTextView);
            blueAllianceTeamThreeIdTextView = view.findViewById(R.id.BlueAllianceTeamThreeIdTextView);

            redAllianceTeamOneIdTextView = view.findViewById(R.id.RedAllianceTeamOneIdTextView);
            redAllianceTeamTwoIdTextView = view.findViewById(R.id.RedAllianceTeamTwoIdTextView);
            redAllianceTeamThreeIdTextView = view.findViewById(R.id.RedAllianceTeamThreeIdTextView);

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

        return new MatchesRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchesRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {

        final Match match = matches.get(viewHolder.getAdapterPosition());

        //get the list of scout cards from the hashmap and pull the one for the current team
        final ArrayList<ScoutCard> scoutCards = this.scoutCards.get(match);
        ScoutCard scoutCard = null;

        for(ScoutCard storedScoutCard : scoutCards)
        {
            if(storedScoutCard.getTeamId() == team.getId())
            {
                scoutCard = storedScoutCard;
                break;
            }
        }


        //set match numbers
        viewHolder.matchIdTextView.setText(match.getMatchType().toString(match));
        
        //set teams
        viewHolder.blueAllianceTeamOneIdTextView.setText(String.valueOf(match.getBlueAllianceTeamOneId()));
        viewHolder.blueAllianceTeamTwoIdTextView.setText(String.valueOf(match.getBlueAllianceTeamTwoId()));
        viewHolder.blueAllianceTeamThreeIdTextView.setText(String.valueOf(match.getBlueAllianceTeamThreeId()));

        viewHolder.redAllianceTeamOneIdTextView.setText(String.valueOf(match.getRedAllianceTeamOneId()));
        viewHolder.redAllianceTeamTwoIdTextView.setText(String.valueOf(match.getRedAllianceTeamTwoId()));
        viewHolder.redAllianceTeamThreeIdTextView.setText(String.valueOf(match.getRedAllianceTeamThreeId()));

        //set score
        viewHolder.blueAllianceScoreTextView.setText(String.valueOf(match.getBlueAllianceScore()));
        viewHolder.redAllianceScoreTextView.setText(String.valueOf(match.getRedAllianceScore()));

        //set the bold text for the winning team
        Match.Status matchStatus = match.getMatchStatus();

        if(scoutCard == null)
            viewHolder.viewMatchButton.setVisibility(View.INVISIBLE);
        else
            viewHolder.viewMatchButton.setVisibility(View.VISIBLE);


        //blue won
        if(matchStatus == Match.Status.BLUE)
        {
            viewHolder.blueAllianceTeamOneIdTextView.setTypeface(null, Typeface.BOLD);
            viewHolder.blueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.BOLD);
            viewHolder.blueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.BOLD);
            viewHolder.blueAllianceScoreTextView.setTypeface(null, Typeface.BOLD);

            viewHolder.redAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.redAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.redAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.redAllianceScoreTextView.setTypeface(null, Typeface.NORMAL);
        }
        //red won
        else if(matchStatus == Match.Status.RED)
        {
            viewHolder.blueAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.blueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.blueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.blueAllianceScoreTextView.setTypeface(null, Typeface.NORMAL);

            viewHolder.redAllianceTeamOneIdTextView.setTypeface(null, Typeface.BOLD);
            viewHolder.redAllianceTeamTwoIdTextView.setTypeface(null, Typeface.BOLD);
            viewHolder.redAllianceTeamThreeIdTextView.setTypeface(null, Typeface.BOLD);
            viewHolder.redAllianceScoreTextView.setTypeface(null, Typeface.BOLD);
        }
        //tie game
        else if(matchStatus == Match.Status.TIE)
        {
            viewHolder.blueAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.blueAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.blueAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.blueAllianceScoreTextView.setTypeface(null, Typeface.NORMAL);

            viewHolder.redAllianceTeamOneIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.redAllianceTeamTwoIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.redAllianceTeamThreeIdTextView.setTypeface(null, Typeface.NORMAL);
            viewHolder.redAllianceScoreTextView.setTypeface(null, Typeface.NORMAL);
        }

        //Opens an option menu for various options on that score card
        viewHolder.matchOptionsImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        }); //TODO: options menu



        //Sends you to the scout card fragment
        final ScoutCard finalScoutCard = scoutCard;

        viewHolder.viewMatchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                //swap fragments
                context.changeFragment(ScoutCardFragment.newInstance(gson.toJson(finalScoutCard), gson.toJson(event), -1), true);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return matches.size();
    }
}

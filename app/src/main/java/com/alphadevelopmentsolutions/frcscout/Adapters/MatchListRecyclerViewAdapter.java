package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
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
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Enums.AllianceColor;
import com.alphadevelopmentsolutions.frcscout.Fragments.ChecklistFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamListFragment;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class MatchListRecyclerViewAdapter extends RecyclerView.Adapter<MatchListRecyclerViewAdapter.ViewHolder>
{

    private ArrayList<Match> matches;

    private MainActivity context;

    private Team team;

    private HashMap<Match, ArrayList<ScoutCard>> scoutCards;

    private Type fragmentOnClick;

    public MatchListRecyclerViewAdapter(@NonNull Event event, @Nullable Team team, @NonNull MainActivity context, @NonNull Type fragmentOnClick)
    {
        this.context = context;
        this.team = team;
        this.fragmentOnClick = fragmentOnClick;

        this.matches = event.getMatches(context.getDatabase(), team);

        scoutCards = new HashMap<>();

        //load all scout cards for specific team from specific match
        if(fragmentOnClick.equals(ScoutCardFragment.class))
        {
            //load all the scout cards for a match
            for (Match match : matches)
                //get the scout card from the match
                scoutCards.put(match, match.getScoutCards(team, false, context.getDatabase()));
        }
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
        Button addCardButton;

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
            addCardButton = view.findViewById(R.id.AddCardButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.layout_card_match, viewGroup, false);

        return new MatchListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MatchListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {

        final Match match = matches.get(viewHolder.getAdapterPosition());

        //set match numbers
        viewHolder.matchIdTextView.setText(match.toString());
        
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

        AllianceColor selectedTeamAllianceColor = null;
        SpannableString spannableString;

        //set the bold text for the winning team
        Match.Status matchStatus = match.getMatchStatus();

        if(team != null)
        {
            //add the underline when viewing matches for a specific team
            if (team.getId() == match.getBlueAllianceTeamOneId())
            {
                spannableString = new SpannableString(String.valueOf(team.getId()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.blueAllianceTeamOneIdTextView.setText(spannableString);

                selectedTeamAllianceColor = AllianceColor.BLUE;
            } else if (team.getId() == match.getBlueAllianceTeamTwoId())
            {
                spannableString = new SpannableString(String.valueOf(team.getId()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.blueAllianceTeamTwoIdTextView.setText(spannableString);

                selectedTeamAllianceColor = AllianceColor.BLUE;
            } else if (team.getId() == match.getBlueAllianceTeamThreeId())
            {
                spannableString = new SpannableString(String.valueOf(team.getId()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.blueAllianceTeamThreeIdTextView.setText(spannableString);

                selectedTeamAllianceColor = AllianceColor.BLUE;
            } else if (team.getId() == match.getRedAllianceTeamOneId())
            {
                spannableString = new SpannableString(String.valueOf(team.getId()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.redAllianceTeamOneIdTextView.setText(spannableString);

                selectedTeamAllianceColor = AllianceColor.RED;
            } else if (team.getId() == match.getRedAllianceTeamTwoId())
            {
                spannableString = new SpannableString(String.valueOf(team.getId()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.redAllianceTeamTwoIdTextView.setText(spannableString);

                selectedTeamAllianceColor = AllianceColor.RED;
            } else if (team.getId() == match.getRedAllianceTeamThreeId())
            {
                spannableString = new SpannableString(String.valueOf(team.getId()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.redAllianceTeamThreeIdTextView.setText(spannableString);

                selectedTeamAllianceColor = AllianceColor.RED;
            }

            //underline the score of the selected team
            if (selectedTeamAllianceColor == AllianceColor.BLUE)
            {
                spannableString = new SpannableString(String.valueOf(match.getBlueAllianceScore()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.blueAllianceScoreTextView.setText(spannableString);
            } else
            {
                spannableString = new SpannableString(String.valueOf(match.getRedAllianceScore()));
                spannableString.setSpan(new UnderlineSpan(), 0, spannableString.length(), 0);
                viewHolder.redAllianceScoreTextView.setText(spannableString);
            }
        }


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


        //logic for showing the view scout card button
        if(fragmentOnClick.equals(ScoutCardFragment.class))
        {
            //get the list of scout cards from the hashmap and pull the one for the current team
            final ArrayList<ScoutCard> scoutCards = this.scoutCards.get(match);

            ScoutCard scoutCard = null;

            if(scoutCards.size() > 0)
            {
                for (ScoutCard storedScoutCard : scoutCards)
                {
                    if (storedScoutCard.getTeamId() == team.getId())
                    {
                        scoutCard = storedScoutCard;
                        break;
                    }
                }
            }

            final ScoutCard finalScoutCard = scoutCard;

            //no card available, show the add card button
            if (scoutCard == null)
            {
                viewHolder.viewMatchButton.setVisibility(View.GONE);
                viewHolder.addCardButton.setVisibility(View.VISIBLE);

                //Sends you to the scout card fragment
                viewHolder.addCardButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //add new card
                        context.changeFragment(ScoutCardFragment.newInstance(match, null, team), true);
                    }
                });
            }

            //card available, show the view match button
            else
            {
                viewHolder.viewMatchButton.setVisibility(View.VISIBLE);
                viewHolder.addCardButton.setVisibility(View.GONE);

                //Sends you to the scout card fragment
                viewHolder.viewMatchButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        //show match
                        context.changeFragment(ScoutCardFragment.newInstance(match, finalScoutCard, team), true);
                    }
                });
            }
        }

        //logic for going to the checklist frag
        else if(fragmentOnClick.equals(ChecklistFragment.class))
        {
            viewHolder.viewMatchButton.setText(context.getString(R.string.view_checklist));
            //Sends you to the checklist fragment
            viewHolder.viewMatchButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    context.changeFragment(ChecklistFragment.newInstance(team, matches.get(viewHolder.getAdapterPosition())), true);
                }
            });
        }

        //logic for going to the match overview
        else if(fragmentOnClick.equals(TeamListFragment.class))
        {
            viewHolder.viewMatchButton.setText(context.getString(R.string.view_match));
            //Sends you to the match overview fragment
            viewHolder.viewMatchButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    context.changeFragment(TeamListFragment.newInstance(matches.get(viewHolder.getAdapterPosition()), null), true);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return matches.size();
    }
}

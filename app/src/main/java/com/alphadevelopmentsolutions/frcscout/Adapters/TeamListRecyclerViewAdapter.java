package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.util.ArrayList;

public class TeamListRecyclerViewAdapter extends RecyclerView.Adapter<TeamListRecyclerViewAdapter.ViewHolder>
{

    private MainActivity context;

    private ArrayList<Team> teamList;

    public TeamListRecyclerViewAdapter(ArrayList<Team> teamList, MainActivity context)
    {
        this.context = context;
        this.teamList = teamList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView teamNameTextView;
        TextView teamNumberTextView;
        TextView teamLocationTextView;
        ImageView teamLogoImageView;
        TextView viewTeamButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            teamNameTextView = view.findViewById(R.id.TeamNameTextView);
            teamNumberTextView = view.findViewById(R.id.TeamNumberTextView);
            teamLocationTextView = view.findViewById(R.id.TeamLocationTextView);
            teamLogoImageView = view.findViewById(R.id.TeamLogoImageView);
            viewTeamButton = view.findViewById(R.id.ViewTeamButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_team_card, viewGroup, false);

        return new TeamListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TeamListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        Team team = teamList.get(viewHolder.getAdapterPosition());
        //Set the content on the card
        viewHolder.teamNameTextView.setText(team.getName());
        viewHolder.teamNumberTextView.setText(String.valueOf(team.getId()));
        viewHolder.teamLocationTextView.setText(team.getCity() + ", " + team.getStateProvince() + ", " + team.getCountry());

        //make sure there is a bitmap found when you try and update the image view
        Bitmap teamLogo = teamList.get(position).getImageBitmap();
        if(teamLogo != null) viewHolder.teamLogoImageView.setImageBitmap(teamLogo);


        //Sends you to the team fragment
        viewHolder.viewTeamButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
               context.changeFragment(TeamFragment.newInstance(new Gson().toJson(teamList.get(viewHolder.getAdapterPosition()))), true);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return teamList.size();
    }
}

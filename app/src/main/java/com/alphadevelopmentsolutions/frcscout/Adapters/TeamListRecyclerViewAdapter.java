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
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.R;

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
        ImageView teamLogoImageView;
        TextView viewTeamButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            teamNameTextView = view.findViewById(R.id.TeamNameTextView);
            teamNumberTextView = view.findViewById(R.id.TeamNumberTextView);
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
    public void onBindViewHolder(@NonNull TeamListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        //Set the content on the card
        viewHolder.teamNameTextView.setText(teamList.get(position).getName());
        viewHolder.teamNumberTextView.setText(String.valueOf(teamList.get(position).getId()));

        //make sure there is a bitmap found when you try and update the image view
        Bitmap teamLogo = teamList.get(position).getImageBitmap();
        if(teamLogo != null) viewHolder.teamLogoImageView.setImageBitmap(teamLogo);


        //Sends you to the team fragment
        viewHolder.viewTeamButton.setOnClickListener(new View.OnClickListener()
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
        return teamList.size();
    }
}

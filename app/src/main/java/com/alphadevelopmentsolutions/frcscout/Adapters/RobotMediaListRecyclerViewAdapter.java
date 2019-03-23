package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.R;

import java.util.ArrayList;

public class RobotMediaListRecyclerViewAdapter extends RecyclerView.Adapter<RobotMediaListRecyclerViewAdapter.ViewHolder>
{

    private Team team;

    private ArrayList<RobotMedia> robotMedia;

    private MainActivity context;

    public RobotMediaListRecyclerViewAdapter(Team team, ArrayList<RobotMedia> robotMedia, MainActivity context)
    {
        this.team = team;
        this.robotMedia = robotMedia;
        this.context = context;

    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView robotImageView;

        ViewHolder(@NonNull View view)
        {
            super(view);

            robotImageView = view.findViewById(R.id.RobotImageView);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.layout_robot_media, viewGroup, false);

        return new RobotMediaListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RobotMediaListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        //set scores
        viewHolder.robotImageView.setImageBitmap(((RobotMedia) robotMedia.get(viewHolder.getAdapterPosition())).getImageBitmap());
    }

    @Override
    public int getItemCount()
    {
        return robotMedia.size();
    }
}

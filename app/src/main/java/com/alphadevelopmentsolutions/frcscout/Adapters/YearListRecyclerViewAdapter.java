package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Years;
import com.alphadevelopmentsolutions.frcscout.Fragments.EventListFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.ScoutCardFragment;
import com.alphadevelopmentsolutions.frcscout.Fragments.TeamFragment;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;
import com.alphadevelopmentsolutions.frcscout.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class YearListRecyclerViewAdapter extends RecyclerView.Adapter<YearListRecyclerViewAdapter.ViewHolder>
{

    private MainActivity context;

    private ArrayList<Years> yearList;

    private SimpleDateFormat simpleDateFormat;


    public YearListRecyclerViewAdapter(@NonNull ArrayList<Years> yearList, @NonNull MainActivity context)
    {
        this.context = context;
        this.yearList = yearList;

        simpleDateFormat = new SimpleDateFormat("MMM d, yyyy");
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView yearTitleTextView;
        TextView yearDateTextView;
        ImageView yearLogoImageView;
        Button viewButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            yearTitleTextView = view.findViewById(R.id.YearTitleTextView);
            yearDateTextView = view.findViewById(R.id.YearDateTextView);
            yearLogoImageView = view.findViewById(R.id.YearLogoImageView);
            viewButton = view.findViewById(R.id.ViewButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view  = LayoutInflater.from(context).inflate(R.layout.layout_card_year, viewGroup, false);

        return new YearListRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final YearListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {
        final Years year = yearList.get(viewHolder.getAdapterPosition());

        //Set the content on the card
        viewHolder.yearTitleTextView.setText(year.toString());
        viewHolder.yearDateTextView.setText(String.format("%s - %s", simpleDateFormat.format(year.getStartDate().getTime()), simpleDateFormat.format(year.getEndDate().getTime())));

        //load the photo if the file exists
        if(!year.getImageUri().equals(""))
            Picasso.get()
                    .load(Uri.fromFile(new File(year.getImageUri())))
                    .fit()
                    .centerCrop()
                    .into(viewHolder.yearLogoImageView);

        else
            viewHolder.yearLogoImageView.setImageDrawable(context.getDrawable(R.drawable.frc_logo));



        //Sends you to the event list fragment
        viewHolder.viewButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                context.setPreference(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, yearList.get(viewHolder.getAdapterPosition()).getServerId());
                context.changeFragment(EventListFragment.newInstance(yearList.get(viewHolder.getAdapterPosition())), false);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return yearList.size();
    }
}

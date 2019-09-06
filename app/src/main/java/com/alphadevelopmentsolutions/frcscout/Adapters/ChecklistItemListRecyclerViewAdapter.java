package com.alphadevelopmentsolutions.frcscout.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity;
import com.alphadevelopmentsolutions.frcscout.Classes.ChecklistItem;
import com.alphadevelopmentsolutions.frcscout.Classes.ChecklistItemResult;
import com.alphadevelopmentsolutions.frcscout.Classes.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.User;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Status;
import com.alphadevelopmentsolutions.frcscout.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;

public class ChecklistItemListRecyclerViewAdapter extends RecyclerView.Adapter<ChecklistItemListRecyclerViewAdapter.ViewHolder>
{

    private ArrayList<ChecklistItem> checklistItems;
    private ArrayList<User> users;

    private ArrayAdapter<String> userNamesAdapter;

    private MainActivity context;

    private Event event;

    private Team team;

    private Match match;

    private Gson gson;


    public ChecklistItemListRecyclerViewAdapter(Event event, Match match, Team team, ArrayList<ChecklistItem> checklistItems, ArrayList<User> users, MainActivity context)
    {
        this.checklistItems = checklistItems;
        this.users = users;
        this.context = context;
        this.event = event;
        this.team = team;
        this.match = match;

        gson = new Gson();

        ArrayList<String> userNames = new ArrayList<>();

        for(User user : users)
        {
            userNames.add(user.toString());
        }

        userNamesAdapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, userNames);

    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {

        TextView titleTextView;
        TextView statusTextView;
        TextView descriptionTextView;

        AutoCompleteTextView completedByAutoCompleteTextView;

        Button toggleStatusButton;

        ViewHolder(@NonNull View view)
        {
            super(view);

            titleTextView = view.findViewById(R.id.TitleTextView);

            statusTextView = view.findViewById(R.id.StatusTextView);
            descriptionTextView = view.findViewById(R.id.DescriptionTextView);

            completedByAutoCompleteTextView = view.findViewById(R.id.CompletedByAutoCompleteTextView);

            toggleStatusButton = view.findViewById(R.id.ToggleStatusButton);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        //Inflate the event layout for the each item in the list
        View view = LayoutInflater.from(context).inflate(R.layout.layout_card_checklist_item, viewGroup, false);



        ChecklistItemListRecyclerViewAdapter.ViewHolder viewHolder = new ChecklistItemListRecyclerViewAdapter.ViewHolder(view);
        viewHolder.completedByAutoCompleteTextView.setAdapter(userNamesAdapter);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ChecklistItemListRecyclerViewAdapter.ViewHolder viewHolder, int position)
    {

        final ChecklistItem checklistItem = checklistItems.get(viewHolder.getAdapterPosition());

        ChecklistItemResult checklistItemResult = null;

        //filter by match id
        for(ChecklistItemResult storedChecklistItemResult : checklistItem.getResults(context.getDatabase(), false))
        {
            if(storedChecklistItemResult.getMatchId().equals(match.getKey()))
            {
                checklistItemResult = storedChecklistItemResult;
                break;
            }
        }

        //no result found, default a new one
        if(checklistItemResult == null)
            checklistItemResult = new ChecklistItemResult(
                    -1,
                    checklistItem.getServerId(),
                    match.getKey(),

                    Status.INCOMPLETE,

                    "",
                    new Date(),

                    true);

        final ChecklistItemResult finalChecklistItemResult = checklistItemResult;

        viewHolder.titleTextView.setText(checklistItem.getTitle());
        viewHolder.descriptionTextView.setText(checklistItem.getDescription());
        viewHolder.statusTextView.setText(finalChecklistItemResult.getStatus());

        viewHolder.completedByAutoCompleteTextView.setText(finalChecklistItemResult.getCompletedBy());

        if(finalChecklistItemResult.getStatus().equals(Status.INCOMPLETE))
        {
            viewHolder.statusTextView.setText(Status.INCOMPLETE);
            viewHolder.statusTextView.setTextColor(context.getResources().getColor(R.color.bad));
            viewHolder.toggleStatusButton.setText(R.string.mark_complete);
        }

        else
        {
            viewHolder.statusTextView.setText(Status.COMPLETE);
            viewHolder.statusTextView.setTextColor(context.getResources().getColor(R.color.good));
            viewHolder.toggleStatusButton.setText(R.string.mark_incomplete);
        }

        //disable the editing of non draft results
        if(finalChecklistItemResult.isDraft())
        {
            //update the status and save the item to the database
            viewHolder.toggleStatusButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (!viewHolder.completedByAutoCompleteTextView.getText().toString().equals(""))
                    {
                        if (finalChecklistItemResult.getStatus().equals(Status.COMPLETE))
                        {
                            finalChecklistItemResult.setStatus(Status.INCOMPLETE);
                            viewHolder.statusTextView.setText(Status.INCOMPLETE);
                            viewHolder.statusTextView.setTextColor(context.getResources().getColor(R.color.bad));
                            viewHolder.toggleStatusButton.setText(R.string.mark_complete);
                        } else
                        {
                            finalChecklistItemResult.setStatus(Status.COMPLETE);
                            viewHolder.statusTextView.setText(Status.COMPLETE);
                            viewHolder.statusTextView.setTextColor(context.getResources().getColor(R.color.good));
                            viewHolder.toggleStatusButton.setText(R.string.mark_incomplete);
                        }

                        finalChecklistItemResult.setCompletedBy(viewHolder.completedByAutoCompleteTextView.getText().toString());
                        finalChecklistItemResult.save(context.getDatabase());
                    } else
                        context.showSnackbar("Please enter completed by.");
                }
            });
        }
        else
        {
            viewHolder.completedByAutoCompleteTextView.setEnabled(false);
            viewHolder.toggleStatusButton.setEnabled(false);
        }
    }

    @Override
    public int getItemCount()
    {
        return checklistItems.size();
    }
}

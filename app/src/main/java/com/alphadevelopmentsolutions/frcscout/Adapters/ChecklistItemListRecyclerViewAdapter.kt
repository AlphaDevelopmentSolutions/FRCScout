package com.alphadevelopmentsolutions.frcscout.Adapters

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.User
import com.alphadevelopmentsolutions.frcscout.Interfaces.Status
import com.alphadevelopmentsolutions.frcscout.R

import java.util.ArrayList
import java.util.Date

internal class ChecklistItemListRecyclerViewAdapter(private val match: Match, private val checklistItems: ArrayList<ChecklistItem>, private val context: MainActivity) : RecyclerView.Adapter<ChecklistItemListRecyclerViewAdapter.ViewHolder>()
{

    private val userNamesAdapter: ArrayAdapter<String>

    init
    {


        val userNames = ArrayList<String>()

        //get all users
        for (user in User.getUsers(null, context.getDatabase())!!)
        {
            userNames.add(user.toString())
        }

        userNamesAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, userNames)

    }

    internal class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
    {

        var titleTextView: TextView
        var statusTextView: TextView
        var descriptionTextView: TextView

        var completedByAutoCompleteTextView: AutoCompleteTextView

        var toggleStatusButton: Button

        init
        {

            titleTextView = view.findViewById(R.id.TitleTextView)

            statusTextView = view.findViewById(R.id.StatusTextView)
            descriptionTextView = view.findViewById(R.id.DescriptionTextView)

            completedByAutoCompleteTextView = view.findViewById(R.id.CompletedByAutoCompleteTextView)

            toggleStatusButton = view.findViewById(R.id.ToggleStatusButton)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the event layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_checklist_item, viewGroup, false)


        val viewHolder = ChecklistItemListRecyclerViewAdapter.ViewHolder(view)
        viewHolder.completedByAutoCompleteTextView.setAdapter(userNamesAdapter)

        return viewHolder
    }

    override fun onBindViewHolder(viewHolder: ChecklistItemListRecyclerViewAdapter.ViewHolder, position: Int)
    {

        val checklistItem = checklistItems[viewHolder.adapterPosition]

        var checklistItemResult: ChecklistItemResult? = null

        //filter by match id
        for (storedChecklistItemResult in checklistItem.getResults(null, false, context.getDatabase())!!)
        {
            if (storedChecklistItemResult.matchId == match.key)
            {
                checklistItemResult = storedChecklistItemResult
                break
            }
        }

        //no result found, default a new one
        if (checklistItemResult == null)
            checklistItemResult = ChecklistItemResult(
                    -1,
                    checklistItem.serverId,
                    match.key,

                    Status.INCOMPLETE,

                    "",
                    Date(),

                    true)

        val finalChecklistItemResult = checklistItemResult

        viewHolder.titleTextView.text = checklistItem.title
        viewHolder.descriptionTextView.text = checklistItem.description
        viewHolder.statusTextView.text = finalChecklistItemResult.status

        viewHolder.completedByAutoCompleteTextView.setText(finalChecklistItemResult.completedBy)

        if (finalChecklistItemResult.status == Status.INCOMPLETE)
        {
            viewHolder.statusTextView.text = Status.INCOMPLETE
            viewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.bad))
            viewHolder.toggleStatusButton.setText(R.string.mark_complete)
        } else
        {
            viewHolder.statusTextView.text = Status.COMPLETE
            viewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.good))
            viewHolder.toggleStatusButton.setText(R.string.mark_incomplete)
        }

        //disable the editing of non draft results
        if (finalChecklistItemResult.isDraft)
        {
            //update the status and save the item to the database
            viewHolder.toggleStatusButton.setOnClickListener {
                if (viewHolder.completedByAutoCompleteTextView.text.toString() != "")
                {
                    if (finalChecklistItemResult.status == Status.COMPLETE)
                    {
                        finalChecklistItemResult.status = Status.INCOMPLETE
                        viewHolder.statusTextView.text = Status.INCOMPLETE
                        viewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.bad))
                        viewHolder.toggleStatusButton.setText(R.string.mark_complete)
                    } else
                    {
                        finalChecklistItemResult.status = Status.COMPLETE
                        viewHolder.statusTextView.text = Status.COMPLETE
                        viewHolder.statusTextView.setTextColor(ContextCompat.getColor(context, R.color.good))
                        viewHolder.toggleStatusButton.setText(R.string.mark_incomplete)
                    }

                    finalChecklistItemResult.completedBy = viewHolder.completedByAutoCompleteTextView.text.toString()
                    finalChecklistItemResult.save(context.getDatabase())
                } else
                    context.showSnackbar("Please enter completed by.")
            }
        } else
        {
            viewHolder.completedByAutoCompleteTextView.isEnabled = false
            viewHolder.toggleStatusButton.isEnabled = false
        }
    }

    override fun getItemCount(): Int
    {
        return checklistItems.size
    }
}

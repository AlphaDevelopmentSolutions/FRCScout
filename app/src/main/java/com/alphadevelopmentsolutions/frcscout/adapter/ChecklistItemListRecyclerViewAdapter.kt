package com.alphadevelopmentsolutions.frcscout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.classes.table.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.classes.table.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.User
import com.alphadevelopmentsolutions.frcscout.interfaces.Status
import com.alphadevelopmentsolutions.frcscout.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.layout_card_checklist_item.view.*
import java.util.*

internal class ChecklistItemListRecyclerViewAdapter(private val match: Match, private val checklistItems: ArrayList<ChecklistItem>, private val context: MainActivity) : RecyclerView.Adapter<ChecklistItemListRecyclerViewAdapter.ViewHolder>()
{

    private val userNamesAdapter: ArrayAdapter<String>

    init
    {
        val userNames = ArrayList<String>()

        //get all users
        for (user in User.getObjects(null, context.database)!!)
        {
            userNames.add(user.toString())
        }

        userNamesAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, userNames)

    }

    internal class ViewHolder(val view: View, context: MainActivity) : RecyclerView.ViewHolder(view)
    {
        init
        {
            view.CompleteButton.setTextColor(context.primaryColor)
            (view.CompleteButton as MaterialButton).rippleColor = context.buttonRipple

            view.IncompleteButton.setTextColor(context.primaryColor)
            (view.IncompleteButton as MaterialButton).rippleColor = context.buttonRipple

            view.CompletedByAutoCompleteTextView.backgroundTintList = context.editTextBackground
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder
    {
        //Inflate the eventId layout for the each item in the list
        val view = LayoutInflater.from(context).inflate(R.layout.layout_card_checklist_item, viewGroup, false)

        view.CompletedByAutoCompleteTextView.setAdapter(userNamesAdapter)

        return ViewHolder(view, context)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int)
    {
        val checklistItem = checklistItems[viewHolder.adapterPosition]

        var checklistItemResult: ChecklistItemResult? = null

        //filter by matchId id
        for (storedChecklistItemResult in checklistItem.getResults(null, false, context.database)!!)
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

                    Status.UNSET,

                    "",
                    Date(),

                    true)

        val finalChecklistItemResult = checklistItemResult

        viewHolder.view.TitleTextView.text = checklistItem.title
        viewHolder.view.DescriptionTextView.text = checklistItem.description
        viewHolder.view.StatusTextView.text = finalChecklistItemResult.status

        viewHolder.view.CompletedByAutoCompleteTextView.setText(finalChecklistItemResult.completedBy)

        when(finalChecklistItemResult.status)
        {
            Status.INCOMPLETE ->
            {
                viewHolder.view.StatusTextView.text = Status.INCOMPLETE
                viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.bad))
            }

            Status.COMPLETE ->
            {
                viewHolder.view.StatusTextView.text = Status.COMPLETE
                viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.good))
            }

            Status.UNSET ->
            {
                viewHolder.view.StatusTextView.text = Status.UNSET
                viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            }
        }

        //disable the editing of non draft results
        if (finalChecklistItemResult.isDraft)
        {
            viewHolder.view.IncompleteButton.setOnClickListener{
                if (viewHolder.view.CompletedByAutoCompleteTextView.text.toString() != "")
                {
                    viewHolder.view.StatusTextView.text = Status.INCOMPLETE
                    viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.bad))

                    finalChecklistItemResult.status = Status.INCOMPLETE
                    finalChecklistItemResult.completedBy = viewHolder.view.CompletedByAutoCompleteTextView.text.toString()
                    finalChecklistItemResult.save(context.database)

                } else
                    context.showSnackbar("Please enter completed by.")
            }

            viewHolder.view.CompleteButton.setOnClickListener{
                if (viewHolder.view.CompletedByAutoCompleteTextView.text.toString() != "")
                {
                    viewHolder.view.StatusTextView.text = Status.COMPLETE
                    viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.good))

                    finalChecklistItemResult.status = Status.COMPLETE
                    finalChecklistItemResult.completedBy = viewHolder.view.CompletedByAutoCompleteTextView.text.toString()
                    finalChecklistItemResult.save(context.database)

                } else
                    context.showSnackbar("Please enter completed by.")
            }
        }
        else
        {
            viewHolder.view.CompletedByAutoCompleteTextView.isEnabled = false
            viewHolder.view.CompleteButton.isEnabled = false
            viewHolder.view.IncompleteButton.isEnabled = false
        }
    }

    override fun getItemCount(): Int
    {
        return checklistItems.size
    }
}
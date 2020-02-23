package com.alphadevelopmentsolutions.frcscout.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.account.User
import com.alphadevelopmentsolutions.frcscout.enums.Status
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.VMProvider
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.extension.init
import com.alphadevelopmentsolutions.frcscout.interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.view.database.ChecklistItemDatabaseView
import com.google.android.material.button.MaterialButton
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.layout_card_checklist_item.view.*
import java.util.*

internal class ChecklistItemListRecyclerViewAdapter(private val matchId: UUID, private val checklistItems: MutableList<ChecklistItemDatabaseView>, private val context: MainActivity) : RecyclerView.Adapter<ChecklistItemListRecyclerViewAdapter.ViewHolder>()
{

    private lateinit var userNamesAdapter: ArrayAdapter<User>

    private val vmProvider by lazy {
        VMProvider.getInstance(context)
    }

    private val disposable: Disposable

    init
    {
        disposable =
                VMProvider.getInstance(context).userViewModel.objs.init()
                    .subscribe(
                            { users ->
                                userNamesAdapter = ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, users)
                            },
                            {
                                AppLog.error(it)
                            }
                    )
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {

        disposable.dispose()

        super.onViewDetachedFromWindow(holder)
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
        val checklistItemView = checklistItems[viewHolder.adapterPosition]

        viewHolder.view.TitleTextView.text = checklistItemView.checklistItem.toString()
        viewHolder.view.DescriptionTextView.text = checklistItemView.checklistItem.description
        viewHolder.view.StatusTextView.text = checklistItemView.checklistItemResult?.checklistItemResult?.status?.toString() ?: Status.UNSET.toString()

        checklistItemView.checklistItemResult?.completedBy?.let { completedByUser ->
            viewHolder.view.CompletedByAutoCompleteTextView.setText(completedByUser.toString())
        }

        checklistItemView.checklistItemResult?.checklistItemResult?.status?.let { status ->

            viewHolder.view.StatusTextView.text = status.toString()

            when(status)
            {
                Status.INCOMPLETE -> viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.bad))
                Status.COMPLETE -> viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.good))
                Status.UNSET -> viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.text_secondary))
            }
        }

        //disable the editing of non draft results
        if (checklistItemView.checklistItemResult?.checklistItemResult?.isDraft == true)
        {
            viewHolder.view.IncompleteButton.setOnClickListener{
                if (viewHolder.view.CompletedByAutoCompleteTextView.text.toString() != "")
                {
                    viewHolder.view.StatusTextView.text = Status.INCOMPLETE.toString()
                    viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.bad))

                    checklistItems[viewHolder.adapterPosition].let { checklistItemView ->
                        checklistItemView.checklistItemResult?.checklistItemResult?.let {
                            vmProvider.checklistItemResultViewModel.insert(it)
                        }
                        ?:
                        let {
                            vmProvider.checklistItemResultViewModel.insert(
                                    ChecklistItemResult(
                                            checklistItemView.checklistItem.id,
                                            matchId,
                                            Status.INCOMPLETE,
                                            Table.DEFAULT_UUID,
                                            Table.DEFAULT_DATE
                                    )
                            )
                        }
                    }
                } else
                    context.showSnackbar("Please enter completed by.")
            }

            viewHolder.view.CompleteButton.setOnClickListener{
                if (viewHolder.view.CompletedByAutoCompleteTextView.text.toString() != "")
                {
                    viewHolder.view.StatusTextView.text = Status.COMPLETE.toString()
                    viewHolder.view.StatusTextView.setTextColor(ContextCompat.getColor(context, R.color.good))

                    checklistItems[viewHolder.adapterPosition].let { checklistItemView ->
                        checklistItemView.checklistItemResult?.checklistItemResult?.let {
                            vmProvider.checklistItemResultViewModel.insert(it)
                        }
                                ?:
                                let {
                                    vmProvider.checklistItemResultViewModel.insert(
                                            ChecklistItemResult(
                                                    checklistItemView.checklistItem.id,
                                                    matchId,
                                                    Status.COMPLETE,
                                                    Table.DEFAULT_UUID,
                                                    Table.DEFAULT_DATE
                                            )
                                    )
                                }
                    }

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

package com.alphadevelopmentsolutions.frcscout.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import kotlinx.android.synthetic.main.layout_selectable_item.view.*

internal class SelectDialogRecyclerViewAdapter(
    private val context: Context,
    private val items: List<Any>,
    private val onItemSelectedListener: OnItemSelectedListener
) : RecyclerView.Adapter<SelectDialogRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemTextView: TextView = view.item_textview
        val itemLayout: LinearLayout = view.item_layout
    }

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            layoutInflater.inflate(
                R.layout.layout_selectable_item,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTextView.text = items[holder.adapterPosition].toString()
        holder.itemLayout.setOnClickListener {
            onItemSelectedListener.onItemSelected(items[holder.adapterPosition])
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
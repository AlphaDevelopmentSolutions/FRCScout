package com.alphadevelopmentsolutions.frcscout.ui.fragments.matches

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardMatchBinding
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardTeamBinding
import com.alphadevelopmentsolutions.frcscout.singletons.GlideInstance
import kotlinx.android.synthetic.main.layout_selectable_item.view.*

class MatchListRecyclerViewAdapter(
    private val context: Context,
    private val matchList: MutableList<MatchDatabaseView>,
    private val navController: NavController
) : RecyclerView.Adapter<MatchListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutCardMatchBinding) : RecyclerView.ViewHolder(binding.root)

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutCardMatchBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val match = matchList[holder.adapterPosition]

        holder.binding.matchDatabaseView = match
    }

    override fun getItemCount(): Int {
        return matchList.size
    }
}
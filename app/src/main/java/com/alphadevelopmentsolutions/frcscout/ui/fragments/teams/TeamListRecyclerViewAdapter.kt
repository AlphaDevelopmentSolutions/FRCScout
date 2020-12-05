package com.alphadevelopmentsolutions.frcscout.ui.fragments.teams

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardTeamBinding
import com.alphadevelopmentsolutions.frcscout.singletons.GlideInstance

class TeamListRecyclerViewAdapter(
    private val context: Context,
    private val teamList: MutableList<Team>,
    private val navController: NavController
) : RecyclerView.Adapter<TeamListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutCardTeamBinding) : RecyclerView.ViewHolder(binding.root)

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutCardTeamBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val team = teamList[holder.adapterPosition]

        holder.binding.team = team

        holder.binding.viewButton.setOnClickListener {
            navController.navigate(
                TeamListFragmentDirections.actionTeamListFragmentDestinationToTeamFragmentDestination(teamList[holder.adapterPosition])
            )
        }
    }

    override fun getItemCount(): Int {
        return teamList.size
    }
}
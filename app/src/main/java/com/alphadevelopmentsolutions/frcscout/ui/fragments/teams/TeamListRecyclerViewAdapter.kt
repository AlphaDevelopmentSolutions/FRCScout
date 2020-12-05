package com.alphadevelopmentsolutions.frcscout.ui.fragments.teams

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.callbacks.OnItemSelectedListener
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardTeamBinding
import com.alphadevelopmentsolutions.frcscout.singletons.GlideInstance
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_selectable_item.view.*

class TeamListRecyclerViewAdapter(
    private val context: Context,
    private val teamList: MutableList<Team>,
    private val navController: NavController
) : RecyclerView.Adapter<TeamListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val teamCardBinding: LayoutCardTeamBinding) : RecyclerView.ViewHolder(teamCardBinding.root)

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

        holder.teamCardBinding.team = team

        holder.teamCardBinding.viewButton.setOnClickListener {
            navController.navigate(
                TeamListFragmentDirections.actionTeamListFragmentDestinationToSettingsFragmentDestination() // TODO: Change to team page
            )
        }

        team.avatarUri?.let { uri ->
            GlideInstance.loadPhoto(
                context,
                holder.teamCardBinding.logoImageView,
                uri
            )
        }
    }

    override fun getItemCount(): Int {
        return teamList.size
    }
}
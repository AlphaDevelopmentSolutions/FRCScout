package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardRobotMediaBinding

class RobotMediaListRecyclerViewAdapter(
    private val context: Context,
    private val robotMediaList: MutableList<RobotMedia>,
    private val navController: NavController
) : RecyclerView.Adapter<RobotMediaListRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(val binding: LayoutCardRobotMediaBinding) : RecyclerView.ViewHolder(binding.root)

    private val layoutInflater by lazy {
        LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            LayoutCardRobotMediaBinding.inflate(
                layoutInflater,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val robotMedia = robotMediaList[holder.adapterPosition]

        holder.binding.robotMedia = robotMedia
        holder.binding.handlers = this
    }

    override fun getItemCount(): Int {
        return robotMediaList.size
    }

    fun navigateToMedia(robotMedia: RobotMedia) {
//        navController.navigate(MatchListFragmentDirections.actionMatchListFragmentDestinationToTeamFragmentDestination(team))
    }

    fun deleteMedia(robotMedia: RobotMedia) {

    }
}
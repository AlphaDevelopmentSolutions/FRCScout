package com.alphadevelopmentsolutions.frcscout.ui.fragments.matchlist

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.alphadevelopmentsolutions.frcscout.FRCScoutApplication
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.databinding.LayoutCardMatchBinding
import com.alphadevelopmentsolutions.frcscout.enums.FragmentTag
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.ui.fragments.team.TeamFragmentDirections
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
        holder.binding.handlers = this

        holder.binding.viewMatchButton.apply {
            visibility = View.VISIBLE
            setOnClickListener {
                if (this@MatchListRecyclerViewAdapter.context is FRCScoutApplication) {
                    val activity = this@MatchListRecyclerViewAdapter.context.activeActivity

                    if (activity is MainActivity) {
                        when (activity.getCurrentFragment()?.TAG) {
                            FragmentTag.MATCH_LIST -> {
                                navController.navigate(
                                    MatchListFragmentDirections.actionMatchListFragmentDestinationToMatchFragment(match)
                                )
                            }

                            FragmentTag.TEAM -> {
                                navController.navigate(
                                    TeamFragmentDirections.actionTeamFragmentDestinationToMatchFragmentDestination(match)
                                )
                            }

                            // Ignore
                            else -> { }
                        }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    fun navigateToTeam(team: Team) {
        navController.navigate(MatchListFragmentDirections.actionMatchListFragmentDestinationToTeamFragmentDestination(team))
    }
}
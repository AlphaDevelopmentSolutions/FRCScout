package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.fragments.team.TeamFragmentDirections

class RobotMediaListViewModel(
    private val context: MainActivity,
    private val lifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val team: Team?
) : AndroidViewModel(context.application) {

    val robotMediaListRecyclerViewAdapter =
        RobotMediaListRecyclerViewAdapter(
            context,
            navController
        )

    init {
        KeyStore.getInstance(context).selectedEvent?.let { event ->
            RepositoryProvider.getInstance(context).robotMediaRepository.getForTeam(event, team).observe<MutableList<RobotMedia>>(
                lifecycleOwner,
                {
                    robotMediaListRecyclerViewAdapter.setData(it)
                }
            )
        }
    }

    fun createMedia() {
        team?.let {
            navController.navigate(
                TeamFragmentDirections.actionTeamFragmentDestinationToRobotMediaFragmentDestination(null, it)
            )
        }
    }
}
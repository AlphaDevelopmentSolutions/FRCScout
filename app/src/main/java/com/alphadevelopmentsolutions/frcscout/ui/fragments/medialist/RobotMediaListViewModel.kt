package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import com.alphadevelopmentsolutions.frcscout.FRCScoutApplication
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.extensions.runOnUiThread
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.fragments.team.TeamFragmentDirections
import com.alphadevelopmentsolutions.frcscout.ui.fragments.teams.TeamListFragmentDirections
import kotlinx.coroutines.launch

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
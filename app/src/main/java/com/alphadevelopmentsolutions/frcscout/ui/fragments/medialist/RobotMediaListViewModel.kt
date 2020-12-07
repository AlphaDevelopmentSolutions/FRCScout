package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.alphadevelopmentsolutions.frcscout.ui.fragments.team.TeamFragmentDirections
import com.alphadevelopmentsolutions.frcscout.ui.fragments.teams.TeamListFragmentDirections

class RobotMediaListViewModel(
    application: Application,
    private val lifecycleOwner: LifecycleOwner,
    private val navController: NavController,
    private val team: Team?
) : AndroidViewModel(application) {

    private val context = application

    private var robotMediaList: MutableList<RobotMedia> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)

            robotMediaListRecyclerViewAdapter.notifyDataSetChanged()
        }

    val robotMediaListRecyclerViewAdapter =
        RobotMediaListRecyclerViewAdapter(
            context,
            robotMediaList,
            navController
        )


    init {
        KeyStore.getInstance(context).selectedEvent?.let { event ->

            RepositoryProvider.getInstance(context).robotMediaRepository.getForTeam(event, team).observe<MutableList<RobotMedia>>(
                lifecycleOwner,
                {
                    robotMediaList = it
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
package com.alphadevelopmentsolutions.frcscout.ui.fragments.teamlist

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore

class TeamListViewModel(
    application: Application,
    private val lifecycleOwner: LifecycleOwner,
    navController: NavController
) : AndroidViewModel(application) {

    val event: Event?

    private val context = application

    private var teamList: MutableList<Team> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)

            teamListRecyclerViewAdapter.notifyDataSetChanged()
        }

    val teamListRecyclerViewAdapter =
        TeamListRecyclerViewAdapter(
            context,
            teamList,
            navController
        )


    init {
        event = KeyStore.getInstance(context).selectedEvent
        event?.let { event ->
            RepositoryProvider.getInstance(context).teamRepository.getAtEvent(event).observe<MutableList<Team>>(
                lifecycleOwner,
                {
                    teamList = it
                }
            )
        }
    }
}
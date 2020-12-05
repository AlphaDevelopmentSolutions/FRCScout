package com.alphadevelopmentsolutions.frcscout.ui.fragments.matches

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore

class MatchListViewModel(
    application: Application,
    private val lifecycleOwner: LifecycleOwner,
    navController: NavController,
    private val team: Team?
) : AndroidViewModel(application) {

    private val context = application

    private var matchList: MutableList<MatchDatabaseView> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)

            matchListRecyclerViewAdapter.notifyDataSetChanged()
        }

    val matchListRecyclerViewAdapter =
        MatchListRecyclerViewAdapter(
            context,
            matchList,
            navController
        )


    init {
        KeyStore.getInstance(context).selectedEvent?.let { event ->

            RepositoryProvider.getInstance(context).matchRepository.getForEvent(event, team).observe<MutableList<MatchDatabaseView>>(
                lifecycleOwner,
                {
                    matchList = it
                }
            )
        }
    }
}
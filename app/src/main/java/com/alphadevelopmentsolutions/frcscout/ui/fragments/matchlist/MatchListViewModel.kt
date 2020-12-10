package com.alphadevelopmentsolutions.frcscout.ui.fragments.matchlist

import android.app.Application
import android.os.Looper
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.FRCScoutApplication
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.extensions.runOnUiThread
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

            runOnUiThread {
                matchListRecyclerViewAdapter.notifyDataSetChanged()
            }
        }

    val matchListRecyclerViewAdapter =
        MatchListRecyclerViewAdapter(
            context,
            matchList,
            navController
        )


    init {
        KeyStore.getInstance(context).selectedEvent?.let { event ->

            launchIO(lifecycleOwner) {
                matchList = RepositoryProvider.getInstance(context).matchRepository.getForEvent(event, team)
            }
        }
    }
}
package com.alphadevelopmentsolutions.frcscout.ui.fragments.match

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

class MatchDetailViewModel(
    application: Application,
    private val navController: NavController,
    val team1: Team,
    val team2: Team,
    val team3: Team
) : AndroidViewModel(application) {

    /**
     * Navigate to the team fragment
     */
    fun onViewTeamClicked(team: Team) {
        val d = 0
    }
}
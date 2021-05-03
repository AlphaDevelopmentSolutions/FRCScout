package com.alphadevelopmentsolutions.frcscout.ui.fragments.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class MatchDetailViewModelFactory(
    val activity: MainActivity,
    val navController: NavController,
    val team1: Team,
    val team2: Team,
    val team3: Team
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MatchDetailViewModel(activity.application, navController, team1, team2, team3) as T
}
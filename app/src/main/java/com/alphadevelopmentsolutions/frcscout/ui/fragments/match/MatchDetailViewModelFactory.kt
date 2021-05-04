package com.alphadevelopmentsolutions.frcscout.ui.fragments.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.ui.fragments.matchlist.MatchDatabaseView

class MatchDetailViewModelFactory(
    private val activity: MainActivity,
    private val navController: NavController,
    private val matchDatabaseView: MatchDatabaseView,
    private val allianceColor: AllianceColor
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MatchDetailViewModel(activity.application, navController, matchDatabaseView, allianceColor) as T
}
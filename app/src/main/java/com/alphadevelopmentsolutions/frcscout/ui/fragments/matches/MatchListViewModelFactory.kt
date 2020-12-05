package com.alphadevelopmentsolutions.frcscout.ui.fragments.matches

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class MatchListViewModelFactory(
    val activity: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController,
    val team: Team?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MatchListViewModel(activity.application, lifecycleOwner, navController, team) as T
}
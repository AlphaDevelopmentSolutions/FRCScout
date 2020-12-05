package com.alphadevelopmentsolutions.frcscout.ui.fragments.teams

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity

class TeamListViewModelFactory(
    val activity: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        TeamListViewModel(activity.application, lifecycleOwner, navController) as T
}
package com.alphadevelopmentsolutions.frcscout.ui.fragments.medialist

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class RobotMediaListViewModelFactory(
    val activity: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController,
    val team: Team?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RobotMediaListViewModel(activity, lifecycleOwner, navController, team) as T
}
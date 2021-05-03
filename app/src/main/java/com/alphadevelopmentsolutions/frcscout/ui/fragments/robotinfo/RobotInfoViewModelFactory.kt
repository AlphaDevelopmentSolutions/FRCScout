package com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class RobotInfoViewModelFactory(
    val activity: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    val team: Team
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RobotInfoViewModel(activity.application, lifecycleOwner, team) as T
}
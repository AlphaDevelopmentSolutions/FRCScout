package com.alphadevelopmentsolutions.frcscout.ui.fragments.media

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class RobotMediaViewModelFactory(
    val activity: MainActivity,
    val media: RobotMedia?,
    val team: Team?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RobotMediaViewModel(activity.application, media, team) as T
}
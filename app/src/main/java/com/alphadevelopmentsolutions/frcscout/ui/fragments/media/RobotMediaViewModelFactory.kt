package com.alphadevelopmentsolutions.frcscout.ui.fragments.media

import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class RobotMediaViewModelFactory(
    val activity: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    val view: View?,
    val media: RobotMedia?,
    val team: Team?
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        RobotMediaViewModel(activity, lifecycleOwner, view, media, team) as T
}
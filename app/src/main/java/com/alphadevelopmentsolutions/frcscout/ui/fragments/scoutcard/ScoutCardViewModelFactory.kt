package com.alphadevelopmentsolutions.frcscout.ui.fragments.scoutcard

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class ScoutCardViewModelFactory(
    val activity: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    val match: Match,
    val team: Team
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        ScoutCardViewModel(activity.application, lifecycleOwner, match, team) as T
}
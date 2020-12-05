package com.alphadevelopmentsolutions.frcscout.ui.fragments.matches

import android.app.Application
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity

class MatchListViewModelFactory(
    val activity: MainActivity,
    val lifecycleOwner: LifecycleOwner,
    val navController: NavController
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        MatchListViewModel(activity.application, lifecycleOwner, navController) as T
}
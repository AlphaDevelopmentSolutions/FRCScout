package com.alphadevelopmentsolutions.frcscout.ui.fragments.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alphadevelopmentsolutions.frcscout.ui.MainActivity

class SettingsViewModelFactory(
    val activity: MainActivity
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        SettingsViewModel(activity) as T
}
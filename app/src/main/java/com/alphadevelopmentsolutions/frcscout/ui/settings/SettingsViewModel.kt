package com.alphadevelopmentsolutions.frcscout.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.alphadevelopmentsolutions.frcscout.R

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    val version = String.format(application.getString(R.string.app_version), BuildConfig.VERSION_NAME)
    val buildNumber = String.format(application.getString(R.string.build_number), BuildConfig.VERSION_CODE)
    val buildType = String.format(application.getString(R.string.build_type), BuildConfig.BUILD_TYPE)
}
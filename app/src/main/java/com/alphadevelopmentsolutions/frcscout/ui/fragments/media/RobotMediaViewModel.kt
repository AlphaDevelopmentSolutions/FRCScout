package com.alphadevelopmentsolutions.frcscout.ui.fragments.media

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore

class RobotMediaViewModel(
    application: Application,
    val media: RobotMedia?,
    team: Team?
) : AndroidViewModel(application) {

    private val context = application

    init {
        if (media == null && team != null) {
            createMedia(team)
        }
    }

    private fun createMedia(team: Team) {

    }
}
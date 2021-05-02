package com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore

class RobotInfoViewModel(
    application: Application,
    lifecycleOwner: LifecycleOwner,
    val team: Team
) : AndroidViewModel(application) {
    private val context = application

    var robotInfoKeyViewList: ObservableField<List<RobotInfoKeyView>> = ObservableField(listOf())

    var event: Event? = null

    private val keyStore by lazy {
        KeyStore.getInstance(context)
    }

    init {
        val year = keyStore.selectedYear
        val userTeamAccount = Account.getInstance(context)?.userTeamAccount
        event = keyStore.selectedEvent


        if (event != null && year != null && userTeamAccount != null) {
            launchIO(lifecycleOwner) {
                robotInfoKeyViewList.set(RepositoryProvider.getInstance(application).robotInfoKeyRepository.getList(year, userTeamAccount))
            }
        }
    }
}
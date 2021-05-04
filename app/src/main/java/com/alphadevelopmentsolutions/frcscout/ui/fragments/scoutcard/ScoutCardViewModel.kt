package com.alphadevelopmentsolutions.frcscout.ui.fragments.scoutcard

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.*
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore

class ScoutCardViewModel(
    application: Application,
    lifecycleOwner: LifecycleOwner,
    val match: Match,
    val team: Team
) : AndroidViewModel(application) {
    private val context = application

    var scoutCardInfoKeyViewList: ObservableField<List<ScoutCardInfoKeyView>> = ObservableField(listOf())

    private val keyStore by lazy {
        KeyStore.getInstance(context)
    }

    init {
        val year = keyStore.selectedYear
        val userTeamAccount = Account.getInstance(context)?.userTeamAccount

        if (year != null && userTeamAccount != null) {
            launchIO(lifecycleOwner) {
                scoutCardInfoKeyViewList.set(RepositoryProvider.getInstance(application).scoutCardInfoKeyRepository.getList(year, userTeamAccount))
            }
        }
    }
}
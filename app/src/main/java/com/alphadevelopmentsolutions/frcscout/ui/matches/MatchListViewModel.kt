package com.alphadevelopmentsolutions.frcscout.ui.matches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.alphadevelopmentsolutions.frcscout.data.repositories.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.data.models.Match

class MatchListViewModel(application: Application) : AndroidViewModel(application) {
    private val matchRepo =
        RepositoryProvider.getInstance(application)
            .matchRepo

    val matchList: LiveData<MutableList<Match>> by lazy {
        matchRepo.getAll()
    }
}
package com.alphadevelopmentsolutions.frcscout.ui.matches

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.alphadevelopmentsolutions.frcscout.R
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.repository.MatchRepository
import com.alphadevelopmentsolutions.frcscout.repository.RepositoryProvider
import com.alphadevelopmentsolutions.frcscout.table.Match

class MatchListViewModel(application: Application) : AndroidViewModel(application) {
    private val matchRepo =
        RepositoryProvider.getInstance(application)
            .matchRepo

    val matchList: LiveData<MutableList<Match>> by lazy {
        matchRepo.getAll()
    }
}
package com.alphadevelopmentsolutions.frcscout.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.Classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import com.alphadevelopmentsolutions.frcscout.Repositories.TeamRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class TeamViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            TeamRepository(RDatabase.getInstance(application).teamDao())

    /**
     * Gets all [Team] objects from the database
     * @see TeamRepository.objs
     */
    val objs: Flowable<List<Team>>

    /**
     * Gets all [Team] objects from the database based on [Team.id]
     * @param id specified the id to sort the [Team] object by
     * @see TeamRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [Team] object into the database
     * @see TeamRepository.insert
     */
    fun insert(team: Team) = viewModelScope.launch {
        repository.insert(team)
    }

    /**
     * Inserts a [Team] object into the database
     * @see TeamRepository.insert
     */
    fun insertAll(teams: List<Team>) = viewModelScope.launch {
        repository.insertAll(teams)
    }
}
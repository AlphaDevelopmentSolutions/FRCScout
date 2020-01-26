package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.Team
import com.alphadevelopmentsolutions.frcscout.repository.TeamRepository
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
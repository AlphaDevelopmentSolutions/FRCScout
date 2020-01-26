package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.Event
import com.alphadevelopmentsolutions.frcscout.classes.table.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.Team
import com.alphadevelopmentsolutions.frcscout.enums.SortDirection
import com.alphadevelopmentsolutions.frcscout.repository.MatchRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class MatchViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            MatchRepository(RDatabase.getInstance(application).matchDao())

    /**
     * Gets all [Match] objects from the database
     * @see MatchRepository.objs
     */
    val objs: Flowable<List<Match>>

    /**
     * Gets all [Match] objects from the database based on [Match.id]
     * @param id specified the id to sort the [Match] object by
     * @see MatchRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    fun objWithCustom(event: Event?, match: Match?, team: Team?, sortDirection: SortDirection = SortDirection.DESC) = repository.objWithCustom(event, match, team, sortDirection)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [Match] object into the database
     * @see MatchRepository.insert
     */
    fun insert(match: Match) = viewModelScope.launch {
        repository.insert(match)
    }

    /**
     * Inserts a [Match] object into the database
     * @see MatchRepository.insert
     */
    fun insertAll(matchs: List<Match>) = viewModelScope.launch {
        repository.insertAll(matchs)
    }
}
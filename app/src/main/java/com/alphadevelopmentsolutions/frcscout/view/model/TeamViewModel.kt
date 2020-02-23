package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.repository.ScoutCardInfoRepository
import com.alphadevelopmentsolutions.frcscout.repository.TeamRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch
import java.util.*

class TeamViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            TeamRepository(RDatabase.getInstance(application).teamDao())

    private val context = application

    /**
     * Gets all [Team] objects from the database
     * @see TeamRepository.objs
     */
    val objs by lazy {
        repository.objs
    }

    fun objAtEvent(eventId: UUID) = repository.objAtEvent(eventId)

    /**
     * Gets all [Team] objects from the database based on [Team.id]
     * @param id specified the id to sort the [Team] object by
     * @see TeamRepository.objWithId
     */
    fun objWithId(id: UUID) = repository.objWithId(id)

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

    suspend fun getStats(team: Team, match: Match): HashMap<String, Double> {

        val scoutCardInfoRepo = ScoutCardInfoRepository(RDatabase.getInstance(context).scoutCardInfoDao())

        val scoutCardInfoViewList = scoutCardInfoRepo.objsViewForTeam(team.id, match.id)

        return scoutCardInfoRepo.calculateStats(scoutCardInfoRepo.objsViewForMatch(match.id))
    }
}
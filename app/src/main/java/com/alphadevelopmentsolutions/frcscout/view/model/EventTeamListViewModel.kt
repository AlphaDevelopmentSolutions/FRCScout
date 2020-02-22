package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.core.EventTeamList
import com.alphadevelopmentsolutions.frcscout.repository.EventTeamListRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class EventTeamListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            EventTeamListRepository(RDatabase.getInstance(application).eventTeamListDao())

    /**
     * Gets all [EventTeamList] objects from the database
     * @see EventTeamListRepository.objs
     */
    val objs: Flowable<List<EventTeamList>>

    /**
     * Gets all [EventTeamList] objects from the database based on [EventTeamList.id]
     * @param id specified the id to sort the [EventTeamList] object by
     * @see EventTeamListRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [EventTeamList] object into the database
     * @see EventTeamListRepository.insert
     */
    fun insert(eventTeamList: EventTeamList) = viewModelScope.launch {
        repository.insert(eventTeamList)
    }

    /**
     * Inserts a [EventTeamList] object into the database
     * @see EventTeamListRepository.insert
     */
    fun insertAll(eventTeamLists: List<EventTeamList>) = viewModelScope.launch {
        repository.insertAll(eventTeamLists)
    }
}
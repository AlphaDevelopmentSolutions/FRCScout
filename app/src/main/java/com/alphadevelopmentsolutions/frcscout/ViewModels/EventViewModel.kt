package com.alphadevelopmentsolutions.frcscout.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.Classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event
import com.alphadevelopmentsolutions.frcscout.Repositories.EventRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            EventRepository(RDatabase.getInstance(application).eventDao())

    /**
     * Gets all [Event] objects from the database
     * @see EventRepository.objs
     */
    val objs: Flowable<List<Event>>

    /**
     * Gets all [Event] objects from the database based on [Event.id]
     * @param id specified the id to sort the [Event] object by
     * @see EventRepository.objWithId
     */
    fun objWithId(id: Int) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [Event] object into the database
     * @see EventRepository.insert
     */
    fun insert(event: Event) = viewModelScope.launch {
        repository.insert(event)
    }

    /**
     * Inserts a [Event] object into the database
     * @see EventRepository.insert
     */
    fun insertAll(events: List<Event>) = viewModelScope.launch {
        repository.insertAll(events)
    }
}
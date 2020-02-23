package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import com.alphadevelopmentsolutions.frcscout.repository.EventRepository
import com.alphadevelopmentsolutions.frcscout.repository.ScoutCardInfoRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.round

class EventViewModel(application: Application) : AndroidViewModel(application) {

    private val repository by lazy {
        EventRepository(RDatabase.getInstance(application).eventDao())
    }

    private val context = application

    /**
     * Gets all [Event] objects from the database
     * @see EventRepository.objs
     */
    val objs by lazy {
        repository.objs
    }

    /**
     * Gets all [Event] objects from the database based on [Event.id]
     * @param id specified the id to sort the [Event] object by
     * @see EventRepository.objWithId
     */
    fun objWithId(id: UUID) = repository.objWithId(id)

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

    suspend fun getStats(event: Event): HashMap<String, Double> {

        val scoutCardInfoRepo = ScoutCardInfoRepository(RDatabase.getInstance(context).scoutCardInfoDao())

        return scoutCardInfoRepo.calculateStats(scoutCardInfoRepo.objsViewForEvent(event.id))
    }

    suspend fun clearData() = repository.clearData()
}
package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import com.alphadevelopmentsolutions.frcscout.dao.EventDao
import io.reactivex.Flowable
import java.util.*


class EventRepository(private val eventDao: EventDao) {

    /**
     * Gets all [Event] objects from the database
     * @see EventDao.getObjs
     */
    val objs: Flowable<List<Event>> = eventDao.getObjs()

    /**
     * Gets all [Event] objects from the database based on [Event.id]
     * @param id specified the id to sort the [Event] object by
     * @see EventDao.getObjWithId
     */
    fun objWithId(id: UUID) = eventDao.getObjWithId(id)

    /**
     * Inserts a [Event] object into the database
     * @see EventDao.insert
     */
    suspend fun insert(event: Event) {
        eventDao.insert(event)
    }

    /**
     * Inserts a [Event] object into the database
     * @see EventDao.insertAll
     */
    suspend fun insertAll(events: List<Event>) {
        eventDao.insertAll(events)
    }

    suspend fun clearData() = eventDao.clear()
}
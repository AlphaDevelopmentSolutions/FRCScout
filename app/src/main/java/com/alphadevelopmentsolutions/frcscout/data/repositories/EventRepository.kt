package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.EventDao
import com.alphadevelopmentsolutions.frcscout.data.models.Event

class EventRepository(private val dao: EventDao) : MasterRepository<Event>(dao), SubmittableTable<Event> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<Event> =
        listOf()
}
package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.EventDao
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Year

class EventRepository(private val dao: EventDao) : MasterRepository<Event>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()

    fun getInYear(year: Year) =
        dao.getInYear(year.id)
}
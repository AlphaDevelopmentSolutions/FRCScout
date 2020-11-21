package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.EventTeamListDao
import com.alphadevelopmentsolutions.frcscout.data.models.EventTeamList

class EventTeamListRepository(private val dao: EventTeamListDao) : MasterRepository<EventTeamList>(dao), SubmittableTable<EventTeamList> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<EventTeamList> =
        listOf()
}
package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.TeamDao
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class TeamRepository(private val dao: TeamDao) : MasterRepository<Team>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()

    fun getAtEvent(event: Event) =
        dao.getAtEvent(event.id)
}
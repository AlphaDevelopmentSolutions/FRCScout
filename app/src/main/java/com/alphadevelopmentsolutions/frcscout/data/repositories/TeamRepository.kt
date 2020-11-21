package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.TeamDao
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class TeamRepository(private val dao: TeamDao) : MasterRepository<Team>(dao), SubmittableTable<Team> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<Team> =
        listOf()
}
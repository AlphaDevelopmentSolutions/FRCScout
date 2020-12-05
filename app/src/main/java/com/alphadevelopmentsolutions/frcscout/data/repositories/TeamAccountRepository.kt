package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.TeamAccountDao
import com.alphadevelopmentsolutions.frcscout.data.models.TeamAccount

class TeamAccountRepository(private val dao: TeamAccountDao) : MasterRepository<TeamAccount>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}
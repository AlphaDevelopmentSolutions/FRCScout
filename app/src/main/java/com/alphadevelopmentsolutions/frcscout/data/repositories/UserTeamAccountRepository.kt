package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.UserTeamAccountDao
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount

class UserTeamAccountRepository(private val dao: UserTeamAccountDao) : MasterRepository<UserTeamAccount>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun getAllRaw() =
        dao.getAllRaw()

    suspend fun getAllRawView() =
        dao.getAllRawView()
}
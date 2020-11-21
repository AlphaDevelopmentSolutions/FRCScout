package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.UserTeamAccountDao
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount

class UserTeamAccountRepository(private val dao: UserTeamAccountDao) : MasterRepository<UserTeamAccount>(dao), SubmittableTable<UserTeamAccount> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<UserTeamAccount> =
        listOf()
}
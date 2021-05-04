package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.ScoutCardInfoKeyDao
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount
import com.alphadevelopmentsolutions.frcscout.data.models.Year

class ScoutCardInfoKeyRepository(private val dao: ScoutCardInfoKeyDao) : MasterRepository<ScoutCardInfoKey>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun getList(year: Year, userTeamAccount: UserTeamAccount) =
        dao.getList(year.id, userTeamAccount.teamAccountId)
}
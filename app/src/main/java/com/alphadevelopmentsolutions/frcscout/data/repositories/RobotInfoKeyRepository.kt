package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.RobotInfoKeyDao
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.data.models.TeamAccount
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount
import com.alphadevelopmentsolutions.frcscout.data.models.Year

class RobotInfoKeyRepository(private val dao: RobotInfoKeyDao) : MasterRepository<RobotInfoKey>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun getList(year: Year, userTeamAccount: UserTeamAccount) =
        dao.getList(year.id, userTeamAccount.teamAccountId)
}
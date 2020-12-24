package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.RoleDao
import com.alphadevelopmentsolutions.frcscout.data.models.Role
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount

class RoleRepository(private val dao: RoleDao) : MasterRepository<Role>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun getListForUserTeamAccount(userTeamAccount: UserTeamAccount) =
        dao.getListForUserTeamAccount(userTeamAccount.id)
}
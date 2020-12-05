package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.UserRoleDao
import com.alphadevelopmentsolutions.frcscout.data.models.UserRole

class UserRoleRepository(private val dao: UserRoleDao) : MasterRepository<UserRole>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}
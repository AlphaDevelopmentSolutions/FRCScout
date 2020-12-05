package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.RoleDao
import com.alphadevelopmentsolutions.frcscout.data.models.Role

class RoleRepository(private val dao: RoleDao) : MasterRepository<Role>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}
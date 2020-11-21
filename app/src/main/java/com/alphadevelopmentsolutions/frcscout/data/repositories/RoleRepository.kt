package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.RoleDao
import com.alphadevelopmentsolutions.frcscout.data.models.Role

class RoleRepository(private val dao: RoleDao) : MasterRepository<Role>(dao), SubmittableTable<Role> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<Role> =
        listOf()
}
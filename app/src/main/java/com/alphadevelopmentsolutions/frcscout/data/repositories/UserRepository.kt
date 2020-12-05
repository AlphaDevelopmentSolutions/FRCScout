package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.UserDao
import com.alphadevelopmentsolutions.frcscout.data.models.User

class UserRepository(private val dao: UserDao) : MasterRepository<User>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}
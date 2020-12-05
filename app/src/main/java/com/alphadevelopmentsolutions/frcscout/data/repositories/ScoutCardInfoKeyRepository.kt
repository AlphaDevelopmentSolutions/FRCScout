package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.ScoutCardInfoKeyDao
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKey

class ScoutCardInfoKeyRepository(private val dao: ScoutCardInfoKeyDao) : MasterRepository<ScoutCardInfoKey>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}
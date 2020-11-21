package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.ScoutCardInfoKeyDao
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKey

class ScoutCardInfoKeyRepository(private val dao: ScoutCardInfoKeyDao) : MasterRepository<ScoutCardInfoKey>(dao), SubmittableTable<ScoutCardInfoKey> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<ScoutCardInfoKey> =
        listOf()
}
package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.YearDao
import com.alphadevelopmentsolutions.frcscout.data.models.Year

class YearRepository(private val dao: YearDao) : MasterRepository<Year>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun getAllRaw(isDraft: Boolean?) =
        dao.getAllRaw(isDraft)
}
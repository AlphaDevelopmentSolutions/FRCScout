package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.MatchTypeDao
import com.alphadevelopmentsolutions.frcscout.data.models.MatchType

class MatchTypeRepository(private val dao: MatchTypeDao) : MasterRepository<MatchType>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}
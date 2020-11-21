package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.models.Match

class MatchRepository(private val dao: MatchDao) : MasterRepository<Match>(dao), SubmittableTable<Match> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<Match> =
        listOf()
}
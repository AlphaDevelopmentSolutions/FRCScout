package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchTypeDao
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.MatchType

class MatchTypeRepository(private val dao: MatchTypeDao) : MasterRepository<MatchType>(dao), SubmittableTable<MatchType> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<MatchType> =
        listOf()
}
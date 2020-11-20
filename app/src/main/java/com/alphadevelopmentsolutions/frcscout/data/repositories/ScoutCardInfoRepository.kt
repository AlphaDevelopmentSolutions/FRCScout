package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.dao.ScoutCardInfoDao
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfo

class ScoutCardInfoRepository(private val dao: ScoutCardInfoDao) : MasterRepository<ScoutCardInfo>(dao), SubmittableTable<ScoutCardInfo> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<ScoutCardInfo> =
        listOf()
}
package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.dao.RobotInfoKeyDao
import com.alphadevelopmentsolutions.frcscout.data.dao.RobotInfoKeyDao_Impl
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey

class RobotInfoKeRepository(private val dao: RobotInfoKeyDao) : MasterRepository<RobotInfoKey>(dao), SubmittableTable<RobotInfoKey> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<RobotInfoKey> =
        listOf()
}
package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.dao.RobotMediaDao
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia

class RobotMediaRepository(private val dao: RobotMediaDao) : MasterRepository<RobotMedia>(dao), SubmittableTable<RobotMedia> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<RobotMedia> =
        listOf()
}
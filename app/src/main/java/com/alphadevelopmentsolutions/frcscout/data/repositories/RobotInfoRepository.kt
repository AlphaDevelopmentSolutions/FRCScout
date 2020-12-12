package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.RobotInfoDao
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class RobotInfoRepository(private val dao: RobotInfoDao) : MasterRepository<RobotInfo>(dao), SubmittableTable<RobotInfo> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<RobotInfo> =
        listOf()

    suspend fun getForTeamAtEvent(event: Event, team: Team) =
        dao.getForTeamAtEvent(event.id, team.id)
}
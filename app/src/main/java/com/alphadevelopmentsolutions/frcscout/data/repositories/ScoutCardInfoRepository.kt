package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.ScoutCardInfoDao
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class ScoutCardInfoRepository(private val dao: ScoutCardInfoDao) : MasterRepository<ScoutCardInfo>(dao), SubmittableTable<ScoutCardInfo> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<ScoutCardInfo> =
        listOf()

    suspend fun getForTeamInMatch(team: Team, match: Match) =
        dao.getForTeamInMatch(team.id, match.id)
}
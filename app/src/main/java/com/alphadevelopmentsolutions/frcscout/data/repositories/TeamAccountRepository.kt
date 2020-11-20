package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.dao.TeamAccountDao
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.TeamAccount

class TeamAccountRepository(private val dao: TeamAccountDao) : MasterRepository<TeamAccount>(dao), SubmittableTable<TeamAccount> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<TeamAccount> =
        listOf()
}
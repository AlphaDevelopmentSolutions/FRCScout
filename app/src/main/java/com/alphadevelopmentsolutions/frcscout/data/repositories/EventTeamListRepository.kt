package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.EventTeamListDao
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.models.EventTeamList
import com.alphadevelopmentsolutions.frcscout.data.models.Match

class EventTeamListRepository(private val dao: EventTeamListDao) : MasterRepository<EventTeamList>(dao), SubmittableTable<EventTeamList> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<EventTeamList> =
        listOf()
}
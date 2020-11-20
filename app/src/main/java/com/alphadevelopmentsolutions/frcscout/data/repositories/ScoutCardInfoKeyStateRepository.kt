package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.dao.ScoutCardInfoKeyStateDao
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKeyState

class ScoutCardInfoKeyStateRepository(private val dao: ScoutCardInfoKeyStateDao) : MasterRepository<ScoutCardInfoKeyState>(dao), SubmittableTable<ScoutCardInfoKeyState> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<ScoutCardInfoKeyState> =
        listOf()
}
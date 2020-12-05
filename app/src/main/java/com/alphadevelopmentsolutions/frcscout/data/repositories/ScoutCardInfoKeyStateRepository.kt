package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.ScoutCardInfoKeyStateDao
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKeyState

class ScoutCardInfoKeyStateRepository(private val dao: ScoutCardInfoKeyStateDao) : MasterRepository<ScoutCardInfoKeyState>(dao) {
    override suspend fun deleteAll() =
        dao.deleteAll()
}
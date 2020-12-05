package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.LogDeleteDao
import com.alphadevelopmentsolutions.frcscout.data.models.LogDelete

class LogDeleteRepository(private val dao: LogDeleteDao) {
    suspend fun enable() = dao.set(LogDelete(true))
    suspend fun disable() = dao.set(LogDelete(false))
}
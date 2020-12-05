package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.LogDraftDao
import com.alphadevelopmentsolutions.frcscout.data.models.LogDraft

class LogDraftRepository(private val dao: LogDraftDao) {
    suspend fun enable() = dao.set(LogDraft(true))
    suspend fun disable() = dao.set(LogDraft(false))
}
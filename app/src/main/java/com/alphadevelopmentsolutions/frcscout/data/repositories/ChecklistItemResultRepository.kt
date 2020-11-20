package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.ChecklistItemResultDao
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.data.models.Match

class ChecklistItemResultRepository(private val dao: ChecklistItemResultDao) : MasterRepository<ChecklistItemResult>(dao), SubmittableTable<ChecklistItemResult> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<ChecklistItemResult> =
        listOf()
}
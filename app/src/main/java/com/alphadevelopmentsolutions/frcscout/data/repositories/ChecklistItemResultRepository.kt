package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.ChecklistItemResultDao
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItemResult

class ChecklistItemResultRepository(private val dao: ChecklistItemResultDao) : MasterRepository<ChecklistItemResult>(dao), SubmittableTable<ChecklistItemResult> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<ChecklistItemResult> =
        listOf()
}
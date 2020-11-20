package com.alphadevelopmentsolutions.frcscout.data.repositories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.data.dao.ChecklistItemDao
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.Match

class ChecklistItemRepository(private val dao: ChecklistItemDao) : MasterRepository<ChecklistItem>(dao), SubmittableTable<ChecklistItem> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<ChecklistItem> =
        listOf()
}
package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.Tables.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.dao.ChecklistItemDao
import io.reactivex.Flowable


class ChecklistItemRepository(private val checklistItemDao: ChecklistItemDao) {

    /**
     * Gets all [ChecklistItem] objects from the database
     * @see ChecklistItemDao.getObjs
     */
    val objs: Flowable<List<ChecklistItem>> = checklistItemDao.getObjs()

    /**
     * Gets all [ChecklistItem] objects from the database based on [ChecklistItem.id]
     * @param id specified the id to sort the [ChecklistItem] object by
     * @see ChecklistItemDao.getObjWithId
     */
    fun objWithId(id: String) = checklistItemDao.getObjWithId(id)

    /**
     * Inserts a [ChecklistItem] object into the database
     * @see ChecklistItemDao.insert
     */
    suspend fun insert(checklistItem: ChecklistItem) {
        checklistItemDao.insert(checklistItem)
    }

    /**
     * Inserts a [ChecklistItem] object into the database
     * @see ChecklistItemDao.insertAll
     */
    suspend fun insertAll(checklistItems: List<ChecklistItem>) {
        checklistItemDao.insertAll(checklistItems)
    }

}
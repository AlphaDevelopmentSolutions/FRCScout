package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.dao.ChecklistItemResultDao
import io.reactivex.Flowable
import java.util.*


class ChecklistItemResultRepository(private val checklistItemResultDao: ChecklistItemResultDao) {

    /**
     * Gets all [ChecklistItemResult] objects from the database
     * @see ChecklistItemResultDao.getObjs
     */
    val objs: Flowable<List<ChecklistItemResult>> = checklistItemResultDao.getObjs()

    /**
     * Gets all [ChecklistItemResult] objects from the database based on [ChecklistItemResult.id]
     * @param id specified the id to sort the [ChecklistItemResult] object by
     * @see ChecklistItemResultDao.getObjWithId
     */
    fun objWithId(id: String) = checklistItemResultDao.getObjWithId(id)

    fun objsViewWithMatch(matchId: UUID) = checklistItemResultDao.getObjsViewWithMatch(matchId)

    /**
     * Inserts a [ChecklistItemResult] object into the database
     * @see ChecklistItemResultDao.insert
     */
    suspend fun insert(checklistItemResult: ChecklistItemResult) {
        checklistItemResultDao.insert(checklistItemResult)
    }

    /**
     * Inserts a [ChecklistItemResult] object into the database
     * @see ChecklistItemResultDao.insertAll
     */
    suspend fun insertAll(checklistItemResults: List<ChecklistItemResult>) {
        checklistItemResultDao.insertAll(checklistItemResults)
    }

}
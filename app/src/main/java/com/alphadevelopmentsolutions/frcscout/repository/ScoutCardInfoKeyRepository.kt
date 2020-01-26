package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.Tables.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.dao.ScoutCardInfoKeyDao
import io.reactivex.Flowable


class ScoutCardInfoKeyRepository(private val scoutCardInfoKeyDao: ScoutCardInfoKeyDao) {

    /**
     * Gets all [ScoutCardInfoKey] objects from the database
     * @see ScoutCardInfoKeyDao.getObjs
     */
    val objs: Flowable<List<ScoutCardInfoKey>> = scoutCardInfoKeyDao.getObjs()

    /**
     * Gets all [ScoutCardInfoKey] objects from the database based on [ScoutCardInfoKey.id]
     * @param id specified the id to sort the [ScoutCardInfoKey] object by
     * @see ScoutCardInfoKeyDao.getObjWithId
     */
    fun objWithId(id: String) = scoutCardInfoKeyDao.getObjWithId(id)

    /**
     * Inserts a [ScoutCardInfoKey] object into the database
     * @see ScoutCardInfoKeyDao.insert
     */
    suspend fun insert(scoutCardInfoKey: ScoutCardInfoKey) {
        scoutCardInfoKeyDao.insert(scoutCardInfoKey)
    }

    /**
     * Inserts a [ScoutCardInfoKey] object into the database
     * @see ScoutCardInfoKeyDao.insertAll
     */
    suspend fun insertAll(scoutCardInfoKeys: List<ScoutCardInfoKey>) {
        scoutCardInfoKeyDao.insertAll(scoutCardInfoKeys)
    }

}
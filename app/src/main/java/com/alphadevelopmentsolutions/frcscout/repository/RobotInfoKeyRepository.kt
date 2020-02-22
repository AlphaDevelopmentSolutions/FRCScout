package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.dao.RobotInfoKeyDao
import io.reactivex.Flowable


class RobotInfoKeyRepository(private val robotInfoKeyDao: RobotInfoKeyDao) {

    /**
     * Gets all [RobotInfoKey] objects from the database
     * @see RobotInfoKeyDao.getObjs
     */
    val objs: Flowable<List<RobotInfoKey>> = robotInfoKeyDao.getObjs()

    /**
     * Gets all [RobotInfoKey] objects from the database based on [RobotInfoKey.id]
     * @param id specified the id to sort the [RobotInfoKey] object by
     * @see RobotInfoKeyDao.getObjWithId
     */
    fun objWithId(id: String) = robotInfoKeyDao.getObjWithId(id)

    /**
     * Inserts a [RobotInfoKey] object into the database
     * @see RobotInfoKeyDao.insert
     */
    suspend fun insert(robotInfoKey: RobotInfoKey) {
        robotInfoKeyDao.insert(robotInfoKey)
    }

    /**
     * Inserts a [RobotInfoKey] object into the database
     * @see RobotInfoKeyDao.insertAll
     */
    suspend fun insertAll(robotInfoKeys: List<RobotInfoKey>) {
        robotInfoKeyDao.insertAll(robotInfoKeys)
    }

}
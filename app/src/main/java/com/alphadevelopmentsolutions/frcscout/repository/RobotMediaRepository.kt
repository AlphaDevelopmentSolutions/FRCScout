package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.dao.RobotMediaDao
import io.reactivex.Flowable


class RobotMediaRepository(private val robotMediaDao: RobotMediaDao) {

    /**
     * Gets all [RobotMedia] objects from the database
     * @see RobotMediaDao.getObjs
     */
    val objs: Flowable<List<RobotMedia>> = robotMediaDao.getObjs()

    /**
     * Gets all [RobotMedia] objects from the database based on [RobotMedia.id]
     * @param id specified the id to sort the [RobotMedia] object by
     * @see RobotMediaDao.getObjWithId
     */
    fun objWithId(id: String) = robotMediaDao.getObjWithId(id)

    /**
     * Inserts a [RobotMedia] object into the database
     * @see RobotMediaDao.insert
     */
    suspend fun insert(robotMedia: RobotMedia) {
        robotMediaDao.insert(robotMedia)
    }

    /**
     * Inserts a [RobotMedia] object into the database
     * @see RobotMediaDao.insertAll
     */
    suspend fun insertAll(robotMedias: List<RobotMedia>) {
        robotMediaDao.insertAll(robotMedias)
    }

}
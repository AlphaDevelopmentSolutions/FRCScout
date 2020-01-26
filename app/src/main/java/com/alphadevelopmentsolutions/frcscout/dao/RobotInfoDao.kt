package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.Tables.RobotInfo
import io.reactivex.Flowable

@Dao
interface RobotInfoDao {

    /**
     * Gets all [RobotInfo] objects from the database
     */
    @Query("SELECT * FROM robot_info")
    fun getObjs(): Flowable<List<RobotInfo>>

    /**
     * Gets all [RobotInfo] objects from the database based on [RobotInfo.id]
     * @param id specified the id to sort the [RobotInfo] object by
     */
    @Query("SELECT * FROM robot_info where id = :id")
    fun getObjWithId(id: String): Flowable<RobotInfo>

    /**
     * Inserts a new [RobotInfo] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(robotInfo: RobotInfo)

    /**
     * Inserts a list of [RobotInfo] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(robotInfos: List<RobotInfo>)

    /**
     * Deletes all [RobotInfo] objects from the database
     */
    @Query("DELETE FROM robot_info")
    suspend fun clear()
}
package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.table.RobotInfoKey
import io.reactivex.Flowable

@Dao
interface RobotInfoKeyDao {

    /**
     * Gets all [RobotInfoKey] objects from the database
     */
    @Query("SELECT * FROM robot_info_keys")
    fun getObjs(): Flowable<List<RobotInfoKey>>

    /**
     * Gets all [RobotInfoKey] objects from the database based on [RobotInfoKey.id]
     * @param id specified the id to sort the [RobotInfoKey] object by
     */
    @Query("SELECT * FROM robot_info_keys where id = :id")
    fun getObjWithId(id: String): Flowable<RobotInfoKey>

    /**
     * Inserts a new [RobotInfoKey] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(robotInfoKey: RobotInfoKey)

    /**
     * Inserts a list of [RobotInfoKey] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(robotInfoKeys: List<RobotInfoKey>)

    /**
     * Deletes all [RobotInfoKey] objects from the database
     */
    @Query("DELETE FROM robot_info_keys")
    suspend fun clear()
}
package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.*
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotInfo
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.view.database.RobotInfoDatabaseView
import io.reactivex.Flowable
import java.util.*

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

    @Query(
            """
                SELECT * FROM ${TableName.ROBOT_INFO_KEY}
                LEFT JOIN ${TableName.ROBOT_INFO} ON ${TableName.ROBOT_INFO}.keyId = ${TableName.ROBOT_INFO_KEY}.id
                WHERE ${TableName.ROBOT_INFO}.teamId = :teamId
            """
    )
    fun getObjsViewForTeam(teamId: UUID): Flowable<List<RobotInfoDatabaseView>>

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

    @Delete
    fun delete(robotInfo: RobotInfo)
}
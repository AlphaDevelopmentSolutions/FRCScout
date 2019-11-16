package com.alphadevelopmentsolutions.frcscout.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year
import io.reactivex.Flowable

@Dao
interface RobotMediaDao {

    /**
     * Gets all [RobotMedia] objects from the database
     */
    @Query("SELECT * FROM robot_media")
    fun getObjs(): Flowable<List<RobotMedia>>

    /**
     * Gets all [RobotMedia] objects from the database based on [RobotMedia.id]
     * @param id specified the id to sort the [RobotMedia] object by
     */
    @Query("SELECT * FROM robot_media where id = :id")
    fun getObjWithId(id: String): Flowable<RobotMedia>

    /**
     * Inserts a new [RobotMedia] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(robotMedia: RobotMedia)

    /**
     * Inserts a list of [RobotMedia] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(robotMediaList: List<RobotMedia>)

    /**
     * Deletes all [RobotMedia] objects from the database
     */
    @Query("DELETE FROM robot_media")
    suspend fun clear()
}
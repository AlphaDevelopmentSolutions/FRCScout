package com.alphadevelopmentsolutions.frcscout.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfo
import io.reactivex.Flowable

@Dao
interface ScoutCardInfoDao {

    /**
     * Gets all [ScoutCardInfo] objects from the database
     */
    @Query("SELECT * FROM scout_card_info")
    fun getObjs(): Flowable<List<ScoutCardInfo>>

    /**
     * Gets all [ScoutCardInfo] objects from the database based on [ScoutCardInfo.id]
     * @param id specified the id to sort the [ScoutCardInfo] object by
     */
    @Query("SELECT * FROM scout_card_info where id = :id")
    fun getObjWithId(id: String): Flowable<ScoutCardInfo>

    /**
     * Inserts a new [ScoutCardInfo] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scoutCardInfo: ScoutCardInfo)

    /**
     * Inserts a list of [ScoutCardInfo] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scoutCardInfos: List<ScoutCardInfo>)

    /**
     * Deletes all [ScoutCardInfo] objects from the database
     */
    @Query("DELETE FROM scout_card_info")
    suspend fun clear()
}
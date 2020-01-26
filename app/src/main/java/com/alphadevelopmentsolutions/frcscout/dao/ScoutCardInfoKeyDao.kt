package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.table.ScoutCardInfoKey
import io.reactivex.Flowable

@Dao
interface ScoutCardInfoKeyDao {

    /**
     * Gets all [ScoutCardInfoKey] objects from the database
     */
    @Query("SELECT * FROM scout_card_info_keys")
    fun getObjs(): Flowable<List<ScoutCardInfoKey>>

    /**
     * Gets all [ScoutCardInfoKey] objects from the database based on [ScoutCardInfoKey.id]
     * @param id specified the id to sort the [ScoutCardInfoKey] object by
     */
    @Query("SELECT * FROM scout_card_info_keys where id = :id")
    fun getObjWithId(id: String): Flowable<ScoutCardInfoKey>

    /**
     * Inserts a new [ScoutCardInfoKey] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scoutCardInfoKey: ScoutCardInfoKey)

    /**
     * Inserts a list of [ScoutCardInfoKey] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scoutCardInfoKeys: List<ScoutCardInfoKey>)

    /**
     * Deletes all [ScoutCardInfoKey] objects from the database
     */
    @Query("DELETE FROM scout_card_info_keys")
    suspend fun clear()
}
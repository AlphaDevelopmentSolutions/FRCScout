package com.alphadevelopmentsolutions.frcscout.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year
import io.reactivex.Flowable

@Dao
interface YearDao {

    /**
     * Gets all [Year] objects from the database
     */
    @Query("SELECT * FROM years")
    fun getObjs(): Flowable<List<Year>>

    /**
     * Gets all [Year] objects from the database based on [Year.id]
     * @param id specified the id to sort the [Year] object by
     */
    @Query("SELECT * FROM years where id = :id")
    fun getObjWithId(id: String): Flowable<Year>

    /**
     * Inserts a new [Year] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(listing: Year)

    /**
     * Inserts a list of [Year] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(listings: List<Year>)

    /**
     * Deletes all [Year] objects from the database
     */
    @Query("DELETE FROM years")
    suspend fun clear()
}
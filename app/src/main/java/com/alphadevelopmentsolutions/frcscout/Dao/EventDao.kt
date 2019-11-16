package com.alphadevelopmentsolutions.frcscout.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event
import io.reactivex.Flowable

@Dao
interface EventDao {

    /**
     * Gets all [Event] objects from the database
     */
    @Query("SELECT * FROM events")
    fun getObjs(): Flowable<List<Event>>

    /**
     * Gets all [Event] objects from the database based on [Event.id]
     * @param id specified the id to sort the [Event] object by
     */
    @Query("SELECT * FROM events where id = :id")
    fun getObjWithId(id: Int): Flowable<Event>

    /**
     * Inserts a new [Event] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(event: Event)

    /**
     * Inserts a list of [Event] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(events: List<Event>)

    /**
     * Deletes all [Event] objects from the database
     */
    @Query("DELETE FROM events")
    suspend fun clear()
}
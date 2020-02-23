package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import io.reactivex.Flowable
import java.util.*

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
    fun getObjWithId(id: UUID): Flowable<Event>

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
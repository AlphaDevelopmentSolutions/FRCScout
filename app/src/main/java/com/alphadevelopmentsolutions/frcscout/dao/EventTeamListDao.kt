package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.table.EventTeamList
import io.reactivex.Flowable

@Dao
interface EventTeamListDao {

    /**
     * Gets all [EventTeamList] objects from the database
     */
    @Query("SELECT * FROM event_team_list")
    fun getObjs(): Flowable<List<EventTeamList>>

    /**
     * Gets all [EventTeamList] objects from the database based on [EventTeamList.id]
     * @param id specified the id to sort the [EventTeamList] object by
     */
    @Query("SELECT * FROM event_team_list where id = :id")
    fun getObjWithId(id: String): Flowable<EventTeamList>

    /**
     * Inserts a new [EventTeamList] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(eventTeamList: EventTeamList)

    /**
     * Inserts a list of [EventTeamList] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(eventTeamLists: List<EventTeamList>)

    /**
     * Deletes all [EventTeamList] objects from the database
     */
    @Query("DELETE FROM event_team_list")
    suspend fun clear()
}
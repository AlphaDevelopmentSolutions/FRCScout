package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Event
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import io.reactivex.Flowable
import java.util.*

@Dao
interface TeamDao {

    /**
     * Gets all [Team] objects from the database
     */
    @Query("SELECT * FROM teams")
    fun getObjs(): Flowable<List<Team>>

    /**
     * Gets all [Team] objects from the database based on [Team.id]
     * @param id specified the id to sort the [Team] object by
     */
    @Query("SELECT * FROM teams where id = :id")
    fun getObjWithId(id: UUID): Flowable<Team>

    @Query(
            """
                SELECT * FROM ${TableName.TEAM}
                LEFT JOIN ${TableName.EVENT_TEAM_LIST} ON ${TableName.EVENT_TEAM_LIST}.teamId = ${TableName.TEAM}.id
                WHERE ${TableName.EVENT_TEAM_LIST}.eventId = :eventId
            """
    )
    fun getObjAtEvent(eventId: UUID): Flowable<List<Team>>

    /**
     * Inserts a new [Team] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(team: Team)

    /**
     * Inserts a list of [Team] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<Team>)

    /**
     * Deletes all [Team] objects from the database
     */
    @Query("DELETE FROM teams")
    suspend fun clear()
}
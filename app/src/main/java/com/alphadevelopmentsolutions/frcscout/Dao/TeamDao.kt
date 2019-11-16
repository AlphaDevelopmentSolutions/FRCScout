package com.alphadevelopmentsolutions.frcscout.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team
import io.reactivex.Flowable

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
    fun getObjWithId(id: String): Flowable<Team>

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
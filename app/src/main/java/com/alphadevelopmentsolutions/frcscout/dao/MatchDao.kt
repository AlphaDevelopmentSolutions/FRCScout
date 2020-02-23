package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.view.database.MatchDatabaseView
import io.reactivex.Flowable

@Dao
interface MatchDao {

    /**
     * Gets all [Match] objects from the database
     */
    @Query("SELECT * FROM matches")
    fun getObjs(): Flowable<List<Match>>

    /**
     * Gets all [Match] objects from the database based on [Match.id]
     * @param id specified the id to sort the [Match] object by
     */
    @Query("SELECT * FROM matches where id = :id")
    fun getObjWithId(id: String): Flowable<Match>

    /**
     * Gets all [Match] objects from the database
     */
    @RawQuery(
            observedEntities = [
                MatchDatabaseView::class
            ]
    )
    fun getObjsWithCustom(sql: SimpleSQLiteQuery): Flowable<List<MatchDatabaseView>>

    /**
     * Inserts a new [Match] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(match: Match)

    /**
     * Inserts a list of [Match] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(matches: List<Match>)

    /**
     * Deletes all [Match] objects from the database
     */
    @Query("DELETE FROM matches")
    suspend fun clear()
}
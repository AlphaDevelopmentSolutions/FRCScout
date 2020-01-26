package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.table.ChecklistItemResult
import io.reactivex.Flowable

@Dao
interface ChecklistItemResultDao {

    /**
     * Gets all [ChecklistItemResult] objects from the database
     */
    @Query("SELECT * FROM checklist_item_results")
    fun getObjs(): Flowable<List<ChecklistItemResult>>

    /**
     * Gets all [ChecklistItemResult] objects from the database based on [ChecklistItemResult.id]
     * @param id specified the id to sort the [ChecklistItemResult] object by
     */
    @Query("SELECT * FROM checklist_item_results where id = :id")
    fun getObjWithId(id: String): Flowable<ChecklistItemResult>

    /**
     * Inserts a new [ChecklistItemResult] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checklistItemResult: ChecklistItemResult)

    /**
     * Inserts a list of [ChecklistItemResult] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checklistItemResults: List<ChecklistItemResult>)

    /**
     * Deletes all [ChecklistItemResult] objects from the database
     */
    @Query("DELETE FROM checklist_item_results")
    suspend fun clear()

}
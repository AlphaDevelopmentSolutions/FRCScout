package com.alphadevelopmentsolutions.frcscout.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem
import io.reactivex.Flowable

@Dao
interface ChecklistItemDao {

    /**
     * Gets all [ChecklistItem] objects from the database
     */
    @Query("SELECT * FROM checklist_items")
    fun getObjs(): Flowable<List<ChecklistItem>>

    /**
     * Gets all [ChecklistItem] objects from the database based on [ChecklistItem.id]
     * @param id specified the id to sort the [ChecklistItem] object by
     */
    @Query("SELECT * FROM checklist_items where id = :id")
    fun getObjWithId(id: String): Flowable<ChecklistItem>

    /**
     * Inserts a new [ChecklistItem] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checklistItem: ChecklistItem)

    /**
     * Inserts a list of [ChecklistItem] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(checklistItems: List<ChecklistItem>)

    /**
     * Deletes all [ChecklistItem] objects from the database
     */
    @Query("DELETE FROM checklist_items")
    suspend fun clear()

}
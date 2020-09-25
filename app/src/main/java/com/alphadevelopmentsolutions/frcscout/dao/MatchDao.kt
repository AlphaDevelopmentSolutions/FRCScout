package com.alphadevelopmentsolutions.frcscout.dao

import androidx.lifecycle.LiveData
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.table.Match

abstract class MatchDao : MasterDao<Match>() {
    @Query("SELECT * FROM ${TableName.MATCH}")
    abstract fun getAll(): LiveData<MutableList<Match>>

    @Query("SELECT * FROM ${TableName.MATCH} WHERE isDraft = IFNULL(:isDraft, 1) OR isDraft = IFNULL(:isDraft, 0)")
    abstract fun getAllRaw(isDraft: Boolean?): List<Match>

    @Query("DELETE FROM ${TableName.MATCH}")
    abstract suspend fun deleteAll()
}
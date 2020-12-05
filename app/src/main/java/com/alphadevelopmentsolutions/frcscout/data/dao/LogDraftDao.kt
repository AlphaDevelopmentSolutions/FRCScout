package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.alphadevelopmentsolutions.frcscout.data.models.LogDraft

@Dao
abstract class LogDraftDao : MasterDao<LogDraft>() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun set(obj: LogDraft)
}

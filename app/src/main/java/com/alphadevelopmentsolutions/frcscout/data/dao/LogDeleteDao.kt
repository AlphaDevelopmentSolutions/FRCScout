package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.alphadevelopmentsolutions.frcscout.data.models.LogDelete

@Dao
abstract class LogDeleteDao : MasterDao<LogDelete>() {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun set(obj: LogDelete)
}
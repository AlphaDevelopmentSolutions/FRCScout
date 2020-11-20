package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class EventDao : MasterDao<EventDao>() {
    @Query(
        """
            DELETE FROM ${TableName.EVENT}
        """
    )
    abstract fun deleteAll()
}
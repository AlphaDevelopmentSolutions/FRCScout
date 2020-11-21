package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class EventDao : MasterDao<Event>() {
    @Query(
        """
            DELETE FROM ${TableName.EVENT}
        """
    )
    abstract fun deleteAll()
}
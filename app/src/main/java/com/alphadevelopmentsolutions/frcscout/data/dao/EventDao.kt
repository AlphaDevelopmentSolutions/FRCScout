package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class EventDao : MasterDao<Event>() {
    @Query(
        """
            DELETE FROM ${TableName.EVENT}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT * 
            FROM ${TableName.EVENT}
            WHERE year_id = :yearId
        """
    )
    abstract fun getInYear(yearId: ByteArray): List<Event>
}
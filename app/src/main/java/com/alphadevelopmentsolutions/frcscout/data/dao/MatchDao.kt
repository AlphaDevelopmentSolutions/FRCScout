package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.ui.fragments.matches.MatchDatabaseView

@Dao
abstract class MatchDao : MasterDao<Match>() {
    @Query(
        """
            DELETE FROM ${TableName.MATCH}
        """
    )
    abstract fun deleteAll()

    @Transaction
    @Query(
        """
            SELECT * 
            FROM ${TableName.MATCH}
            WHERE event_id = :eventId
        """
    )
    abstract fun getForEvent(eventId: ByteArray): LiveData<MutableList<MatchDatabaseView>>
}
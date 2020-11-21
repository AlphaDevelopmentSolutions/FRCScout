package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class MatchDao : MasterDao<Match>() {
    @Query(
        """
            DELETE FROM ${TableName.MATCH}
        """
    )
    abstract fun deleteAll()
}
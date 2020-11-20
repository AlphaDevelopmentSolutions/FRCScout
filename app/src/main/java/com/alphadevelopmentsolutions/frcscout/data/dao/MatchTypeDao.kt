package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.MatchType
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class MatchTypeDao : MasterDao<MatchType>() {
    @Query(
        """
            DELETE FROM ${TableName.MATCH_TYPE}
        """
    )
    abstract fun deleteAll()
}
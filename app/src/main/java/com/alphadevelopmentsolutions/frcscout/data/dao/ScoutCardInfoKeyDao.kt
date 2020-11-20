package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class ScoutCardInfoKeyDao : MasterDao<ScoutCardInfoKey>() {
    @Query(
        """
            DELETE FROM ${TableName.SCOUT_CARD_INFO_KEY}
        """
    )
    abstract fun deleteAll()
}
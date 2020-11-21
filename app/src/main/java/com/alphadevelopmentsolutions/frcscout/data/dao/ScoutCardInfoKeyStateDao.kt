package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKeyState
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class ScoutCardInfoKeyStateDao : MasterDao<ScoutCardInfoKeyState>() {
    @Query(
        """
            DELETE FROM ${TableName.SCOUT_CARD_INFO_KEY_STATE}
        """
    )
    abstract fun deleteAll()
}
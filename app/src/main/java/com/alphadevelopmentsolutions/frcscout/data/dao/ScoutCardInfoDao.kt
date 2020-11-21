package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class ScoutCardInfoDao : MasterDao<ScoutCardInfo>() {
    @Query(
        """
            DELETE FROM ${TableName.SCOUT_CARD_INFO}
        """
    )
    abstract fun deleteAll()
}
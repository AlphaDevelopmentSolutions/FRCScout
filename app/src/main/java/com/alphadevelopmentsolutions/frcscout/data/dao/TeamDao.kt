package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class TeamDao : MasterDao<Team>() {
    @Query(
        """
            DELETE FROM ${TableName.TEAM}
        """
    )
    abstract fun deleteAll()
}
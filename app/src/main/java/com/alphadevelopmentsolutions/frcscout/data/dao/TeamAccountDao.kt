package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.TeamAccount
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class TeamAccountDao : MasterDao<TeamAccount>() {
    @Query(
        """
            DELETE FROM ${TableName.TEAM_ACCOUNT}
        """
    )
    abstract fun deleteAll()
}
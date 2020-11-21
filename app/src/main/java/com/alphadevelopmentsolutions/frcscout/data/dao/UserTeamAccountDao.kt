package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class UserTeamAccountDao : MasterDao<UserTeamAccount>() {
    @Query(
        """
            DELETE FROM ${TableName.USER_TEAM_ACCOUNT}
        """
    )
    abstract fun deleteAll()
}
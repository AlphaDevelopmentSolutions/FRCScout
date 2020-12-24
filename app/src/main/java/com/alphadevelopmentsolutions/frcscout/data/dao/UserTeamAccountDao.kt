package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.TeamAccount
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.ui.fragments.settings.UserTeamAccountView

@Dao
abstract class UserTeamAccountDao : MasterDao<UserTeamAccount>() {
    @Query(
        """
            DELETE FROM ${TableName.USER_TEAM_ACCOUNT}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT * 
            FROM ${TableName.USER_TEAM_ACCOUNT}
            """
    )
    abstract suspend fun getAllRaw(): List<UserTeamAccount>

    @Query(
        """
            SELECT * 
            FROM ${TableName.USER_TEAM_ACCOUNT}
            """
    )
    abstract suspend fun getAllRawView(): List<UserTeamAccountView>
}
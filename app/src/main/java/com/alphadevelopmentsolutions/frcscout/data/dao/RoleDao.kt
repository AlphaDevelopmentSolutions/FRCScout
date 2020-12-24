package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.Role
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class RoleDao : MasterDao<Role>() {
    @Query(
        """
            DELETE FROM ${TableName.ROLE}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT r.*
            FROM ${TableName.ROLE} r
            LEFT JOIN ${TableName.USER_ROLE} ur ON ur.role_id = r.id
            WHERE ur.user_team_account_list_id = :userTeamAccountId
        """
    )
    abstract suspend fun getListForUserTeamAccount(userTeamAccountId: ByteArray): List<Role>
}
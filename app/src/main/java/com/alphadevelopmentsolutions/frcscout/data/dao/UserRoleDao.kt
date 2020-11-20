package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.UserRole
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class UserRoleDao : MasterDao<UserRole>() {
    @Query(
        """
            DELETE FROM ${TableName.USER_ROLE}
        """
    )
    abstract fun deleteAll()
}
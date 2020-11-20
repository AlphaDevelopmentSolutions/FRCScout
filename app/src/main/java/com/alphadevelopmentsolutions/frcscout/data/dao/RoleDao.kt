package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
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
}
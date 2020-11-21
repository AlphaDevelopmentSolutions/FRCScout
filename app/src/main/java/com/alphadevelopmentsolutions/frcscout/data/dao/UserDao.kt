package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.User
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class UserDao : MasterDao<User>() {
    @Query(
        """
            DELETE FROM ${TableName.USER}
        """
    )
    abstract fun deleteAll()
}
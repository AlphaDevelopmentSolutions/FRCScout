package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class RobotInfoKeyDao : MasterDao<RobotInfoKey>() {
    @Query(
        """
            DELETE FROM ${TableName.ROBOT_INFO_KEY}
        """
    )
    abstract fun deleteAll()
}
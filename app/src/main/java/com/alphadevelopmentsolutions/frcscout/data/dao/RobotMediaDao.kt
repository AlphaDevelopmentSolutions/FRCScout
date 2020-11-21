package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class RobotMediaDao : MasterDao<RobotMedia>() {
    @Query(
        """
            DELETE FROM ${TableName.ROBOT_MEDIA}
        """
    )
    abstract fun deleteAll()
}
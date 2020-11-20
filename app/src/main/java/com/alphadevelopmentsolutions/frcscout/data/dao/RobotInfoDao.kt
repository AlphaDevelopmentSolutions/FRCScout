package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class RobotInfoDao : MasterDao<RobotInfo>() {
    @Query(
        """
            DELETE FROM ${TableName.ROBOT_INFO}
        """
    )
    abstract fun deleteAll()
}
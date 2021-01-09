package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKeyState
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class RobotInfoKeyStateDao : MasterDao<RobotInfoKeyState>() {
    @Query(
        """
            DELETE FROM ${TableName.ROBOT_INFO_KEY_STATE}
        """
    )
    abstract fun deleteAll()
}
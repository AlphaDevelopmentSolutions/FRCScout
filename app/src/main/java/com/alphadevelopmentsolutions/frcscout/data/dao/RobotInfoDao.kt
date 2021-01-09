package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo.RobotInfoKeyView

@Dao
abstract class RobotInfoDao : MasterDao<RobotInfo>() {
    @Query(
        """
            DELETE FROM ${TableName.ROBOT_INFO}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT *
            FROM robot_info
            WHERE team_id = :teamId
            AND event_id = :eventId
            AND deleted_date IS NULL
        """
    )
    abstract suspend fun getForTeamAtEvent(teamId: ByteArray, eventId: ByteArray): List<RobotInfo>
}
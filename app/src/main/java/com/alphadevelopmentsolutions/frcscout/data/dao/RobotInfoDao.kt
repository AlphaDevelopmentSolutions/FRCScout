package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo.RobotInfoView

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
            SELECT ri.*
            FROM robot_info ri
            LEFT JOIN robot_info_keys rik ON rik.id = ri.key_id
            WHERE ri.event_id = :eventId
            AND ri.team_id = :teamId
            ORDER BY rik.`order` ASC
        """
    )
    abstract suspend fun getForTeamAtEvent(eventId: ByteArray, teamId: ByteArray): List<RobotInfoView>
}
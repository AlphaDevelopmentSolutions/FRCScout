package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.lifecycle.LiveData
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

    @Query(
        """
            SELECT *
            FROM ${TableName.ROBOT_MEDIA}
            WHERE event_id = :eventId
            AND IFNULL(team_id = :teamId, 1)
        """
    )
    abstract fun getForTeam(eventId: ByteArray, teamId: ByteArray?): LiveData<MutableList<RobotMedia>>
}
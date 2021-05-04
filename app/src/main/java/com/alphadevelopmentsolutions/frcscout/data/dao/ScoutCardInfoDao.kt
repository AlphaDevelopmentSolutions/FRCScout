package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class ScoutCardInfoDao : MasterDao<ScoutCardInfo>() {
    @Query(
        """
            DELETE FROM ${TableName.SCOUT_CARD_INFO}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT *
            FROM scout_card_info
            WHERE team_id = :teamId
            AND match_id = :matchId
            AND deleted_date IS NULL
        """
    )
    abstract suspend fun getForTeamInMatch(teamId: ByteArray, matchId: ByteArray): List<ScoutCardInfo>
}
package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.ui.fragments.matches.MatchDatabaseView

@Dao
abstract class MatchDao : MasterDao<Match>() {
    @Query(
        """
            DELETE FROM ${TableName.MATCH}
        """
    )
    abstract fun deleteAll()

    @Transaction
    @Query(
        """
            SELECT ${TableName.MATCH}.* 
            FROM ${TableName.MATCH}
            WHERE ${TableName.MATCH}.event_id = :eventId AND
            IFNULL(
                :teamId
                IN 
                (
                    blue_alliance_team_one_id,
                    blue_alliance_team_two_id,
                    blue_alliance_team_three_id,
                    red_alliance_team_one_id,
                    red_alliance_team_two_id,
                    red_alliance_team_three_id
                ),
                1
            )
            ORDER BY type_id, match_number, set_number 
        """
    )
    abstract fun getForEvent(eventId: ByteArray, teamId: ByteArray?): LiveData<MutableList<MatchDatabaseView>>
}
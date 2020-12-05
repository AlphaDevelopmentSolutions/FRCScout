package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class TeamDao : MasterDao<Team>() {
    @Query(
        """
            DELETE FROM ${TableName.TEAM}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT ${TableName.TEAM}.*
            FROM ${TableName.TEAM}
            LEFT JOIN ${TableName.EVENT_TEAM_LIST} ON ${TableName.EVENT_TEAM_LIST}.team_id = ${TableName.TEAM}.id
            WHERE ${TableName.EVENT_TEAM_LIST}.event_id = :eventId
        """
    )
    abstract fun getAtEvent(eventId: ByteArray): LiveData<MutableList<Team>>
}
package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.EventTeamList
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class EventTeamListDao : MasterDao<EventTeamList>() {
    @Query(
        """
            DELETE FROM ${TableName.EVENT_TEAM_LIST}
        """
    )
    abstract fun deleteAll()
}
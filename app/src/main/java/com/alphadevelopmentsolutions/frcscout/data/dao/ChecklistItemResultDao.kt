package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class ChecklistItemResultDao : MasterDao<ChecklistItemResult>() {
    @Query(
        """
            DELETE FROM ${TableName.CHECKLIST_ITEM_RESULT}
        """
    )
    abstract fun deleteAll()
}
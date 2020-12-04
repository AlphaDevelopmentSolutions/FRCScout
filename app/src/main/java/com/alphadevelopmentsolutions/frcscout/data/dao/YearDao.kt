package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class YearDao : MasterDao<Year>() {
    @Query(
        """
            DELETE FROM ${TableName.YEAR}
        """
    )
    abstract fun deleteAll()

    @Query("SELECT * FROM ${TableName.YEAR} WHERE is_draft = IFNULL(:isDraft, 1) OR is_draft = IFNULL(:isDraft, 0)")
    abstract fun getAllRaw(isDraft: Boolean? = null): List<Year>
}
package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.DataType
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Dao
abstract class DataTypeDao : MasterDao<DataType>() {
    @Query(
        """
            DELETE FROM ${TableName.DATA_TYPE}
        """
    )
    abstract fun deleteAll()
}
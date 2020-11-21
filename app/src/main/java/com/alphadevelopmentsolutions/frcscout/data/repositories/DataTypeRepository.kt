package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.DataTypeDao
import com.alphadevelopmentsolutions.frcscout.data.models.DataType

class DataTypeRepository(private val dao: DataTypeDao) : MasterRepository<DataType>(dao), SubmittableTable<DataType> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<DataType> =
        listOf()
}
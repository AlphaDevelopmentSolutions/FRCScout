package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.dao.ChecklistItemDao
import com.alphadevelopmentsolutions.frcscout.data.dao.PhotoFileDao
import com.alphadevelopmentsolutions.frcscout.data.models.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.data.models.PhotoFile

class PhotoFileRepository(private val dao: PhotoFileDao) : MasterRepository<PhotoFile>(dao) {

    fun getAll() =
        dao.getAll()

    fun getAllRaw() =
        dao.getAllRaw()

    override suspend fun deleteAll() =
        dao.deleteAll()

    suspend fun delete(id: ByteArray) =
        dao.delete(id)
}
package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.data.models.PhotoFile
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import io.reactivex.Flowable

@Dao
abstract class PhotoFileDao : MasterDao<PhotoFile>() {
    @Query("SELECT * FROM ${TableName.PHOTO_FILE}")
    abstract fun getAll(): Flowable<List<PhotoFile>>

    @Query("SELECT * FROM ${TableName.PHOTO_FILE}")
    abstract fun getAllRaw(): List<PhotoFile>

    @Query("DELETE FROM ${TableName.PHOTO_FILE}")
    abstract suspend fun deleteAll()

    @Query("DELETE FROM ${TableName.PHOTO_FILE} WHERE photo_id = :id")
    abstract suspend fun delete(id: ByteArray)
}
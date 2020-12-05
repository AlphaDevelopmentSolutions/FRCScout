package com.alphadevelopmentsolutions.frcscout.data.repositories

import androidx.appcompat.app.AppCompatActivity
import com.alphadevelopmentsolutions.frcscout.api.Api
import com.alphadevelopmentsolutions.frcscout.callbacks.OnProgressCallback
import com.alphadevelopmentsolutions.frcscout.data.dao.RobotMediaDao
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia

class RobotMediaRepository(private val dao: RobotMediaDao) : MasterRepository<RobotMedia>(dao), SubmittableTable<RobotMedia> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<RobotMedia> =
        listOf()

    suspend fun downloadPhotos(objs: MutableList<RobotMedia>, context: AppCompatActivity, onProgressCallback: OnProgressCallback? = null) =
        Api.getPhotos(context, objs, onProgressCallback)
}
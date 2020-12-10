package com.alphadevelopmentsolutions.frcscout.data.repositories

import androidx.appcompat.app.AppCompatActivity
import com.alphadevelopmentsolutions.frcscout.api.Api
import com.alphadevelopmentsolutions.frcscout.callbacks.OnProgressCallback
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.data.dao.RobotMediaDao
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.RobotMedia
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import java.util.*

class RobotMediaRepository(private val dao: RobotMediaDao) : MasterRepository<RobotMedia>(dao), SubmittableTable<RobotMedia> {
    override suspend fun deleteAll() =
        dao.deleteAll()

    override fun getAllRaw(isDraft: Boolean?): List<RobotMedia> =
        listOf()

    override suspend fun delete(obj: RobotMedia) {
        obj.deletedDate = Date()
        obj.deletedById = Account.getInstance(null)?.id

        insert(obj)
    }

    suspend fun downloadPhotos(objs: MutableList<RobotMedia>, context: AppCompatActivity, onProgressCallback: OnProgressCallback? = null) =
        Api.getPhotos(context, objs, onProgressCallback)

    fun getForTeam(event: Event, team: Team?) =
        dao.getForTeam(event.id, team?.id)

    fun getFromId(media: RobotMedia) =
        dao.getFromId(media.id)
}
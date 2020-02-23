package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotInfo
import com.alphadevelopmentsolutions.frcscout.repository.RobotInfoRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch
import java.util.*

class RobotInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            RobotInfoRepository(RDatabase.getInstance(application).robotInfoDao())

    /**
     * Gets all [RobotInfo] objects from the database
     * @see RobotInfoRepository.objs
     */
    val objs by lazy {
        repository.objs
    }

    fun objsViewForTeam(teamId: UUID) = repository.objsViewForTeam(teamId)

    /**
     * Gets all [RobotInfo] objects from the database based on [RobotInfo.id]
     * @param id specified the id to sort the [RobotInfo] object by
     * @see RobotInfoRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    /**
     * Inserts a [RobotInfo] object into the database
     * @see RobotInfoRepository.insert
     */
    fun insert(robotInfo: RobotInfo) = viewModelScope.launch {
        repository.insert(robotInfo)
    }

    /**
     * Inserts a [RobotInfo] object into the database
     * @see RobotInfoRepository.insert
     */
    fun insertAll(robotInfos: List<RobotInfo>) = viewModelScope.launch {
        repository.insertAll(robotInfos)
    }

    fun delete(robotInfo: RobotInfo) {
        repository.delete(robotInfo)
    }
}
package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.Tables.RobotInfo
import com.alphadevelopmentsolutions.frcscout.repository.RobotInfoRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class RobotInfoViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            RobotInfoRepository(RDatabase.getInstance(application).robotInfoDao())

    /**
     * Gets all [RobotInfo] objects from the database
     * @see RobotInfoRepository.objs
     */
    val objs: Flowable<List<RobotInfo>>

    /**
     * Gets all [RobotInfo] objects from the database based on [RobotInfo.id]
     * @param id specified the id to sort the [RobotInfo] object by
     * @see RobotInfoRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

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
}
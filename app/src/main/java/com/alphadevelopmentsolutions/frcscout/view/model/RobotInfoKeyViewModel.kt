package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.repository.RobotInfoKeyRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class RobotInfoKeyViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            RobotInfoKeyRepository(RDatabase.getInstance(application).robotInfoKeyDao())

    /**
     * Gets all [RobotInfoKey] objects from the database
     * @see RobotInfoKeyRepository.objs
     */
    val objs: Flowable<List<RobotInfoKey>>

    /**
     * Gets all [RobotInfoKey] objects from the database based on [RobotInfoKey.id]
     * @param id specified the id to sort the [RobotInfoKey] object by
     * @see RobotInfoKeyRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [RobotInfoKey] object into the database
     * @see RobotInfoKeyRepository.insert
     */
    fun insert(robotInfoKey: RobotInfoKey) = viewModelScope.launch {
        repository.insert(robotInfoKey)
    }

    /**
     * Inserts a [RobotInfoKey] object into the database
     * @see RobotInfoKeyRepository.insert
     */
    fun insertAll(robotInfoKeys: List<RobotInfoKey>) = viewModelScope.launch {
        repository.insertAll(robotInfoKeys)
    }
}
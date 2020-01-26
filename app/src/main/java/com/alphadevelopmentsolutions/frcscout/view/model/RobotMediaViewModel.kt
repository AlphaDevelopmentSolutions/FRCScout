package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.Tables.RobotMedia
import com.alphadevelopmentsolutions.frcscout.repository.RobotMediaRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class RobotMediaViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            RobotMediaRepository(RDatabase.getInstance(application).robotMediaDao())

    /**
     * Gets all [RobotMedia] objects from the database
     * @see RobotMediaRepository.objs
     */
    val objs: Flowable<List<RobotMedia>>

    /**
     * Gets all [RobotMedia] objects from the database based on [RobotMedia.id]
     * @param id specified the id to sort the [RobotMedia] object by
     * @see RobotMediaRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [RobotMedia] object into the database
     * @see RobotMediaRepository.insert
     */
    fun insert(robotMedia: RobotMedia) = viewModelScope.launch {
        repository.insert(robotMedia)
    }

    /**
     * Inserts a [RobotMedia] object into the database
     * @see RobotMediaRepository.insert
     */
    fun insertAll(robotMedias: List<RobotMedia>) = viewModelScope.launch {
        repository.insertAll(robotMedias)
    }
}
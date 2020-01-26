package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.repository.ChecklistItemResultRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class ChecklistItemResultViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            ChecklistItemResultRepository(RDatabase.getInstance(application).checklistItemResultDao())

    /**
     * Gets all [ChecklistItemResult] objects from the database
     * @see ChecklistItemResultRepository.objs
     */
    val objs: Flowable<List<ChecklistItemResult>>

    /**
     * Gets all [ChecklistItemResult] objects from the database based on [ChecklistItemResult.id]
     * @param id specified the id to sort the [ChecklistItemResult] object by
     * @see ChecklistItemResultRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [ChecklistItemResult] object into the database
     * @see ChecklistItemResultRepository.insert
     */
    fun insert(checklistItemResult: ChecklistItemResult) = viewModelScope.launch {
        repository.insert(checklistItemResult)
    }

    /**
     * Inserts a [ChecklistItemResult] object into the database
     * @see ChecklistItemResultRepository.insert
     */
    fun insertAll(checklistItemResults: List<ChecklistItemResult>) = viewModelScope.launch {
        repository.insertAll(checklistItemResults)
    }
}
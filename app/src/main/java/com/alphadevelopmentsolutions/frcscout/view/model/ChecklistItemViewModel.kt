package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Year
import com.alphadevelopmentsolutions.frcscout.repository.YearRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class ChecklistItemViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            YearRepository(RDatabase.getInstance(application).yearDao())

    /**
     * Gets all [Year] objects from the database
     * @see YearRepository.objs
     */
    val objs: Flowable<List<Year>>

    /**
     * Gets all [Year] objects from the database based on [Year.id]
     * @param id specified the id to sort the [Year] object by
     * @see YearRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [Year] object into the database
     * @see YearRepository.insert
     */
    fun insert(year: Year) = viewModelScope.launch {
        repository.insert(year)
    }

    /**
     * Inserts a [Year] object into the database
     * @see YearRepository.insert
     */
    fun insertAll(years: List<Year>) = viewModelScope.launch {
        repository.insertAll(years)
    }
}
package com.alphadevelopmentsolutions.frcscout.view.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.classes.table.account.User
import com.alphadevelopmentsolutions.frcscout.repository.UserRepository
import io.reactivex.Flowable
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    private val repository =
            UserRepository(RDatabase.getInstance(application).userDao())

    /**
     * Gets all [User] objects from the database
     * @see UserRepository.objs
     */
    val objs: Flowable<List<User>>

    /**
     * Gets all [User] objects from the database based on [User.id]
     * @param id specified the id to sort the [User] object by
     * @see UserRepository.objWithId
     */
    fun objWithId(id: String) = repository.objWithId(id)

    init {
        objs = repository.objs
    }

    /**
     * Inserts a [User] object into the database
     * @see UserRepository.insert
     */
    fun insert(user: User) = viewModelScope.launch {
        repository.insert(user)
    }

    /**
     * Inserts a [User] object into the database
     * @see UserRepository.insert
     */
    fun insertAll(users: List<User>) = viewModelScope.launch {
        repository.insertAll(users)
    }

    suspend fun clearData() = repository.clearData()
}
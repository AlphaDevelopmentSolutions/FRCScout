package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.Tables.User
import com.alphadevelopmentsolutions.frcscout.dao.UserDao
import io.reactivex.Flowable


class UserRepository(private val userDao: UserDao) {

    /**
     * Gets all [User] objects from the database
     * @see UserDao.getObjs
     */
    val objs: Flowable<List<User>> = userDao.getObjs()

    /**
     * Gets all [User] objects from the database based on [User.id]
     * @param id specified the id to sort the [User] object by
     * @see UserDao.getObjWithId
     */
    fun objWithId(id: String) = userDao.getObjWithId(id)

    /**
     * Inserts a [User] object into the database
     * @see UserDao.insert
     */
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    /**
     * Inserts a [User] object into the database
     * @see UserDao.insertAll
     */
    suspend fun insertAll(users: List<User>) {
        userDao.insertAll(users)
    }

}
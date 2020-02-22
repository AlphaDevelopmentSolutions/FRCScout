package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.alphadevelopmentsolutions.frcscout.classes.table.account.User
import io.reactivex.Flowable

@Dao
interface UserDao {

    /**
     * Gets all [User] objects from the database
     */
    @Query("SELECT * FROM users")
    fun getObjs(): Flowable<List<User>>

    /**
     * Gets all [User] objects from the database based on [User.id]
     * @param id specified the id to sort the [User] object by
     */
    @Query("SELECT * FROM users where id = :id")
    fun getObjWithId(id: String): Flowable<User>

    /**
     * Inserts a new [User] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)

    /**
     * Inserts a list of [User] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(users: List<User>)

    /**
     * Deletes all [User] objects from the database
     */
    @Query("DELETE FROM users")
    suspend fun clear()
}
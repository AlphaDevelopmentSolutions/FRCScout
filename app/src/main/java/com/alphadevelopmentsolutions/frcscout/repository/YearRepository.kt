package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.table.core.Year
import com.alphadevelopmentsolutions.frcscout.dao.YearDao
import io.reactivex.Flowable


class YearRepository(private val yearDao: YearDao) {

    /**
     * Gets all [Year] objects from the database
     * @see YearDao.getObjs
     */
    val objs: Flowable<List<Year>> = yearDao.getObjs()

    /**
     * Gets all [Year] objects from the database based on [Year.id]
     * @param id specified the id to sort the [Year] object by
     * @see YearDao.getObjWithId
     */
    fun objWithId(id: String) = yearDao.getObjWithId(id)

    /**
     * Inserts a [Year] object into the database
     * @see YearDao.insert
     */
    suspend fun insert(listing: Year) {
        yearDao.insert(listing)
    }

    /**
     * Inserts a [Year] object into the database
     * @see YearDao.insertAll
     */
    suspend fun insertAll(listings: List<Year>) {
        yearDao.insertAll(listings)
    }

}
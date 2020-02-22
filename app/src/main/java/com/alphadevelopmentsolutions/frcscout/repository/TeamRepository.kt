package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team
import com.alphadevelopmentsolutions.frcscout.dao.TeamDao
import io.reactivex.Flowable


class TeamRepository(private val teamDao: TeamDao) {

    /**
     * Gets all [Team] objects from the database
     * @see TeamDao.getObjs
     */
    val objs: Flowable<List<Team>> = teamDao.getObjs()

    /**
     * Gets all [Team] objects from the database based on [Team.id]
     * @param id specified the id to sort the [Team] object by
     * @see TeamDao.getObjWithId
     */
    fun objWithId(id: String) = teamDao.getObjWithId(id)

    /**
     * Inserts a [Team] object into the database
     * @see TeamDao.insert
     */
    suspend fun insert(team: Team) {
        teamDao.insert(team)
    }

    /**
     * Inserts a [Team] object into the database
     * @see TeamDao.insertAll
     */
    suspend fun insertAll(teams: List<Team>) {
        teamDao.insertAll(teams)
    }

}
package com.alphadevelopmentsolutions.frcscout.repository

import androidx.sqlite.db.SimpleSQLiteQuery
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.enums.SortDirection
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import io.reactivex.Flowable
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList


class MatchRepository(private val matchDao: MatchDao) {

    /**
     * Gets all [Match] objects from the database
     * @see MatchDao.getObjs
     */
    val objs: Flowable<List<Match>> = matchDao.getObjs()

    /**
     * Gets all [Match] objects from the database based on [Match.id]
     * @param id specified the id to sort the [Match] object by
     * @see MatchDao.getObjWithId
     */
    fun objWithId(id: String) = matchDao.getObjWithId(id)

    fun objWithCustom(eventId: UUID?, matchId: UUID?, teamId: UUID?, sortDirection: SortDirection = SortDirection.DESC): Flowable<List<Match>>? {
        var returnFlowable: Flowable<List<Match>>? = null

        Match.Type.getTypes().forEach {

            val query = StringBuilder()
            val args = ArrayList<Any>()

            query.append("SELECT * FROM ${Constants.TableNames.MATCH} WHERE 1=1 ")

            if (eventId != null) {
                query.append(" AND eventId = ? ")
                args.add(eventId.blueAllianceId)
            }

            if (matchId != null) {
                query.append(" AND `key` = ? ")
                args.add(matchId.key)
            }

            if (teamId != null) {
                query
                        .append(" AND ${teamId.id} IN (")

                        .append("blueAllianceTeamOneId, ")
                        .append("blueAllianceTeamTwoId, ")
                        .append("blueAllianceTeamThreeId, ")

                        .append("redAllianceTeamOneId, ")
                        .append("redAllianceTeamTwoId, ")
                        .append("redAllianceTeamThreeId)")
            }

            query.append(" matchType = ? ")
            args.add(it.toString())


            query.append(" ORDER BY matchNumber ${sortDirection.name}")


            with(returnFlowable)
            {
                when (this) {
                    null -> returnFlowable = matchDao.getObjsWithCustom(SimpleSQLiteQuery(query.toString(), args.toArray()))
                    else -> mergeWith(matchDao.getObjsWithCustom(SimpleSQLiteQuery(query.toString(), args.toArray())))
                }
            }
        }

        return returnFlowable
    }


    /**
     * Inserts a [Match] object into the database
     * @see MatchDao.insert
     */
    suspend fun insert(match: Match) {
        matchDao.insert(match)
    }

    /**
     * Inserts a [Match] object into the database
     * @see MatchDao.insertAll
     */
    suspend fun insertAll(matchs: List<Match>) {
        matchDao.insertAll(matchs)
    }

}
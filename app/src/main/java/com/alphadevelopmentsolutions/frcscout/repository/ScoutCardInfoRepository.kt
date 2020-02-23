package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.dao.ScoutCardInfoDao
import com.alphadevelopmentsolutions.frcscout.view.database.ScoutCardInfoDatabaseView
import io.reactivex.Flowable
import java.util.*


class ScoutCardInfoRepository(private val scoutCardInfoDao: ScoutCardInfoDao) {

    /**
     * Gets all [ScoutCardInfo] objects from the database
     * @see ScoutCardInfoDao.getObjs
     */
    val objs: Flowable<List<ScoutCardInfo>> = scoutCardInfoDao.getObjs()

    /**
     * Gets all [ScoutCardInfo] objects from the database based on [ScoutCardInfo.id]
     * @param id specified the id to sort the [ScoutCardInfo] object by
     * @see ScoutCardInfoDao.getObjWithId
     */
    fun objWithId(id: String) = scoutCardInfoDao.getObjWithId(id)

    fun objsViewForTeam(teamId: UUID, matchId: UUID) = scoutCardInfoDao.getObjsViewForTeam(teamId, matchId)
    fun objsViewForEvent(eventId: UUID) = scoutCardInfoDao.getObjsViewForEvent(eventId)
    fun objsViewForMatch(matchId: UUID) = scoutCardInfoDao.getObjsViewForMatch(matchId)


    /**
     * Inserts a [ScoutCardInfo] object into the database
     * @see ScoutCardInfoDao.insert
     */
    suspend fun insert(scoutCardInfo: ScoutCardInfo) {
        scoutCardInfoDao.insert(scoutCardInfo)
    }

    /**
     * Inserts a [ScoutCardInfo] object into the database
     * @see ScoutCardInfoDao.insertAll
     */
    suspend fun insertAll(scoutCardInfos: List<ScoutCardInfo>) {
        scoutCardInfoDao.insertAll(scoutCardInfos)
    }

    fun delete(scoutCardInfo: ScoutCardInfo) = scoutCardInfoDao.delete(scoutCardInfo)

    fun calculateStats(scoutCardInfoViewList: List<ScoutCardInfoDatabaseView>): HashMap<String, Double> {

        val statsHashMap = HashMap<String, Double>()

        if(scoutCardInfoViewList.isNotEmpty())
        {
            for(scoutCardInfoView in scoutCardInfoViewList)
            {
                if(scoutCardInfoView.scoutCardInfoKey.includeInStats)
                {
                    if(scoutCardInfoView.scoutCardInfo.isNotEmpty())
                    {
                        var cardCount = 0
                        var totalStats = 0.0

                        scoutCardInfoView.scoutCardInfo.forEach { scoutCardInfo ->

                            val stat = Integer.parseInt(scoutCardInfo.value)

                            if(!scoutCardInfoView.scoutCardInfoKey.nullZeros || (scoutCardInfoView.scoutCardInfoKey.nullZeros && stat != 0))
                            {
                                totalStats += stat
                                cardCount++
                            }
                        }


                        statsHashMap[scoutCardInfoView.scoutCardInfoKey.toString()] = if(cardCount != 0) Math.round((totalStats / cardCount) * 100.00) / 100.00 else 0.0
                    }

                    //default stats
                    else
                        statsHashMap[scoutCardInfoView.scoutCardInfoKey.toString()] = 0.0

                }
            }
        }

        return statsHashMap
    }

    suspend fun clearData() = scoutCardInfoDao.clear()
}
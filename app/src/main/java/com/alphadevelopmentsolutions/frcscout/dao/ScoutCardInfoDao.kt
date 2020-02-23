package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.*
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.view.database.ScoutCardInfoDatabaseView
import io.reactivex.Flowable
import java.util.*

@Dao
interface ScoutCardInfoDao {

    /**
     * Gets all [ScoutCardInfo] objects from the database
     */
    @Query("SELECT * FROM scout_card_info")
    fun getObjs(): Flowable<List<ScoutCardInfo>>

    /**
     * Gets all [ScoutCardInfo] objects from the database based on [ScoutCardInfo.id]
     * @param id specified the id to sort the [ScoutCardInfo] object by
     */
    @Query("SELECT * FROM scout_card_info where id = :id")
    fun getObjWithId(id: String): Flowable<ScoutCardInfo>

    @Query(
            """
                SELECT * FROM ${TableName.SCOUT_CARD_INFO_KEY}
                LEFT JOIN ${TableName.SCOUT_CARD_INFO} ON ${TableName.SCOUT_CARD_INFO}.keyId = ${TableName.SCOUT_CARD_INFO_KEY}.id
                WHERE ${TableName.SCOUT_CARD_INFO}.teamId = :teamId AND ${TableName.SCOUT_CARD_INFO}.matchId  = :matchId
            """
    )
    fun getObjsViewForTeam(teamId: UUID, matchId: UUID): Flowable<List<ScoutCardInfoDatabaseView>>

    @Query(
            """
                SELECT * FROM ${TableName.SCOUT_CARD_INFO_KEY}
                LEFT JOIN ${TableName.SCOUT_CARD_INFO} ON ${TableName.SCOUT_CARD_INFO}.keyId = ${TableName.SCOUT_CARD_INFO_KEY}.id
                LEFT JOIN ${TableName.MATCH} ON ${TableName.MATCH}.id = ${TableName.SCOUT_CARD_INFO}.matchId
                WHERE ${TableName.MATCH}.eventId = :eventId
            """
    )
    fun getObjsViewForEvent(eventId: UUID): List<ScoutCardInfoDatabaseView>

    @Query(
            """
                SELECT * FROM ${TableName.SCOUT_CARD_INFO_KEY}
                LEFT JOIN ${TableName.SCOUT_CARD_INFO} ON ${TableName.SCOUT_CARD_INFO}.keyId = ${TableName.SCOUT_CARD_INFO_KEY}.id
                WHERE ${TableName.SCOUT_CARD_INFO}.matchId = :matchId
            """
    )
    fun getObjsViewForMatch(matchId: UUID): List<ScoutCardInfoDatabaseView>


    /**
     * Inserts a new [ScoutCardInfo] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scoutCardInfo: ScoutCardInfo)

    /**
     * Inserts a list of [ScoutCardInfo] object into the database
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(scoutCardInfos: List<ScoutCardInfo>)

    /**
     * Deletes all [ScoutCardInfo] objects from the database
     */
    @Query("DELETE FROM scout_card_info")
    suspend fun clear()

    @Delete
    fun delete(scoutCardInfo: ScoutCardInfo)
}
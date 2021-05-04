package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alphadevelopmentsolutions.frcscout.data.models.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo.RobotInfoKeyView
import com.alphadevelopmentsolutions.frcscout.ui.fragments.scoutcard.ScoutCardInfoKeyView

@Dao
abstract class ScoutCardInfoKeyDao : MasterDao<ScoutCardInfoKey>() {
    @Query(
        """
            DELETE FROM ${TableName.SCOUT_CARD_INFO_KEY}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT scik.*
            FROM scout_card_info_keys scik
            LEFT JOIN scout_card_info_key_states sciks ON sciks.id = scik.state_id
            WHERE sciks.year_id = :yearId
            AND sciks.team_account_id = :teamAccountId
            ORDER BY scik.`order` ASC
        """
    )
    @Transaction
    abstract suspend fun getList(yearId: ByteArray, teamAccountId: ByteArray): List<ScoutCardInfoKeyView>
}
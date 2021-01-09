package com.alphadevelopmentsolutions.frcscout.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo.RobotInfoKeyView

@Dao
abstract class RobotInfoKeyDao : MasterDao<RobotInfoKey>() {
    @Query(
        """
            DELETE FROM ${TableName.ROBOT_INFO_KEY}
        """
    )
    abstract fun deleteAll()

    @Query(
        """
            SELECT rik.*
            FROM robot_info_keys rik
            LEFT JOIN robot_info_key_states riks ON riks.id = rik.state_id
            WHERE riks.year_id = :yearId
            AND riks.team_account_id = :teamAccountId
            ORDER BY rik.`order` ASC
        """
    )
    @Transaction
    abstract suspend fun getList(yearId: ByteArray, teamAccountId: ByteArray): List<RobotInfoKeyView>
}
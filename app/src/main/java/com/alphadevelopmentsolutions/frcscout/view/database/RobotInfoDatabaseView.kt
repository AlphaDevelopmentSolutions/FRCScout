package com.alphadevelopmentsolutions.frcscout.view.database

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.account.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team


class RobotInfoDatabaseView(
        @Embedded val robotInfoKey: RobotInfoKey,
        @Relation(parentColumn = "id", entityColumn = "keyId", entity = RobotInfo::class) val robotInfo: RobotInfo
)
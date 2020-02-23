package com.alphadevelopmentsolutions.frcscout.view.database

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.classes.table.account.*
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team


class ScoutCardInfoDatabaseView(
        @Embedded val scoutCardInfoKey: ScoutCardInfoKey,
        @Relation(parentColumn = "id", entityColumn = "keyId", entity = ScoutCardInfo::class) val scoutCardInfo: List<ScoutCardInfo>
)
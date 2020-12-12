package com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKeyState

class RobotInfoView(
    @Embedded val robotInfo: RobotInfo,
    @Relation(parentColumn = "key_id", entityColumn = "id", entity = RobotInfoKey::class) val robotInfoKeyView: RobotInfoKeyView
)
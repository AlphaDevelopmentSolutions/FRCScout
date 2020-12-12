package com.alphadevelopmentsolutions.frcscout.ui.fragments.robotinfo

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.data.models.DataType
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfo
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKey
import com.alphadevelopmentsolutions.frcscout.data.models.RobotInfoKeyState

class RobotInfoKeyView(
    @Embedded val robotInfoKey: RobotInfoKey,
    @Relation(parentColumn = "state_id", entityColumn = "id", entity = RobotInfoKeyState::class) val robotInfoKeyState: RobotInfoKeyState,
    @Relation(parentColumn = "data_type_id", entityColumn = "id", entity = DataType::class) val dataType: DataType,
)
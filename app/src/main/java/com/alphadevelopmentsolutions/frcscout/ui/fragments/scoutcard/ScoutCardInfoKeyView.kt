package com.alphadevelopmentsolutions.frcscout.ui.fragments.scoutcard

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.data.models.*

class ScoutCardInfoKeyView(
    @Embedded val scoutCardInfoKey: ScoutCardInfoKey,
    @Relation(parentColumn = "state_id", entityColumn = "id", entity = ScoutCardInfoKeyState::class) val scoutCardInfoKeyState: ScoutCardInfoKeyState,
    @Relation(parentColumn = "data_type_id", entityColumn = "id", entity = DataType::class) val dataType: DataType,
)
package com.alphadevelopmentsolutions.frcscout.view.database

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItemResult


class ChecklistItemDatabaseView(
        @Embedded val checklistItem: ChecklistItem,
        @Relation(parentColumn = "id", entityColumn = "checklistItemId", entity = ChecklistItemResult::class) val checklistItemResult: ChecklistItemResultDatabaseView? = null
)
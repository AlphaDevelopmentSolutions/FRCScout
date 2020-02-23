package com.alphadevelopmentsolutions.frcscout.view.database

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.classes.table.account.User


class ChecklistItemResultDatabaseView(
        @Embedded val checklistItemResult: ChecklistItemResult,
        @Relation(parentColumn = "completedBy", entityColumn = "id", entity = User::class) val completedBy: User
)
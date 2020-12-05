package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

@Entity(tableName = TableName.LOG_DRAFT)
class LogDraft(
    @ColumnInfo(name = "log_draft", defaultValue = "1") var logDraft: Boolean = true,
    @PrimaryKey @ColumnInfo(name = "id", index = true, defaultValue = "1") val id: Int = 1
)
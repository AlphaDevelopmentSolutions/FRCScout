package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.FormattableDate
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.enums.Status
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TableName.CHECKLIST_ITEM_RESULT)
class ChecklistItemResult(
        @SerializedName("checklist_item_id") var checklistItemId: UUID,
        @SerializedName("match_id") var matchId: UUID,
        var status: Status,
        @SerializedName("completed_by") var completedBy: UUID,
        @SerializedName("completed_date") var completedDate: FormattableDate
) : Table()
{


    /**
     * @see Table.toString
     */
    override fun toString() = ""
}

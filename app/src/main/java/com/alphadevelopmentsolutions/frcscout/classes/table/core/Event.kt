package com.alphadevelopmentsolutions.frcscout.classes.table.core

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.FormattableDate
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.math.round

@Entity(tableName = TableName.EVENT)
class Event(
        @SerializedName("year_id") var yearId: UUID,
        @SerializedName("blue_alliance_id") var blueAllianceId: String,
        var name: String,
        var city: String? = null,
        @SerializedName("state_province") var stateProvince: String? = null,
        var country: String? = null,
        @SerializedName("start_time") var startTime: FormattableDate? = null,
        @SerializedName("end_time") var endTime: FormattableDate? = null
) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString() = name
}

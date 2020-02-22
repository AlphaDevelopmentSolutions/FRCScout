package com.alphadevelopmentsolutions.frcscout.classes.table.account

import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = TableName.SCOUT_CARD_INFO_KEY)
class ScoutCardInfoKey(
        @SerializedName("year_id") var yearId: UUID,
        var state: String,
        var name: String,
        var order: Int,
        var min: Int? = null,
        var max: Int? = null,
        @SerializedName("null_zeros") var nullZeros: Boolean? = null,
        @SerializedName("include_in_stats") var includeInStats: Boolean? = null,
        @SerializedName("data_type") var dataType: DataType
) : Table()
{
    enum class DataType
    {
        BOOL,
        INT,
        TEXT;

        companion object
        {
            /**
             * Parses the string into a datatype format
             * @param type string of type to parse
             * @return datatype
             */
            fun fromString(type: String): DataType
            {
                return when
                {
                    type.toUpperCase() == BOOL.name -> BOOL
                    type.toUpperCase() == INT.name -> INT
                    type.toUpperCase() == TEXT.name -> TEXT
                    else -> BOOL
                }

            }
        }
    }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "$state $name"
    }
}

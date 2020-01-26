package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import java.util.*

@Entity(tableName = "scout_card_info_keys")
class ScoutCardInfoKey(
        var yearId: UUID,
        var keyState: String = DEFAULT_STRING,
        var keyName: String = DEFAULT_STRING,
        var sortOrder: Int = DEFAULT_INT,
        var minValue: Int? = null,
        var maxValue: Int? = null,
        var nullZeros: Boolean? = null,
        var includeInStats: Boolean? = null,
        var dataType: DataTypes = DataTypes.TEXT) : Table()
{
    enum class DataTypes
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
            fun parseString(type: String): DataTypes
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
        return "$keyState $keyName"
    }
}

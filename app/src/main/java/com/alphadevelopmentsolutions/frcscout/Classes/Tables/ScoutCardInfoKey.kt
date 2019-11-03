package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*

class ScoutCardInfoKey(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var yearId: Int = DEFAULT_INT,
        var keyState: String = DEFAULT_STRING,
        var keyName: String = DEFAULT_STRING,
        var sortOrder: Int = DEFAULT_INT,
        var minValue: Int? = null,
        var maxValue: Int? = null,
        var nullZeros: Boolean? = null,
        var includeInStats: Boolean? = null,
        var dataType: DataTypes = DataTypes.TEXT) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "scout_card_info_keys"
        
        const val COLUMN_NAME_YEAR_ID = "YearId"
        const val COLUMN_NAME_KEY_STATE = "KeyState"
        const val COLUMN_NAME_KEY_NAME = "KeyName"
        const val COLUMN_NAME_SORT_ORDER = "SortOrder"
        const val COLUMN_NAME_MIN_VALUE = "MinValue"
        const val COLUMN_NAME_MAX_VALUE = "MaxValue"
        const val COLUMN_NAME_NULL_ZEROS = "NullZeros"
        const val COLUMN_NAME_INCLUDE_IN_STATS = "IncludeInStats"
        const val COLUMN_NAME_DATA_TYPE = "DataType"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_YEAR_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_KEY_STATE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_KEY_NAME, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_SORT_ORDER, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_MIN_VALUE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_MAX_VALUE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_NULL_ZEROS, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_INCLUDE_IN_STATS, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_DATA_TYPE, SQLiteDataTypes.TEXT))
            }

        /**
         * Returns [ArrayList] of [ScoutCardInfoKey] with specified filters from [database]
         * @param year if specified, filters [ScoutCardInfoKey] by [year] id
         * @param scoutCardInfoKey if specified, filters [ScoutCardInfoKey] by [scoutCardInfoKey] id
         * @param database used to load [ScoutCardInfoKey]
         * @return [ArrayList] of [ScoutCardInfoKey]
         */
        fun getObjects(year: Year?, scoutCardInfoKey: ScoutCardInfoKey?, database: Database): ArrayList<ScoutCardInfoKey>
        {
            return ArrayList<ScoutCardInfoKey>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (year != null)
                {
                    whereStatement.append("$COLUMN_NAME_YEAR_ID = ? ")
                    whereArgs.add(year.serverId.toString())
                }

                //filter by object
                if (scoutCardInfoKey != null)
                {
                    whereStatement.append(if (whereStatement.isNotEmpty()) " AND " else "").append("${if(scoutCardInfoKey.localId != DEFAULT_LONG) COLUMN_NAME_LOCAL_ID else COLUMN_NAME_SERVER_ID} = ? ")
                    whereArgs.add(
                            if(scoutCardInfoKey.localId != DEFAULT_LONG)
                                scoutCardInfoKey.localId.toString()
                            else
                                scoutCardInfoKey.serverId.toString()
                    )
                }

                //add all object records to array list
                with(database.getObjects(
                        TABLE_NAME,
                        whereStatement.toString(),
                        whereArgs))
                {
                    if (this != null) {
                        while (moveToNext()) {
                            add(
                                    ScoutCardInfoKey(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getInt(COLUMN_NAME_YEAR_ID),
                                            getString(COLUMN_NAME_KEY_STATE),
                                            getString(COLUMN_NAME_KEY_NAME),
                                            getInt(COLUMN_NAME_SORT_ORDER),
                                            getIntOrNull(COLUMN_NAME_MIN_VALUE),
                                            getIntOrNull(COLUMN_NAME_MAX_VALUE),
                                            getBooleanOrNull(COLUMN_NAME_NULL_ZEROS),
                                            getBooleanOrNull(COLUMN_NAME_INCLUDE_IN_STATS),
                                            DataTypes.parseString(getString(COLUMN_NAME_DATA_TYPE))
                                    )
                            )
                        }

                        close()
                    }
                }
            }
        }
    }

    enum class DataTypes {
        BOOL,
        INT,
        TEXT;

        companion object {
            /**
             * Parses the string into a datatype format
             * @param type string of type to parse
             * @return datatype
             */
            fun parseString(type: String): DataTypes {
                return when {
                    type.toUpperCase() == BOOL.name -> BOOL
                    type.toUpperCase() == INT.name -> INT
                    type.toUpperCase() == TEXT.name -> TEXT
                    else -> BOOL
                }
            }
        }
    }

    /**
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(null, this, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    this@ScoutCardInfoKey.loadParentValues(this)
                    this@ScoutCardInfoKey.yearId = yearId
                    this@ScoutCardInfoKey.sortOrder = sortOrder
                    this@ScoutCardInfoKey.keyState = keyState
                    this@ScoutCardInfoKey.keyName = keyName
                    this@ScoutCardInfoKey.minValue = minValue
                    this@ScoutCardInfoKey.maxValue = minValue
                    this@ScoutCardInfoKey.nullZeros = nullZeros
                    this@ScoutCardInfoKey.includeInStats = includeInStats
                    this@ScoutCardInfoKey.dataType = dataType
                }
            }
        }

        return false
    }

    /**
     * @see Table.childValues
     */
    override val childValues: MasterContentValues
        get() = MasterContentValues().apply {
            put(COLUMN_NAME_YEAR_ID, yearId)
            put(COLUMN_NAME_KEY_STATE, keyState)
            put(COLUMN_NAME_KEY_NAME, keyName)
            put(COLUMN_NAME_SORT_ORDER, sortOrder)
            put(COLUMN_NAME_MIN_VALUE, minValue)
            put(COLUMN_NAME_MAX_VALUE, maxValue)
            put(COLUMN_NAME_NULL_ZEROS, nullZeros)
            put(COLUMN_NAME_INCLUDE_IN_STATS, includeInStats)
            put(COLUMN_NAME_DATA_TYPE, dataType.toString())
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return "$keyState $keyName"
    }

}

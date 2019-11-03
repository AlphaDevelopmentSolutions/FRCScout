package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes

class Robot(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var name: String = DEFAULT_STRING,
        var teamNumber: Int = DEFAULT_INT) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "robots"
        const val COLUMN_NAME_NAME = "Name"
        const val COLUMN_NAME_TEAM_NUMBER = "TeamId"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_NAME, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_TEAM_NUMBER, SQLiteDataTypes.INTEGER))
            }
    }

    /**
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        return false
    }

    /**
     * @see Table.childValues
     */
    override val childValues: MasterContentValues
        get() = MasterContentValues().apply {
            put(COLUMN_NAME_NAME, name)
            put(COLUMN_NAME_TEAM_NUMBER, teamNumber)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return name
    }
}

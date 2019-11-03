package com.alphadevelopmentsolutions.frcscout.Classes.Tables

import com.alphadevelopmentsolutions.frcscout.Classes.Database
import com.alphadevelopmentsolutions.frcscout.Classes.MasterContentValues
import com.alphadevelopmentsolutions.frcscout.Classes.TableColumn
import com.alphadevelopmentsolutions.frcscout.Interfaces.ChildTableCompanion
import com.alphadevelopmentsolutions.frcscout.Interfaces.SQLiteDataTypes
import java.util.*
import kotlin.math.round

class Event(
        localId: Long = DEFAULT_LONG,
        serverId: Long = DEFAULT_LONG,
        var yearId: Int = DEFAULT_INT,
        var blueAllianceId: String = DEFAULT_STRING,
        var name: String = DEFAULT_STRING,
        var city: String = DEFAULT_STRING,
        var stateProvince: String = DEFAULT_STRING,
        var country: String = DEFAULT_STRING,
        var startDate: Date = DEFAULT_DATE,
        var endDate: Date = DEFAULT_DATE) : Table(TABLE_NAME, localId, serverId)
{
    companion object: ChildTableCompanion
    {
        override val TABLE_NAME = "events"
        const val COLUMN_NAME_YEAR_ID = "YearId"
        const val COLUMN_NAME_BLUE_ALLIANCE_ID = "BlueAllianceId"
        const val COLUMN_NAME_NAME = "Name"
        const val COLUMN_NAME_CITY = "City"
        const val COLUMN_NAME_STATEPROVINCE = "StateProvince"
        const val COLUMN_NAME_COUNTRY = "Country"
        const val COLUMN_NAME_START_DATE = "StartDate"
        const val COLUMN_NAME_END_DATE = "EndDate"

        override val childColumns: ArrayList<TableColumn>
            get() = ArrayList<TableColumn>().apply {
                add(TableColumn(COLUMN_NAME_YEAR_ID, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_BLUE_ALLIANCE_ID, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_NAME, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_CITY, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_STATEPROVINCE, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_COUNTRY, SQLiteDataTypes.TEXT))
                add(TableColumn(COLUMN_NAME_START_DATE, SQLiteDataTypes.INTEGER))
                add(TableColumn(COLUMN_NAME_END_DATE, SQLiteDataTypes.INTEGER))
            }

        /**
         * Returns [ArrayList] of [Event] with specified filters from [database]
         * @param year if specified, filters [Event] by [year] id
         * @param event if specified, filters [Event] by [event] id
         * @param team if specified, filters [Event] by [team] id
         * @param database used to load [Event]
         * @return [ArrayList] of [Event]
         */
        fun getObjects(year: Year?, event: Event?, team: Team?, database: Database): ArrayList<Event> {
            return ArrayList<Event>().apply {

                val whereStatement = StringBuilder()
                val whereArgs = ArrayList<String>()

                //filter by object
                if (year != null)
                {
                    whereStatement.append("$COLUMN_NAME_YEAR_ID = ?")
                    whereArgs.add(year.serverId.toString())
                }

                //filter by object
                if (event != null)
                {
                    whereStatement
                            .append(if (whereStatement.isNotEmpty()) " AND " else "")
                            .append("$Event.COLUMN_NAME_ID = ?")
                    whereArgs.add(event.localId.toString())
                }

                //filter by object
                if (team != null)
                {
                    whereStatement
                            .append(if (whereStatement.isNotEmpty()) " AND " else "")
                            .append("$COLUMN_NAME_BLUE_ALLIANCE_ID IN (SELECT ${EventTeamList.COLUMN_NAME_EVENT_ID} FROM ${EventTeamList.TABLE_NAME} WHERE ${EventTeamList.COLUMN_NAME_TEAM_ID} = ?)")
                    whereArgs.add(team.serverId.toString())
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
                                    Event(
                                            getLong(COLUMN_NAME_LOCAL_ID),
                                            getLong(COLUMN_NAME_SERVER_ID),
                                            getInt(COLUMN_NAME_YEAR_ID),
                                            getString(COLUMN_NAME_BLUE_ALLIANCE_ID),
                                            getString(COLUMN_NAME_NAME),
                                            getString(COLUMN_NAME_CITY),
                                            getString(COLUMN_NAME_STATEPROVINCE),
                                            getString(COLUMN_NAME_COUNTRY),
                                            getDate(COLUMN_NAME_START_DATE),
                                            getDate(COLUMN_NAME_END_DATE)
                                    )
                            )
                        }

                        close()
                    }
                }
            }
        }
    }

    /**
     * Calculates all stats for this [Event]
     * Highly recommended that this gets ran inside it's own thread
     * it will take a long time to process
     * @param year [Year] used to pull data
     * @param scoutCardInfoKeys [ArrayList] list of [ScoutCardInfoKey] for stats
     * @param scoutCardInfos [ArrayList] list of [ScoutCardInfo] for stats
     * @param database [Database] used to pull data
     * @return [HashMap] of stats
     */
    fun getStats(year: Year?, scoutCardInfoKeys: ArrayList<ScoutCardInfoKey>?, scoutCardInfos: ArrayList<ScoutCardInfo>?, database: Database): HashMap<String, Double>
    {
        val statsHashMap = HashMap<String, Double>()
        val cardCount = HashMap<String, Int>()

        val scoutCardInfoKeys = scoutCardInfoKeys ?: ScoutCardInfoKey.getObjects(year, null, database)
        val scoutCardInfos = scoutCardInfos ?: ScoutCardInfo.getObjects(this, null, null, null, null, false, database)

        if(!scoutCardInfoKeys.isNullOrEmpty() && !scoutCardInfos.isNullOrEmpty())
        {
            for(scoutCardInfoKey in scoutCardInfoKeys)
            {
                if(scoutCardInfoKey.includeInStats == true)
                {
                    if(!scoutCardInfos.isNullOrEmpty())
                    {
                        for(scoutCardInfo in scoutCardInfos)
                        {
                            if(scoutCardInfo.propertyKeyId == scoutCardInfoKey.serverId)
                            {
                                val stat = Integer.parseInt(scoutCardInfo.propertyValue)

                                statsHashMap[scoutCardInfoKey.toString()] = (statsHashMap[scoutCardInfoKey.toString()] ?: 0.0) + stat //add to the stat record

                                val runningCardCountTotal = (cardCount[scoutCardInfoKey.toString()] ?: 0)
                                cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros == true && stat == 0) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
                            }
                        }

                        if(statsHashMap[scoutCardInfoKey.toString()] == null)
                        {
                            statsHashMap[scoutCardInfoKey.toString()] = 0.0 //add to the stat record

                            val runningCardCountTotal = (cardCount[scoutCardInfoKey.toString()] ?: 0)
                            cardCount[scoutCardInfoKey.toString()] = if(scoutCardInfoKey.nullZeros == true) runningCardCountTotal else runningCardCountTotal + 1 //keep a running total of the card count
                        }
                    }

                    //set default stats
                    else
                    {
                        statsHashMap[scoutCardInfoKey.toString()] = 0.0
                        cardCount[scoutCardInfoKey.toString()] = 0
                    }
                }
            }

            //calculate averages by iterating through each stat and card count
            for(stat in statsHashMap)
            {
                val cardCount = cardCount[stat.key]!!

                statsHashMap[stat.key] = if(cardCount != 0) round((statsHashMap[stat.key]!! / cardCount) * 100.00) / 100.00 else 0.0
            }
        }

        return statsHashMap
    }

    /**
     * @see Table.load
     */
    override fun load(database: Database): Boolean
    {
        with(getObjects(null, this, null, database))
        {
            with(if (size > 0) this[0] else null)
            {
                if (this != null)
                {
                    loadParentValues(this)
                    this@Event.yearId = yearId
                    this@Event.blueAllianceId = blueAllianceId
                    this@Event.name = name
                    this@Event.city = city
                    this@Event.stateProvince = stateProvince
                    this@Event.country = country
                    this@Event.startDate = startDate
                    this@Event.endDate = endDate
                    return true
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
            put(COLUMN_NAME_BLUE_ALLIANCE_ID, blueAllianceId)
            put(COLUMN_NAME_NAME, name)
            put(COLUMN_NAME_CITY, city)
            put(COLUMN_NAME_STATEPROVINCE, stateProvince)
            put(COLUMN_NAME_COUNTRY, country)
            put(COLUMN_NAME_START_DATE, startDate)
            put(COLUMN_NAME_END_DATE, endDate)
        }

    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return name
    }
}

package com.alphadevelopmentsolutions.frcscout.classes.table

import androidx.room.Entity
import java.util.*
import kotlin.math.round

@Entity(tableName = "events")
class Event(
        var yearId: Int = DEFAULT_INT,
        var blueAllianceId: String = DEFAULT_STRING,
        var name: String = DEFAULT_STRING,
        var city: String = DEFAULT_STRING,
        var stateProvince: String = DEFAULT_STRING,
        var country: String = DEFAULT_STRING,
        var startDate: Date = DEFAULT_DATE,
        var endDate: Date = DEFAULT_DATE) : Table()
{
    /**
     * @see Table.toString
     */
    override fun toString(): String
    {
        return name
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
}

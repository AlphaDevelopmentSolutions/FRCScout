package com.alphadevelopmentsolutions.frcscout.classes.table.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfo
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ScoutCardInfoKey
import com.alphadevelopmentsolutions.frcscout.classes.table.Table
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.io.File

@Entity(tableName = TableName.TEAM)
class Team(
        var number: Int,
        var name: String,
        var city: String? = null,
        @SerializedName("state_province") var stateProvince: String? = null,
        var country: String? = null,
        @SerializedName("rookie_year") var rookieYear: Int? = null,
        @SerializedName("facebook_url") var facebookURL: String? = null,
        @SerializedName("twitter_url") var twitterURL: String? = null,
        @SerializedName("instagram_url") var instagramURL: String? = null,
        @SerializedName("youtube_url") var youtubeURL: String? = null,
        @SerializedName("website_url") var websiteURL: String? = null,
        @SerializedName("avatar_uri") var avatarUri: String? = null
) : Table()
{
    /**
     * Returns the bitmap from the specified file location
     * @return null if no image found, bitmap if image found
     */
    //check if the image exists
    val imageBitmap: Bitmap?
        get()
        {
            val file = File(avatarUri)
            return if (file.exists()) BitmapFactory.decodeFile(file.absolutePath) else null

        }

    /**
     * Calculates all stats for this [Team]
     * Highly recommended that this gets ran inside it's own thread
     * it will take a long time to process
     * @param year [Year] used to pull data
     * @param event [Event] used to pull data
     * @param matches [ArrayList] list of [Match] for stats
     * @param scoutCardInfoKeys [ArrayList] list of [ScoutCardInfoKey] for stats
     * @param scoutCardInfos [ArrayList] list of [ScoutCardInfo] for stats
     * @param database [Database] used to pull data
     * @return [HashMap] of stats
     */
    fun getStats(year: Year?, event: Event?, matches: ArrayList<Match>?, scoutCardInfoKeys: ArrayList<ScoutCardInfoKey>?, scoutCardInfos: ArrayList<ScoutCardInfo>?, database: Database): HashMap<String, HashMap<String, Int>>
    {
        val teamStats = HashMap<String, HashMap<String, Int>>()

        val scoutCardInfoKeys = scoutCardInfoKeys ?: ScoutCardInfoKey.getObjects(year, null, database)
        val scoutCardInfos = scoutCardInfos ?: ScoutCardInfo.getObjects(event, null, this, null, null, false, database)
        val matches = matches ?: Match.getObjects(event, null, this, database)

        if (!matches.isNullOrEmpty() && !scoutCardInfoKeys.isNullOrEmpty() && !scoutCardInfos.isNullOrEmpty())
        {
            for(match in matches)
            {
                val statsHashMap = HashMap<String, Int>()

                //filter the cards that matchId this matchId
                val filteredScoutCardInfos = ArrayList<ScoutCardInfo>().apply {

                    for(scoutCard in scoutCardInfos)
                    {
                        if(scoutCard.matchId == match.key && scoutCard.teamId == id)
                            add(scoutCard)
                    }
                }

                //iterate through each scout card info key
                for(scoutCardInfoKey in scoutCardInfoKeys)
                {
                    if(scoutCardInfoKey.includeInStats == true)
                    {
                        if(!filteredScoutCardInfos.isNullOrEmpty())
                        {
                            for(scoutCardInfo in filteredScoutCardInfos)
                            {
                                if(scoutCardInfo.keyId == scoutCardInfoKey.serverId)
                                    statsHashMap[scoutCardInfoKey.toString()] = Integer.parseInt(scoutCardInfo.value) //get the most recent stat and set it
                            }

                            //if stat was never set, default to 0
                            if(statsHashMap[scoutCardInfoKey.toString()] == null)
                                statsHashMap[scoutCardInfoKey.toString()] = 0

                        }
                        else
                            statsHashMap[scoutCardInfoKey.toString()] = 0
                    }
                }

                //add the stats to the running list of stats per matchId
                teamStats[match.toString()] = statsHashMap
            }
        }

        return teamStats
    }

    /**
     * @see Table.toString
     */
    override fun toString() = "$number - $name"
}

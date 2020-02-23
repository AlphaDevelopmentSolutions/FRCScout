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
     * @see Table.toString
     */
    override fun toString() = "$number - $name"
}

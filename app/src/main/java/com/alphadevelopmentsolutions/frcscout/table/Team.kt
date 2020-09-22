package com.alphadevelopmentsolutions.frcscout.table

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.classes.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import java.io.File
import java.util.*

@Entity(
    tableName = TableName.TEAM,
    inheritSuperIndices = true
)
class Team(
    var number: Int,
    var name: String,
    var city: String? = null,
    var stateProvince: String? = null,
    var country: String? = null,
    var rookieYear: Int? = null,
    var facebookUrl: String? = null,
    var twitterUrl: String? = null,
    var instagramUrl: String? = null,
    var youtubeUrl: String? = null,
    var websiteUrl: String? = null,
    var avatarUri: String? = null,
    var lastModifiedDate: Date = Date(),
    var modifiedById: ByteArray
) : Table() {
    override fun toString(): String =
        name
}

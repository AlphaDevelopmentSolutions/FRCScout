package com.alphadevelopmentsolutions.frcscout.data.models

import android.graphics.Color
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = TableName.TEAM_ACCOUNT,
    inheritSuperIndices = true,
)
class TeamAccount(
    @SerializedName("team_id") @ColumnInfo(name = "team_id", index = true) var teamId: ByteArray,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("description") @ColumnInfo(name = "description") var description: String? = null,
    @SerializedName("username") @ColumnInfo(name = "username") var username: String,
    @SerializedName("owner_id") @ColumnInfo(name = "owner_id", index = true) var ownerId: ByteArray,
    @SerializedName("avatar_uri") @ColumnInfo(name = "avatar_uri") var avatarUri: String? = null,
    @SerializedName("primary_color") @ColumnInfo(name = "primary_color") var primaryColor: String? = null,
    @SerializedName("accent_color") @ColumnInfo(name = "accent_color") var accentColor: String? = null,
    @SerializedName("created_date") @ColumnInfo(name = "created_date") var createdDate: Date
) : SubmittableTable(null, null, ByteArray(0)) {

    companion object : StaticTable<TeamAccount> {
        override fun create(): TeamAccount =
            TeamAccount(
                ByteArray(0),
                "",
                null,
                "",
                ByteArray(0),
                null,
                null,
                null,
                Date()
            )
    }

    override fun toString(): String =
        name
}

package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.USER,
    inheritSuperIndices = true
)
class User(
    @SerializedName("first_name") @ColumnInfo(name = "first_name") var firstName: String,
    @SerializedName("last_name") @ColumnInfo(name = "last_name") var lastName: String,
    @SerializedName("email") @ColumnInfo(name = "email") var email: String,
    @SerializedName("username") @ColumnInfo(name = "username") var username: String,
    @SerializedName("description") @ColumnInfo(name = "description") var description: String? = null,
    @SerializedName("avatar_uri") @ColumnInfo(name = "avatar_uri") var avatarUri: String? = null,
    @SerializedName("auth_token") @ColumnInfo(name = "auth_token") var authToken: String
) : SubmittableTable(null, null, ByteArray(0)) {

    companion object : StaticTable<User> {
        override fun create(): User =
            User(
                "",
                "",
                "",
                "",
                null,
                null,
                ""
            )
    }

    override fun toString(): String =
        "$firstName $lastName"
}

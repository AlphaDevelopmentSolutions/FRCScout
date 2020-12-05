package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.USER,
    inheritSuperIndices = true
)
open class User(
    @SerializedName("first_name") @ColumnInfo(name = "first_name") open var firstName: String,
    @SerializedName("last_name") @ColumnInfo(name = "last_name") open var lastName: String,
    @SerializedName("email") @ColumnInfo(name = "email") open var email: String,
    @SerializedName("username") @ColumnInfo(name = "username") open var username: String,
    @SerializedName("description") @ColumnInfo(name = "description") open var description: String? = null,
    @SerializedName("avatar_uri") @ColumnInfo(name = "avatar_uri") open var avatarUri: String? = null
) : Table() {

    companion object : StaticTable<User> {
        override fun create(): User =
            User(
                "",
                "",
                "",
                "",
                null,
                null
            )
    }

    override fun toString(): String =
        "$firstName $lastName"
}

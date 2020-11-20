package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.alphadevelopmentsolutions.frcscout.data.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = TableName.USER_ROLE,
    inheritSuperIndices = true
)
class UserRole(
    @SerializedName("user_team_account_list_id") @ColumnInfo(name = "user_team_account_list_id", index = true) var userTeamAccountListId: ByteArray,
    @SerializedName("role_id") @ColumnInfo(name = "role_id", index = true) var roleId: ByteArray
) : Table() {

    companion object : StaticTable<UserRole> {
        override fun create(): UserRole =
            UserRole(
                ByteArray(0),
                ByteArray(0)
            )
    }

    override fun toString(): String =
        ""
}
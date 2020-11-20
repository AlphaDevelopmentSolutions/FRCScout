package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.alphadevelopmentsolutions.frcscout.data.RDatabase
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(
    tableName = TableName.USER_TEAM_ACCOUNT,
    inheritSuperIndices = true
)
class UserTeamAccount(
    @SerializedName("user_id") @ColumnInfo(name = "user_id", index = true) var userId: ByteArray,
    @SerializedName("team_account_id") @ColumnInfo(name = "team_account_id", index = true) var teamAccountId: ByteArray,
    @SerializedName("role_id") @ColumnInfo(name = "role_id", index = true) var roleId: ByteArray,
    @SerializedName("state") @ColumnInfo(name = "state") var state: ItemState? = null
) : SubmittableTable(null, null, ByteArray(0)) {

    companion object : StaticTable<UserTeamAccount> {
        override fun create(): UserTeamAccount =
            UserTeamAccount(
                ByteArray(0),
                ByteArray(0),
                ByteArray(0)
            )
    }

    override fun toString(): String =
        ""

    enum class ItemState {
        ENABLED,
        DISABLED
    }
}

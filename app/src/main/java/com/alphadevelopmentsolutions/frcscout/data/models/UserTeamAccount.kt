package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.USER_TEAM_ACCOUNT,
    inheritSuperIndices = true
)
class UserTeamAccount(
    @SerializedName("user_id") @ColumnInfo(name = "user_id", index = true) var userId: ByteArray,
    @SerializedName("team_account_id") @ColumnInfo(name = "team_account_id", index = true) var teamAccountId: ByteArray,
    @SerializedName("role_id") @ColumnInfo(name = "role_id", index = true) var roleId: ByteArray,
    @SerializedName("state") @ColumnInfo(name = "state") var state: ItemState? = null
) : Table() {

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
        DISABLED;

        companion object {
            fun fromString(itemState: String?): ItemState? =
                when {
                    itemState.equals(ENABLED.name, ignoreCase = true) -> ENABLED
                    itemState.equals(DISABLED.name, ignoreCase = true) -> DISABLED
                    else -> null
                }
        }
    }
}

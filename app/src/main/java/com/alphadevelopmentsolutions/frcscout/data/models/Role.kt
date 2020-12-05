package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.ROLE,
    inheritSuperIndices = true
)
class Role(
    @SerializedName("team_account_id") @ColumnInfo(name = "team_account_id", index = true) var teamAccountId: ByteArray,
    @SerializedName("name") @ColumnInfo(name = "name") var name: String,
    @SerializedName("description") @ColumnInfo(name = "description") var description: String? = null,
    @SerializedName("can_manage_team") @ColumnInfo(name = "can_manage_team", defaultValue = "0") var canManageTeam: Boolean = false,
    @SerializedName("can_manage_users") @ColumnInfo(name = "can_manage_users", defaultValue = "0") var canManageUsers: Boolean = false,
    @SerializedName("can_match_scout") @ColumnInfo(name = "can_match_scout", defaultValue = "0") var canMatchScout: Boolean = false,
    @SerializedName("can_pit_scout") @ColumnInfo(name = "can_pit_scout", defaultValue = "0") var canPitScout: Boolean = false,
    @SerializedName("can_capture_media") @ColumnInfo(name = "can_capture_media", defaultValue = "0") var canCaptureMedia: Boolean = false,
    @SerializedName("can_manage_reports") @ColumnInfo(name = "can_manage_reports", defaultValue = "0") var canManageReports: Boolean = false
) : Table() {

    companion object : StaticTable<Role> {
        override fun create(): Role =
            Role(
                ByteArray(0),
                "",
                null,
                canManageTeam = false,
                canManageUsers = false,
                canMatchScout = false,
                canPitScout = false,
                canCaptureMedia = false,
                canManageReports = false
            )
    }

    override fun toString(): String =
        name
}
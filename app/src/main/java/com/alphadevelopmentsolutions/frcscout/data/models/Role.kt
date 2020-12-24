package com.alphadevelopmentsolutions.frcscout.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName

import com.google.gson.annotations.SerializedName

@Entity(
    tableName = TableName.ROLE,
    inheritSuperIndices = true
)

/**
 * When this class is used as a matrix in the [Account] instance, it takes all [Role]
 * objects assigned to an [Account] for a [TeamAccount] and merges the permissions together
 *
 * When merging permissions, decision is treated as an OR gate
 * e.g. permission = (permissionValue1 == true) || (permissionValue2 == true) ...
 */
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

        /**
         * Merges permissions into a role matrix
         * @return [Role] merged permissions
         */
        fun generateMatrix(roleList: List<Role>, teamAccountId: ByteArray): Role {
            val roleMatrix = create()

            roleMatrix.teamAccountId = teamAccountId

            roleList.forEach { role ->
                if (!roleMatrix.canManageTeam)
                    roleMatrix.canManageTeam = role.canManageTeam

                if (!roleMatrix.canManageUsers)
                    roleMatrix.canManageUsers = role.canManageUsers

                if (!roleMatrix.canMatchScout)
                    roleMatrix.canMatchScout = role.canMatchScout

                if (!roleMatrix.canPitScout)
                    roleMatrix.canPitScout = role.canPitScout

                if (!roleMatrix.canCaptureMedia)
                    roleMatrix.canCaptureMedia = role.canCaptureMedia

                if (!roleMatrix.canManageReports)
                    roleMatrix.canManageReports = role.canManageReports
            }

            return roleMatrix
        }
    }

    override fun toString(): String =
        name
}
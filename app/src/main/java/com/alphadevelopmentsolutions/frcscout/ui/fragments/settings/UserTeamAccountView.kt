package com.alphadevelopmentsolutions.frcscout.ui.fragments.settings

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.data.models.*

class UserTeamAccountView(
    @Embedded val userTeamAccount: UserTeamAccount,
    @Relation(parentColumn = "team_account_id", entityColumn = "id", entity = TeamAccount::class) val teamAccount: TeamAccount
) {
    override fun toString() =
        teamAccount.toString()
}
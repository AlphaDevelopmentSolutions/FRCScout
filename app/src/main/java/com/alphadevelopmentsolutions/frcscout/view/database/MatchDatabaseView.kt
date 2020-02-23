package com.alphadevelopmentsolutions.frcscout.view.database

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItem
import com.alphadevelopmentsolutions.frcscout.classes.table.account.ChecklistItemResult
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match
import com.alphadevelopmentsolutions.frcscout.classes.table.core.Team


class MatchDatabaseView(
        @Embedded val match: Match,
        @Relation(parentColumn = "blueAllianceTeamOneId", entityColumn = "id", entity = Team::class) val blueAllianceTeamOne: Team,
        @Relation(parentColumn = "blueAllianceTeamTwoId", entityColumn = "id", entity = Team::class) val blueAllianceTeamTwo: Team,
        @Relation(parentColumn = "blueAllianceTeamThreeId", entityColumn = "id", entity = Team::class) val blueAllianceTeamThree: Team,
        @Relation(parentColumn = "redAllianceTeamOneId", entityColumn = "id", entity = Team::class) val redAllianceTeamOne: Team,
        @Relation(parentColumn = "redAllianceTeamTwoId", entityColumn = "id", entity = Team::class) val redAllianceTeamTwo: Team,
        @Relation(parentColumn = "redAllianceTeamThreeId", entityColumn = "id", entity = Team::class) val redAllianceTeamThree: Team
)
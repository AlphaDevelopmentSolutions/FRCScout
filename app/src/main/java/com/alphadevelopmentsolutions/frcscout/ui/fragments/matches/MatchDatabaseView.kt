package com.alphadevelopmentsolutions.frcscout.ui.fragments.matches

import androidx.room.Embedded
import androidx.room.Relation
import com.alphadevelopmentsolutions.frcscout.data.models.Match
import com.alphadevelopmentsolutions.frcscout.data.models.MatchType
import com.alphadevelopmentsolutions.frcscout.data.models.Team

class MatchDatabaseView(
    @Embedded val match: Match,
    @Relation(parentColumn = "type_id", entityColumn = "id", entity = MatchType::class) val matchType: MatchType,
    @Relation(parentColumn = "blue_alliance_team_one_id", entityColumn = "id", entity = Team::class) val blueAllianceTeamOne: Team,
    @Relation(parentColumn = "blue_alliance_team_two_id", entityColumn = "id", entity = Team::class) val blueAllianceTeamTwo: Team,
    @Relation(parentColumn = "blue_alliance_team_three_id", entityColumn = "id", entity = Team::class) val blueAllianceTeamThree: Team,
    @Relation(parentColumn = "red_alliance_team_one_id", entityColumn = "id", entity = Team::class) val redAllianceTeamOne: Team,
    @Relation(parentColumn = "red_alliance_team_two_id", entityColumn = "id", entity = Team::class) val redAllianceTeamTwo: Team,
    @Relation(parentColumn = "red_alliance_team_three_id", entityColumn = "id", entity = Team::class) val redAllianceTeamThree: Team
) {
    override fun toString() =
        "$matchType ${match.matchNumber}"
}
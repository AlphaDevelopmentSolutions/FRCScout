package com.alphadevelopmentsolutions.frcscout.ui.fragments.match

import android.app.Application
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.alphadevelopmentsolutions.frcscout.data.models.Team
import com.alphadevelopmentsolutions.frcscout.enums.AllianceColor
import com.alphadevelopmentsolutions.frcscout.ui.fragments.matchlist.MatchDatabaseView

class MatchDetailViewModel(
    application: Application,
    private val navController: NavController,
    private val matchDatabaseView: MatchDatabaseView,
    allianceColor: AllianceColor
) : AndroidViewModel(application) {

    val team1: Team
    val team2: Team
    val team3: Team

    init {
        when (allianceColor) {
            AllianceColor.BLUE -> {
                team1 = matchDatabaseView.blueAllianceTeamOne
                team2 = matchDatabaseView.blueAllianceTeamTwo
                team3 = matchDatabaseView.blueAllianceTeamThree
            }

            AllianceColor.RED -> {
                team1 = matchDatabaseView.redAllianceTeamOne
                team2 = matchDatabaseView.redAllianceTeamTwo
                team3 = matchDatabaseView.redAllianceTeamThree
            }
        }
    }

    /**
     * Navigate to the team fragment
     */
    fun onTeamButtonClicked(team: Team) {
        navController.navigate(MatchFragmentDirections.actionMatchFragmentDestinationToScoutCardFragmentDestination(matchDatabaseView.match, team))
    }
}
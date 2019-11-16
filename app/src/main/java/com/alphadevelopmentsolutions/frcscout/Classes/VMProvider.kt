package com.alphadevelopmentsolutions.frcscout.Classes

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.alphadevelopmentsolutions.frcscout.ViewModels.*

class VMProvider(owner: ViewModelStoreOwner) : ViewModelProvider(owner) {

    val checklistItemResultViewModel = get(ChecklistItemResultViewModel::class.java)
    val checklistItemViewModel = get(ChecklistItemResultViewModel::class.java)
    val eventTeamListViewModel = get(EventTeamListViewModel::class.java)
    val eventViewModel = get(EventViewModel::class.java)
    val matchViewModel = get(MatchViewModel::class.java)
    val robotInfoKeyViewModel = get(RobotInfoKeyViewModel::class.java)
    val robotInfoViewModel = get(RobotInfoViewModel::class.java)
    val robotMediaViewModel = get(RobotMediaViewModel::class.java)
    val scoutCardInfoKeyViewModel = get(ScoutCardInfoKeyViewModel::class.java)
    val scoutCardInfoViewModel = get(ScoutCardInfoViewModel::class.java)
    val teamViewModel = get(TeamViewModel::class.java)
    val userViewModel = get(UserViewModel::class.java)
    val yearViewModel = get(YearViewModel::class.java)
}
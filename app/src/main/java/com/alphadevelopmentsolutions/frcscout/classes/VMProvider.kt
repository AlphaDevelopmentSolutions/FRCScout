package com.alphadevelopmentsolutions.frcscout.classes

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.alphadevelopmentsolutions.frcscout.view.model.*

class VMProvider(owner: ViewModelStoreOwner) : ViewModelProvider(owner) {

    val checklistItemResultViewModel by lazy {
        get(ChecklistItemResultViewModel::class.java)
    }
    val checklistItemViewModel by lazy {
        get(ChecklistItemResultViewModel::class.java)
    }

    val eventTeamListViewModel by lazy {
        get(EventTeamListViewModel::class.java)
    }

    val eventViewModel by lazy {
        get(EventViewModel::class.java)
    }

    val matchViewModel by lazy {
        get(MatchViewModel::class.java)
    }

    val robotInfoKeyViewModel by lazy {
        get(RobotInfoKeyViewModel::class.java)
    }

    val robotInfoViewModel by lazy {
        get(RobotInfoViewModel::class.java)
    }

    val robotMediaViewModel by lazy {
        get(RobotMediaViewModel::class.java)
    }

    val scoutCardInfoKeyViewModel by lazy {
        get(ScoutCardInfoKeyViewModel::class.java)
    }

    val scoutCardInfoViewModel by lazy {
        get(ScoutCardInfoViewModel::class.java)
    }

    val teamViewModel by lazy {
        get(TeamViewModel::class.java)
    }

    val userViewModel by lazy {
        get(UserViewModel::class.java)
    }

    val yearViewModel by lazy {
        get(YearViewModel::class.java)
    }
}
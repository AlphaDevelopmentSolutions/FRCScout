package com.alphadevelopmentsolutions.frcscout.api

import com.alphadevelopmentsolutions.frcscout.classes.table.account.*
import com.alphadevelopmentsolutions.frcscout.classes.table.core.*

class AppData (
        val checklistItems: List<ChecklistItem> = listOf(),
        val checklistItemResults: List<ChecklistItemResult> = listOf(),
        val events: List<Event> = listOf(),
        val eventTeamList: List<EventTeamList> = listOf(),
        val matches: List<Match> = listOf(),
        val robotInfo: List<RobotInfo> = listOf(),
        val robotInfoKeys: List<RobotInfoKey> = listOf(),
        val robotMedia: List<RobotMedia> = listOf(),
        val scoutCardInfo: List<ScoutCardInfo> = listOf(),
        val scoutCardInfoKeys: List<ScoutCardInfoKey> = listOf(),
        val teams: List<Team> = listOf(),
        val users: List<User> = listOf(),
        val years: List<Year> = listOf()
)
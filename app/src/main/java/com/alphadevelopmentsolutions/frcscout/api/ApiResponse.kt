package com.alphadevelopmentsolutions.frcscout.api

import com.alphadevelopmentsolutions.frcscout.classes.table.*

class ApiResponse
{
    class GetData(
            val checklistItems: List<ChecklistItem>,
            val checklistItemResults: List<ChecklistItemResult>,
            val events: List<Event>,
            val eventTeamList: List<EventTeamList>,
            val matches: List<Match>,
            val robotInfo: List<RobotInfo>,
            val robotInfoKeys: List<RobotInfoKey>,
            val robotMedia: List<RobotMedia>,
            val scoutCardInfo: List<ScoutCardInfo>,
            val scoutCardInfoKeys: List<ScoutCardInfoKey>,
            val teams: List<Team>,
            val users: List<User>,
            val years: List<Year>
    )

    class Connect()
}
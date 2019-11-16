package com.alphadevelopmentsolutions.frcscout.Api

import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
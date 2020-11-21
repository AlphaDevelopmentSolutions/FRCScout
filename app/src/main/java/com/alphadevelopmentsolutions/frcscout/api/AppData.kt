package com.alphadevelopmentsolutions.frcscout.api

import com.alphadevelopmentsolutions.frcscout.data.models.*
import com.google.gson.annotations.SerializedName

class AppData(
    @SerializedName("checklist_items") var checklistItemList: List<ChecklistItem> = listOf(),
    @SerializedName("checklist_item_results") var checklistItemResultList: List<ChecklistItemResult> = listOf(),
    @SerializedName("data_types") var dataTypeList: List<DataType> = listOf(),
    @SerializedName("events") var eventList: List<Event> = listOf(),
    @SerializedName("event_team_lists") var eventTeamListList: List<EventTeamList> = listOf(),
    @SerializedName("matches") var matchList: List<Match> = listOf(),
    @SerializedName("match_types") var matchTypeList: List<MatchType> = listOf(),
    @SerializedName("robot_info") var robotInfoList: List<RobotInfo> = listOf(),
    @SerializedName("robot_info_keys") var robotInfoKeyList: List<RobotInfoKey> = listOf(),
    @SerializedName("robot_media") var robotMediaList: List<RobotMedia> = listOf(),
    @SerializedName("role") var roleList: List<Role> = listOf(),
    @SerializedName("scout_card_info") var scoutCardInfoList: List<ScoutCardInfo> = listOf(),
    @SerializedName("scout_card_info_keys") var scoutCardInfoKeyList: List<ScoutCardInfoKey> = listOf(),
    @SerializedName("scout_card_info_key_states") var scoutCardInfoKeyStateList: List<ScoutCardInfoKeyState> = listOf(),
    @SerializedName("teams") var teamList: List<Team> = listOf(),
    @SerializedName("team_accounts") var teamAccountList: List<TeamAccount> = listOf(),
    @SerializedName("users") var userList: List<User> = listOf(),
    @SerializedName("user_roles") var userRoleList: List<UserRole> = listOf(),
    @SerializedName("user_team_accounts") var userTeamAccountList: List<UserTeamAccount> = listOf(),
    @SerializedName("years") var yearList: List<Year> = listOf()
) {
    companion object {
        fun create() =
            AppData()
    }
}
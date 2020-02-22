package com.alphadevelopmentsolutions.frcscout.interfaces

import android.util.Log
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.crashlytics.android.Crashlytics

interface TableName
{
    companion object
    {
        const val EVENT = "events"
        const val EVENT_TEAM_LIST = "event_team_list"
        const val MATCH = "matches"
        const val TEAM = "teams"
        const val YEAR = "years"
        const val CHECKLIST_ITEM = "checklist_items"
        const val CHECKLIST_ITEM_RESULT = "checklist_item_results"
        const val ROBOT_INFO = "robot_info"
        const val ROBOT_INFO_KEY = "robot_info_keys"
        const val ROBOT_MEDIA = "robot_media"
        const val SCOUT_CARD_INFO = "scout_card_info"
        const val SCOUT_CARD_INFO_KEY = "scout_card_info_keys"
        const val USER = "users"
    }
}
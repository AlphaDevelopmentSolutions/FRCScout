package com.alphadevelopmentsolutions.frcscout.interfaces

/**
 * Stores all table names
 */
interface TableName {
    companion object {
        const val DELETED_RECORDS = "deleted_records"
        const val LOG_DRAFT = "log_draft"
        const val LOG_DELETE = "log_delete"

        const val CHECKLIST_ITEM = "checklist_items"
        const val CHECKLIST_ITEM_RESULT = "checklist_items_results"
        const val EVENT = "events"
        const val EVENT_TEAM_LIST = "event_team_list"
        const val MATCH = "matches"
        const val ROBOT_INFO = "robot_info"
        const val ROBOT_INFO_KEY = "robot_info_keys"
        const val ROBOT_MEDIA = "robot_media"
        const val SCOUT_CARD_INFO = "scout_card_info"
        const val SCOUT_CARD_INFO_KEY = "scout_card_info_keys"
        const val TEAM = "teams"
        const val USER = "users"
        const val YEAR = "years"
    }
}
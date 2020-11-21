package com.alphadevelopmentsolutions.frcscout.enums

enum class ChecklistStatus {
    COMPLETE,
    INCOMPLETE;

    companion object {
        fun fromString(status: String?) =
            when {
                status.equals(COMPLETE.name, ignoreCase = true) -> COMPLETE
                status.equals(INCOMPLETE.name, ignoreCase = true) -> INCOMPLETE
                else -> null
            }
    }
}
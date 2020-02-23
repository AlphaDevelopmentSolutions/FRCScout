package com.alphadevelopmentsolutions.frcscout.enums

enum class Status
{
    COMPLETE,
    INCOMPLETE,
    UNSET;

    companion object {
        fun fromString(status: String): Status {
            return when(status.toUpperCase()) {
                COMPLETE.toString()     -> COMPLETE
                INCOMPLETE.toString()   -> INCOMPLETE
                else                    -> UNSET
            }
        }
    }
}

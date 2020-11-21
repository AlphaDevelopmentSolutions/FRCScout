package com.alphadevelopmentsolutions.frcscout.extensions

import com.alphadevelopmentsolutions.frcscout.classes.DateFormatter
import java.util.*

/**
 * Formats the date into a JSON appropriate type
 */
fun Date.toJson(): String =
    DateFormatter.getInstance().let {
        it.timeZone = TimeZone.getTimeZone("UTC")
        it.applyPattern("yyyy-MM-dd HH:mm:ss")
        val json = it.format(this)
        it.timeZone = TimeZone.getDefault()
        json
    }

/**
 * Formats date in a readable format
 */
fun Date.toHumanString(): String =
    DateFormatter.getInstance().let {
        it.applyPattern("d")
        it.applyPattern("MMM d'${this.getSuffix(it.format(this).toInt())}', yyyy")
        it.format(this)
    }

/**
 * Formats date into a timestamp
 */
fun Date.toTimeStamp(): String =
    DateFormatter.getInstance().let {
        it.applyPattern("yyyy-MM-dd HH:mm:ss")
        it.format(this)
    }

/**
 * Formats date in 12 hour format
 */
fun Date.to12Hour(): String =
    DateFormatter.getInstance().let {
        it.applyPattern("h:mm")
        it.format(this)
    }

/**
 * Formats date in 12 hour format with period
 */
fun Date.to12HourWithPeriod(): String = "${to12Hour()} ${get12HourPeriod()}"

/**
 * Formats date in 24 hour format
 */
fun Date.to24Hour(): String =
    DateFormatter.getInstance().let {
        it.applyPattern("HH:mm")
        it.format(this)
    }

/**
 * Formats date in AM / PM format
 */
fun Date.get12HourPeriod(): String {
    return DateFormatter.getInstance().let {
        it.applyPattern("a")
        it.format(this)
    }
}

/**
 * Gets the suffix for a given number
 * @param number [Int] to get suffix for
 * @return [String] suffix for number
 */
fun Date.getSuffix(number: Int): String {
    if (number in 11..13)
        return "th"

    return when (number % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
    }
}
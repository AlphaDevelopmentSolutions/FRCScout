package com.alphadevelopmentsolutions.frcscout.classes

import java.text.SimpleDateFormat
import java.util.*

class FormattableDate: Date {

    private val dateFormatter by lazy {
        SimpleDateFormat()
    }

    constructor(time: Long) : super(time)

    constructor() : super()

    /**
     * Formats the date into a JSON appropriate type
     */
    fun toJson(): String {
        dateFormatter.applyPattern("yyyy-MM-dd HH:mm:ss")

        return dateFormatter.format(this)
    }

    /**
     * Formats date in a readable format
     */
    override fun toString(): String {
        dateFormatter.apply {
            applyPattern("d")
            val numberSuffix = getNumberSuffix(format(this@FormattableDate).toInt())
            applyPattern("MMM d'$numberSuffix', yyyy")
        }

        return dateFormatter.format(this)
    }

    /**
     * Formats date into a timestamp
     */
    fun toTimeStamp(): String {
        dateFormatter.applyPattern("yyyy-MM-dd HH:mm:ss")
        return dateFormatter.format(this)
    }

    /**
     * Gets the suffix for a given number
     * @param number [Int] to get suffix for
     * @return [String] suffix for number
     */
    private fun getNumberSuffix(number: Int): String {
        return when (number) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }
}
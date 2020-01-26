package com.alphadevelopmentsolutions.frcscout.classes.Tables

import java.util.*

abstract class Table
{

    abstract override fun toString(): String

    companion object
    {
        val DEFAULT_STRING: String
            get()
            {
                return ""
            }

        val DEFAULT_INT: Int
            get()
            {
                return -1
            }

        val DEFAULT_LONG: Long
            get()
            {
                return -1
            }

        val DEFAULT_DOUBLE: Double
            get()
            {
                return -1.0
            }

        val DEFAULT_DATE: Date
            get()
            {
                return Date()
            }

        val DEFAULT_BOOLEAN: Boolean
            get()
            {
                return false
            }
    }
}

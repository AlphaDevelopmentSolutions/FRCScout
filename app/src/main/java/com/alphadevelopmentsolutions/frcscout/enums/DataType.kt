package com.alphadevelopmentsolutions.frcscout.enums

enum class DataType {
    BOOL,
    INT,
    TEXT;

    companion object
    {
        /**
         * Parses the string into a datatype format
         * @param type string of type to parse
         * @return datatype
         */
        fun fromString(type: String): DataType
        {
            return when
            {
                type.toUpperCase() == BOOL.name -> BOOL
                type.toUpperCase() == INT.name -> INT
                type.toUpperCase() == TEXT.name -> TEXT
                else -> BOOL
            }

        }
    }
}
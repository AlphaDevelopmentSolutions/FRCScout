package com.alphadevelopmentsolutions.frcscout.enums

/**
 * Colors for available alliances during the matches
 */
enum class AllianceColor
{
    RED,
    BLUE,
    NONE;


    companion object
    {

        /**
         * Converts a string value to enum
         * @param allianceColor string enum name
         * @return enum converted from string
         */
        fun getColorFromString(allianceColor: String): AllianceColor
        {
            return when
            {
                allianceColor.toLowerCase() == RED.name.toLowerCase() -> RED
                allianceColor.toLowerCase() == BLUE.name.toLowerCase() -> BLUE
                else -> NONE
            }
        }
    }
}

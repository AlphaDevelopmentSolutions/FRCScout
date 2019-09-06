package com.alphadevelopmentsolutions.frcscout.Enums

/**
 * Positions available before a match
 */
enum class StartingPosition
{
    LEFT,
    CENTER,
    RIGHT;


    companion object
    {

        /**
         * Converts a string value to enum
         * @param position string enum name
         * @return enum converted from string
         */
        fun getPositionFromString(position: String): StartingPosition
        {
            return when
            {
                position.toLowerCase() == StartingPosition.LEFT.name.toLowerCase() -> LEFT
                position.toLowerCase() == StartingPosition.CENTER.name.toLowerCase() -> CENTER
                else -> RIGHT
            }
        }
    }
}

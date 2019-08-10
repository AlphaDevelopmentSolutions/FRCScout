package com.alphadevelopmentsolutions.frcscout.Enums

enum class GameScoreStatus
{
    WIN,
    TIE,
    LOSE;


    companion object
    {

        /**
         * Converts a string value to enum
         * @param gameScoreStatus string enum name
         * @return enum converted from string
         */
        fun getStatusFromString(gameScoreStatus: String): GameScoreStatus
        {
            return when
            {
                gameScoreStatus.toLowerCase() == GameScoreStatus.WIN.name.toLowerCase() -> WIN
                gameScoreStatus.toLowerCase() == GameScoreStatus.TIE.name.toLowerCase() -> TIE
                else -> LOSE
            }
        }
    }
}

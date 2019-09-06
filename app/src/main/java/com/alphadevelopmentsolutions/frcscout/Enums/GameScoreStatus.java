package com.alphadevelopmentsolutions.frcscout.Enums;

public enum  GameScoreStatus
{
    WIN,
    TIE,
    LOSE;

    /**
     * Converts a string value to enum
     * @param gameScoreStatus string enum name
     * @return enum converted from string
     */
    public static GameScoreStatus getStatusFromString(String gameScoreStatus)
    {
        if(gameScoreStatus.toLowerCase().equals(GameScoreStatus.WIN.name().toLowerCase()))
            return GameScoreStatus.WIN;
        else if(gameScoreStatus.toLowerCase().equals(GameScoreStatus.TIE.name().toLowerCase()))
            return GameScoreStatus.TIE;
        else
            return GameScoreStatus.LOSE;
    }
}

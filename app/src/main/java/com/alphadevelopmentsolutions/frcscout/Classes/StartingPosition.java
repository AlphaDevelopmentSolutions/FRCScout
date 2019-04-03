package com.alphadevelopmentsolutions.frcscout.Classes;

/**
 * Positions available before a match
 */
public enum StartingPosition
{
    LEFT,
    CENTER,
    RIGHT;

    /**
     * Converts a string value to enum
     * @param position string enum name
     * @return enum converted from string
     */
    public static StartingPosition getPositionFromString(String position)
    {
        if(position.toLowerCase().equals(StartingPosition.LEFT.name().toLowerCase()))
            return StartingPosition.LEFT;
        else if(position.toLowerCase().equals(StartingPosition.CENTER.name().toLowerCase()))
            return StartingPosition.CENTER;
        else
            return StartingPosition.RIGHT;
    }
}

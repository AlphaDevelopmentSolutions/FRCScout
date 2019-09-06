package com.alphadevelopmentsolutions.frcscout.Enums;

/**
 * Colors for available alliances during the matches
 */
public enum  AllianceColor
{
    RED,
    BLUE,
    NONE;

    /**
     * Converts a string value to enum
     * @param allianceColor string enum name
     * @return enum converted from string
     */
    public static AllianceColor getColorFromString(String allianceColor)
    {
        if(allianceColor.toLowerCase().equals(RED.name().toLowerCase()))
            return RED;
        else if (allianceColor.toLowerCase().equals(BLUE.name().toLowerCase()))
            return BLUE;
        else
            return NONE;
    }
}

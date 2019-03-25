package com.alphadevelopmentsolutions.frcscout.Classes;

/**
 * Colors for available alliances during the matches
 */
public enum  AllianceColor
{
    RED,
    BLUE;

    /**
     * Converts a string value to enum
     * @param allianceColor string enum name
     * @return enum converted from string
     */
    public static AllianceColor getColorFromString(String allianceColor)
    {
        if(allianceColor.toLowerCase().equals(AllianceColor.RED.name().toLowerCase()))
            return AllianceColor.RED;
        else
            return AllianceColor.BLUE;
    }
}

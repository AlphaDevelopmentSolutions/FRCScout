package com.alphadevelopmentsolutions.frcscout.Classes;

/**
 * Pieces available during a match
 */
public enum StartingPiece
{
    HATCH,
    CARGO;

    /**
     * Converts a string value to enum
     * @param piece string enum name
     * @return enum converted from string
     */
    public static StartingPiece getPieceFromString(String piece)
    {
        if(piece.toLowerCase().equals(StartingPiece.HATCH.name().toLowerCase()))
            return StartingPiece.HATCH;
        else
            return StartingPiece.CARGO;
    }
}

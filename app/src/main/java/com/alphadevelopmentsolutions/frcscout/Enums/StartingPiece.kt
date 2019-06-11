package com.alphadevelopmentsolutions.frcscout.Enums

/**
 * Pieces available during a match
 */
enum class StartingPiece
{
    HATCH,
    CARGO;


    companion object
    {

        /**
         * Converts a string value to enum
         * @param piece string enum name
         * @return enum converted from string
         */
        fun getPieceFromString(piece: String): StartingPiece
        {
            return if (piece.toLowerCase() == StartingPiece.HATCH.name.toLowerCase())
                HATCH
            else
                CARGO
        }
    }
}

package com.alphadevelopmentsolutions.frcscout.enums

import com.alphadevelopmentsolutions.frcscout.classes.table.core.Match

enum class MatchType {

    qm,
    qf,
    sf,
    f;

    fun toString(match: Match): String
    {
        return when (this)
        {
            qm -> "Quals " + match.matchNumber

            qf -> "Quarters " + match.setNumber + " - " + match.matchNumber

            sf -> "Semis " + match.setNumber + " - " + match.matchNumber

            f -> "Finals " + match.matchNumber
        }
    }

    companion object
    {

        /**
         * Converts a string value to enum
         * @param matchType string enum name
         * @return enum converted from string
         */
        fun fromString(matchType: String): MatchType
        {
            if (matchType.toLowerCase() == qm.name.toLowerCase())
                return qm
            if (matchType.toLowerCase() == qf.name.toLowerCase())
                return qf
            return if (matchType.toLowerCase() == sf.name.toLowerCase())
                sf
            else
                f
        }

        fun getTypes() : ArrayList<MatchType>
        {
            return arrayListOf(qm, qf, sf, f)
        }
    }
}
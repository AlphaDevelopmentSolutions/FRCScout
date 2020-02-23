package com.alphadevelopmentsolutions.frcscout.interfaces

import com.alphadevelopmentsolutions.frcscout.enums.MatchType

interface MatchTypeReference {

    companion object
    {
        val QUALIFICATIONS = MatchType.qm
        val QUARTER_FINALS = MatchType.qf
        val SEMI_FINALS = MatchType.sf
        val FINALS = MatchType.f
    }

}
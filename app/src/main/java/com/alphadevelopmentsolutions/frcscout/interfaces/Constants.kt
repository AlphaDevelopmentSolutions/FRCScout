package com.alphadevelopmentsolutions.frcscout.interfaces

import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.fasterxml.uuid.EthernetAddress
import com.fasterxml.uuid.Generators
import com.fasterxml.uuid.impl.TimeBasedGenerator
import java.io.File

interface Constants
{
    companion object {
        const val AUTH_TOKEN: String = "auth-token"
        const val API_VERSION: String = "api-version"
        const val LAST_UPDATED: String = "last-updated"

        const val API_DNS = BuildConfig.API_DNS
        const val API_PROTOCOL = BuildConfig.API_PROTOCOL
    }
}

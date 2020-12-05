package com.alphadevelopmentsolutions.frcscout.interfaces

import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.fasterxml.uuid.EthernetAddress
import com.fasterxml.uuid.Generators
import com.fasterxml.uuid.impl.TimeBasedGenerator

object Constant {
    const val AUTH_TOKEN: String = "token"
    const val API_VERSION: String = "api-version"
    const val LAST_UPDATED: String = "since"

    const val API_DNS = BuildConfig.API_DNS
    const val API_PROTOCOL = BuildConfig.API_PROTOCOL

    const val IMAGE_FILE_EXTENSION = ".jpg"

    const val IMAGE_EXTERNAL_FILES_DIR = "photos"

    const val FILE_AUTHORITY = "${BuildConfig.APPLICATION_ID}.provider"

    val UUID_GENERATOR: TimeBasedGenerator by lazy {
        Generators.timeBasedGenerator(EthernetAddress.fromInterface())
    }
}

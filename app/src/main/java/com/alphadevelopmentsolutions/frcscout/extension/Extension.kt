package com.alphadevelopmentsolutions.frcscout.extension

import android.os.Bundle
import java.util.*

fun Bundle.getUUIDOrNull(key: String): UUID? {
    return if (containsKey(key))
        try {
            UUID.fromString(getString(key))
        } catch (e: IllegalArgumentException) {
            null
        }
    else
        null
}

fun Bundle.putUUID(key: String, uuid: UUID?) {
    putString(key, uuid?.toString())
}
package com.alphadevelopmentsolutions.frcscout.extensions

import com.alphadevelopmentsolutions.frcscout.interfaces.ByteArrayConverter
import java.util.*

/**
 * Parses a [ByteArray] object to a [UUID] object
 */
fun ByteArray.toUUID(): UUID = ByteArrayConverter.toUUID(this)
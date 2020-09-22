package com.alphadevelopmentsolutions.frcscout.extension

import com.alphadevelopmentsolutions.frcscout.interfaces.ByteArrayConverter
import java.util.*

fun UUID.toByteArray(): ByteArray =
    ByteArrayConverter.fromUUID(this)
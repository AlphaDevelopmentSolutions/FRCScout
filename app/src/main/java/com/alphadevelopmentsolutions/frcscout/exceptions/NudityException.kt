package com.alphadevelopmentsolutions.frcscout.exceptions

import com.alphadevelopmentsolutions.frcscout.extensions.toHumanString
import com.alphadevelopmentsolutions.frcscout.extensions.toTimeStamp
import com.alphadevelopmentsolutions.frcscout.extensions.toUUID
import java.lang.Exception
import java.util.*

class NudityException(
    confidence: Float,
    userId: ByteArray
) : Exception(
    """
        Nudity detected in image. 
        Confidence: $confidence
        User Id: ${userId.toUUID()}
        Timestamp: ${Date().toTimeStamp()}
    """.trimIndent()
)
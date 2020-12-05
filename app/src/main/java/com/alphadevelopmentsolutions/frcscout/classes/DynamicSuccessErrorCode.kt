package com.alphadevelopmentsolutions.frcscout.classes

class DynamicSuccessErrorCode(
    val id: ByteArray? = null,
    val success: Boolean = false,
    val error: Int = 400,
    val message: String = ""
)
package com.alphadevelopmentsolutions.frcscout.exceptions

import org.json.JSONArray

class ApiException(
    private val responseCode: Int,
    private val response: String
) : Exception() {
    override fun toString() =
        "$responseCode: $response"
}

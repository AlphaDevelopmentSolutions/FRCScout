package com.alphadevelopmentsolutions.frcscout.exceptions

import org.json.JSONArray

class ApiException(private val responseCode: Int, private val response: JSONArray) : Exception()
{

    override fun toString(): String
    {
        return "$responseCode: $response"
    }
}

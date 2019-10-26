package com.alphadevelopmentsolutions.frcscout.Exceptions

import org.json.JSONArray

class ApiException(private val responseCode: Int, private val response: JSONArray) : Exception()
{

    override fun toString(): String
    {
        return "$responseCode: $response"
    }
}

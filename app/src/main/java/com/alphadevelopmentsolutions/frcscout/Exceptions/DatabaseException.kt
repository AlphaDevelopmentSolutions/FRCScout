package com.alphadevelopmentsolutions.frcscout.Exceptions

import org.json.JSONArray

class DatabaseException(private val title: String, private val response: String) : Exception()
{

    override fun toString(): String
    {
        return "$title: $response"
    }
}

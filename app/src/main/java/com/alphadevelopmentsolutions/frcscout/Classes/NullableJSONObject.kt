package com.alphadevelopmentsolutions.frcscout.Classes

import org.json.JSONObject

class NullableJSONObject(private val jsonObject: JSONObject)
{
    fun getIntOrNull(fieldName: String) : Int?
    {
        return if(jsonObject.isNull(fieldName)) null else jsonObject.getInt(fieldName)
    }

    fun getInt(fieldName: String): Int
    {
        return jsonObject.getInt(fieldName)
    }

    fun getStringOrNull(fieldName: String) : String?
    {
        return if(jsonObject.isNull(fieldName)) null else jsonObject.getString(fieldName)
    }

    fun getString(fieldName: String): String
    {
        return jsonObject.getString(fieldName)
    }
}
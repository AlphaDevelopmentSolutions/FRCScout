package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.ContentValues
import java.util.*

class MasterContentValues
{
    val contentValues = ContentValues()

    fun put(key: String, value: Any?)
    {
        when(value)
        {
            is String -> contentValues.put(key, value)
            is Int -> contentValues.put(key, value)
            is Long -> contentValues.put(key, value)
            is Boolean -> contentValues.put(key, if(value) 1 else 0)
            is Date -> contentValues.put(key, value.time)
            value == null -> contentValues.putNull(key)
        }
    }
}
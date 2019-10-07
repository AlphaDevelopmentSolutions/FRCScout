package com.alphadevelopmentsolutions.frcscout.Classes

import android.database.Cursor
import java.util.*

class DatabaseCursor(private val cursor: Cursor?)
{
    val count: Int
        get() { return cursor?.count ?: 0 }

    fun getDate(columnIndex: Int): Date
    {
        return Date(cursor?.getLong(columnIndex) ?: 0)
    }

    fun getString(columnIndex: Int): String
    {
        return cursor?.getString(columnIndex) ?: ""
    }

    fun getInt(columnIndex: Int): Int
    {
        return cursor?.getInt(columnIndex) ?: -1
    }

    fun getBoolean(columnIndex: Int): Boolean
    {
        return cursor?.getInt(columnIndex) == 1
    }

    fun getBooleanOrNull(columnIndex: Int): Boolean?
    {
        return if(cursor?.isNull(columnIndex) == true) null else cursor?.getInt(columnIndex) == 1
    }

    fun getStringOrNull(columnIndex: Int): String?
    {
        return if(cursor?.isNull(columnIndex) == true) null else cursor?.getString(columnIndex)
    }

    fun getIntOrNull(columnIndex: Int): Int?
    {
        return if(cursor?.isNull(columnIndex) == true) null else cursor?.getInt(columnIndex)
    }

    fun getDateOrNull(columnIndex: Int): Date?
    {
        return if(cursor?.isNull(columnIndex) == true) null else Date(cursor?.getLong(columnIndex) ?: 0)
    }

    fun moveToNext(): Boolean
    {
        return cursor?.moveToNext() ?: false
    }

    fun close()
    {
        cursor?.close()
    }

    fun getColumnIndex(columnName: String): Int
    {
        return cursor?.getColumnIndex(columnName) ?: -1
    }

    fun moveToFirst()
    {
        cursor?.moveToFirst()
    }
}
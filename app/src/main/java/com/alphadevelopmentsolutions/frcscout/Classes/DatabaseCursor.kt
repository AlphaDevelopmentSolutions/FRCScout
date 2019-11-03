package com.alphadevelopmentsolutions.frcscout.Classes

import android.database.Cursor
import java.util.*

class DatabaseCursor(private val cursor: Cursor?)
{
    val count: Int
        get() { return cursor?.count ?: 0 }

    fun getDate(columnName: String): Date
    {
        return Date(cursor?.getLong(getColumnIndex(columnName)) ?: 0)
    }

    fun getString(columnName: String): String
    {
        return cursor?.getString(getColumnIndex(columnName)) ?: ""
    }

    fun getInt(columnName: String): Int
    {
        return cursor?.getInt(getColumnIndex(columnName)) ?: -1
    }

    fun getLong(columnName: String): Long
    {
        return cursor?.getLong(getColumnIndex(columnName)) ?: -1
    }

    fun getBoolean(columnName: String): Boolean
    {
        return cursor?.getInt(getColumnIndex(columnName)) == 1
    }

    fun getBooleanOrNull(columnName: String): Boolean?
    {
        return if(cursor?.isNull(getColumnIndex(columnName)) == true) null else cursor?.getInt(getColumnIndex(columnName)) == 1
    }

    fun getStringOrNull(columnName: String): String?
    {
        return if(cursor?.isNull(getColumnIndex(columnName)) == true) null else cursor?.getString(getColumnIndex(columnName))
    }

    fun getIntOrNull(columnName: String): Int?
    {
        return if(cursor?.isNull(getColumnIndex(columnName)) == true) null else cursor?.getInt(getColumnIndex(columnName))
    }

    fun getDateOrNull(columnName: String): Date?
    {
        return if(cursor?.isNull(getColumnIndex(columnName)) == true) null else Date(cursor?.getLong(getColumnIndex(columnName)) ?: 0)
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
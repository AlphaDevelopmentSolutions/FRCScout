package com.alphadevelopmentsolutions.frcscout.classes

import android.database.sqlite.SQLiteDatabase

class CustomSQLiteDatabase
{
    var database: SQLiteDatabase? = null

    var isOpen: Boolean
    get() { return database?.isOpen ?: false }
    set(value) {}

    fun beginTransaction()
    {
        database?.beginTransaction()
    }

    fun finishTransaction()
    {
        database?.setTransactionSuccessful()
        database?.endTransaction()
    }

    fun inTransaction(): Boolean
    {
        return database?.inTransaction() ?: false
    }

    fun execSQL(sql: String)
    {
        database?.execSQL(sql)
    }

    fun query(table: String, columns: Array<String>, selection: String, selectionArgs: Array<String>, groupBy: String?, having: String?, orderBy: String?): DatabaseCursor?
    {
        with(database?.query(table, columns, selection, selectionArgs, groupBy, having, orderBy))
        {
            return if(this != null)
                DatabaseCursor(this)
            else
                null
        }
    }

    fun update(table: String, values: MasterContentValues, whereClause: String, whereArgs: Array<String>): Int
    {
        return database?.update(table, values.contentValues, whereClause, whereArgs)?: -1
    }

    fun insert(table: String, nullColumnHack: String?, values: MasterContentValues): Long
    {
        return database?.insert(table, nullColumnHack, values.contentValues)?: -1
    }

    fun delete(table: String, whereClause: String, whereArgs: Array<String>): Int
    {
        return database?.delete(table, whereClause, whereArgs)?: -1
    }

}
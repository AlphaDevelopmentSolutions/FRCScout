package com.alphadevelopmentsolutions.frcscout.Classes

import android.database.Cursor
import android.database.SQLException
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Exceptions.DatabaseException
import com.alphadevelopmentsolutions.frcscout.Exceptions.UnauthorizedClassException
import com.alphadevelopmentsolutions.frcscout.Interfaces.AppLog
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.reflect.KClass
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaField
import kotlin.reflect.jvm.jvmName

class Database(context: MainActivity)
{
    private val databaseHelper: DatabaseHelper = DatabaseHelper(context)
    private var db: CustomSQLiteDatabase = CustomSQLiteDatabase()

    /**
     * Checks if the database is currently open
     *
     * @return boolean if database is open
     */
    val isOpen: Boolean
        get() = db.isOpen

    enum class SortDirection
    {
        ASC,
        DESC
    }

    /**
     * Starts a transaction when inserting a large number of records
     */
    fun beginTransaction()
    {
        db.beginTransaction()
    }

    /**
     * Completes the transaction that was previously opened
     */
    fun finishTransaction()
    {
        db.finishTransaction()
    }

    /**
     * Returns if the database is currently in a transaction
     */
    fun inTransaction(): Boolean
    {
        return db.inTransaction()
    }

    /**
     * Opens the database for usage
     *
     * @return boolean if database was opened successfully
     */
    fun open(): Boolean
    {
        return try
        {
            db.database = databaseHelper.writableDatabase
            true
        } catch (e: SQLException)
        {
            AppLog.error(e)
            false
        }

    }

    /**
     * Closes the database after usage
     *
     * @return boolean if database was closed successfully
     */
    fun close(): Boolean
    {
        return try
        {
            databaseHelper.close()
            true
        } catch (e: SQLException)
        {
            AppLog.error(e)
            false
        }

    }

    /**
     * Gets objects from the database
     * @param tableName [String] name of the table to select from
     * @param whereStatement [String]? SQL WHERE statement for what rows to return, args to be set with ?
     * @param whereArgs [Array]? list of args to replace ? [whereStatement]
     * @param groupBy [String]? filter specifying how to group records formatted as an SQL GROUP statement
     * @param orderBy [String]? filter specifying how to order records formatted as an SQL ORDER BY statement
     * @return [Cursor] with rows selected from the query
     */
    fun getObjects(
            tableName: String,
            whereStatement: String? = null,
            whereArgs: ArrayList<String>? = null,
            groupBy: String? = null,
            orderBy: String? = null
    ): DatabaseCursor?
    {
        AppLog.log("Database Get", "Getting $tableName objects from the database WHERE $whereStatement $whereArgs")

        return db.query(
                tableName,
                null,
                whereStatement.toString(),
                whereArgs?.toTypedArray(),
                groupBy,
                null,
                orderBy)
    }

    /**
     * Inserts or updates records to the database
     * @param table [Table] object to be inserted or updated in the database
     * @return [Long] if record is inserted, returns ID of record or -1 if failure
     *                else record is updated, returns number of rows affected
     */
    fun insertOrUpdateObject(table: Table): Long {

        //Record exists, update
        if (table.localId > 0) {
            AppLog.log("Database Update", "Saving ${table.tableName} with Local Id: ${table.localId} and Server Id: ${table.serverId}")

            val whereStatement = "${quote(Table.COLUMN_NAME_LOCAL_ID)} =  ?"
            val whereArgs = arrayOf(table.localId.toString())

            // Rows affected
            return db.update(
                    table.tableName,
                    table.tableValues,
                    whereStatement,
                    whereArgs).toLong()
        }

        //Record doesn't exist, insert
        else {
            AppLog.log("Database Insert", "Saving ${table.tableName} with Local Id: ${table.localId} and Server Id: ${table.serverId}")

            // Row ID if inserted, or -1 if failed
            return db.insert(
                    table.tableName,
                    null,
                    table.tableValues.apply {
                        remove(Table.COLUMN_NAME_LOCAL_ID)
                    }
            )
        }
    }

    /**
     * Deletes an object from the database
     * @param table [Table] object to be deleted
     * @return [Boolean] if deletion was successful
     */
    fun deleteObject(table: Table): Boolean {
        if (table.localId > 0) {
            AppLog.log("Database Delete", "Deleting ${table.tableName} with Local Id: {${table.localId} and Server Id: ${table.serverId} from the database")

            val whereStatement = "${Table.COLUMN_NAME_LOCAL_ID} =  ?"
            val whereArgs = arrayOf(table.localId.toString())

            // Rows affected > 0
            return db.delete(table.tableName, whereStatement, whereArgs) > 0
        }

        AppLog.error(DatabaseException("Database Delete FAILURE", "**FAILURE Deleting ${table.tableName} with Local Id: {${table.localId} and Server Id: ${table.serverId} from the database FAILURE**"))

        return false
    }

    /**
     * Deletes an object from the database
     * @param tableName [String] name of table
     * @param clearDrafts [Boolean] if drafts from the table should be cleared
     *                              if null, clear everything
     * @return [Boolean] if deletion was successful
     */
    fun clearTable(tableName: String, clearDrafts: Boolean? = null): Boolean {
        when (clearDrafts) {
            true ->
            {
                AppLog.log("Database Table Clear", "Clearing all $tableName records from the database.")

                // 0 because no where arg given
                return db.delete(tableName, null, null) == 0
            }

            false ->
            {
                AppLog.log("Database Table Clear", "Clearing non-draft $tableName records from the database.")

                val whereStatement = "${Table.COLUMN_NAME_IS_DRAFT} =  ?"
                val whereArgs = arrayOf("1")

                // Rows affected
                db.delete(tableName, whereStatement, whereArgs)

                return true
            }

            null ->
            {
                AppLog.log("Database Table Clear", "Clearing all $tableName records from the database.")

                // 0 because no where arg given
                return db.delete(tableName, null, null) == 0
            }
        }
    }


    companion object
    {
        fun quote(columnName: String): String {
            return "`$columnName`"
        }
    }


}

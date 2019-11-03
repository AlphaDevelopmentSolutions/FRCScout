package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)
{

    override fun onCreate(db: SQLiteDatabase)
    {
        Year.createTable(db)
        Event.createTable(db)
        EventTeamList.createTable(db)
        Match.createTable(db)
        Team.createTable(db)

        Robot.createTable(db)
        RobotMedia.createTable(db)

        ScoutCardInfo.createTable(db)
        ScoutCardInfoKey.createTable(db)
        RobotInfo.createTable(db)
        RobotInfoKey.createTable(db)

        User.createTable(db)

        ChecklistItem.createTable(db)
        ChecklistItemResult.createTable(db)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventTeamList.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Year.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Event.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Match.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Team.TABLE_NAME)

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Robot.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RobotMedia.TABLE_NAME)

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScoutCardInfo.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScoutCardInfoKey.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RobotInfo.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RobotInfoKey.TABLE_NAME)

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME)

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChecklistItem.TABLE_NAME)
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChecklistItemResult.TABLE_NAME)

        onCreate(sqLiteDatabase)
    }

    companion object
    {

        private val DB_VERSION = 16
        private val DB_NAME = "FRCScout.db"
    }
}

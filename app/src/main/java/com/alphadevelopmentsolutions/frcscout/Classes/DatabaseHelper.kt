package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION)
{

    override fun onCreate(db: SQLiteDatabase)
    {
        db.execSQL(Year.CREATE_TABLE)
        db.execSQL(Event.CREATE_TABLE)
        db.execSQL(EventTeamList.CREATE_TABLE)
        db.execSQL(Match.CREATE_TABLE)
        db.execSQL(Team.CREATE_TABLE)

        db.execSQL(Robot.CREATE_TABLE)
        db.execSQL(RobotMedia.CREATE_TABLE)

        db.execSQL(ScoutCardInfo.CREATE_TABLE)
        db.execSQL(ScoutCardInfoKey.CREATE_TABLE)
        db.execSQL(RobotInfo.CREATE_TABLE)
        db.execSQL(RobotInfoKey.CREATE_TABLE)

        db.execSQL(User.CREATE_TABLE)

        db.execSQL(ChecklistItem.CREATE_TABLE)
        db.execSQL(ChecklistItemResult.CREATE_TABLE)
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

        private val DB_VERSION = 15
        private val DB_NAME = "FRCScout.db"
    }
}

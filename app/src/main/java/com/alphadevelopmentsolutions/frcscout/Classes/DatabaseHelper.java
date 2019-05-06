package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DB_VERSION = 10;
    private static final String DB_NAME = "FRCScout.db";


    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Event.CREATE_TABLE);
        db.execSQL(EventTeamList.CREATE_TABLE);
        db.execSQL(Match.CREATE_TABLE);
        db.execSQL(Team.CREATE_TABLE);

        db.execSQL(Robot.CREATE_TABLE);
        db.execSQL(RobotMedia.CREATE_TABLE);

        db.execSQL(ScoutCard.CREATE_TABLE);
        db.execSQL(PitCard.CREATE_TABLE);

        db.execSQL(User.CREATE_TABLE);

        db.execSQL(ChecklistItem.CREATE_TABLE);
        db.execSQL(ChecklistItemResult.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventTeamList.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Event.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Match.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Team.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Robot.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RobotMedia.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScoutCard.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PitCard.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChecklistItem.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChecklistItemResult.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}

package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "FRCScout.db";


    private final String CREATE_EVENTS_TABLE =
            "CREATE TABLE " + Event.TABLE_NAME + " (" +
                    Event.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Event.COLUMN_NAME_NAME + " TEXT," +
                    Event.COLUMN_NAME_CITY + " TEXT," +
                    Event.COLUMN_NAME_STATEPROVINCE + " TEXT," +
                    Event.COLUMN_NAME_COUNTRY + " TEXT," +
                    Event.COLUMN_NAME_STARTDATE + " INTEGER," +
                    Event.COLUMN_NAME_ENDDATE + " INTEGER)";

    private final String CREATE_TEAMS_TABLE =
            "CREATE TABLE " + Team.TABLE_NAME + " (" +
                    Team.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Team.COLUMN_NAME_NAME + " TEXT," +
                    Team.COLUMN_NAME_NUMBER + " INTEGER," +
                    Team.COLUMN_NAME_CITY + " TEXT," +
                    Team.COLUMN_NAME_STATEPROVINCE + " TEXT," +
                    Team.COLUMN_NAME_COUNTRY + " TEXT," +
                    Team.COLUMN_NAME_ROOKIE_YEAR + " INTEGER," +
                    Team.COLUMN_NAME_WEBSITE + " TEXT)";

    private final String CREATE_ROBOTS_TABLE =
            "CREATE TABLE " + Robot.TABLE_NAME +" (" +
                    Robot.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Robot.TABLE_NAME + " TEXT," +
                    Robot.COLUMN_NAME_TEAM_NUMBER + " INTEGER)";

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_EVENTS_TABLE);
        db.execSQL(CREATE_TEAMS_TABLE);
        db.execSQL(CREATE_ROBOTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newvVersion)
    {

    }
}

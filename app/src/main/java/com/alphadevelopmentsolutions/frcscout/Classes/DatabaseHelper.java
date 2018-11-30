package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "FRCScout.db";

    private final String CREATE_EVENTS_TABLE =
            "CREATE TABLE events (" +
                    "Id INTEGER PRIMARY KEY," +
                    "Name TEXT," +
                    "City TEXT," +
                    "StateProvince TEXT," +
                    "Country TEXT," +
                    "StartDate TEXT," +
                    "EndDate TEXT)";

    private final String CREATE_TEAMS_TABLE =
            "CREATE TABLE teams (" +
                    "Id INTEGER PRIMARY KEY," +
                    "Name TEXT," +
                    "Number INTEGER," +
                    "City TEXT," +
                    "StateProvince TEXT," +
                    "Country TEXT," +
                    "RookieYear INTEGER," +
                    "Website TEXT)";

    private final String CREATE_ROBOTS_TABLE =
            "CREATE TABLE robots (" +
                    "Id INTEGER PRIMARY KEY," +
                    "Name TEXT," +
                    "TeamNumber TEXT)";

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

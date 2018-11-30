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
                    "id INTEGER PRIMARY KEY," +
                    "name TEXT," +
                    "location TEXT," +
                    "startDate TEXT," +
                    "endDate TEXT)";

    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}

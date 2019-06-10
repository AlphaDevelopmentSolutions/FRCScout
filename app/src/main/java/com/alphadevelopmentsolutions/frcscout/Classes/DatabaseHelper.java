package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItem;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ChecklistItemResult;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Event;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.EventTeamList;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Match;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Robot;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfo;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotInfoKey;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.RobotMedia;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCard;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.ScoutCardInfoKey;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Team;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.User;
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.Year;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DB_VERSION = 12;
    private static final String DB_NAME = "FRCScout.db";


    public DatabaseHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Year.CREATE_TABLE);
        db.execSQL(Event.CREATE_TABLE);
        db.execSQL(EventTeamList.CREATE_TABLE);
        db.execSQL(Match.CREATE_TABLE);
        db.execSQL(Team.CREATE_TABLE);

        db.execSQL(Robot.CREATE_TABLE);
        db.execSQL(RobotMedia.CREATE_TABLE);

        db.execSQL(ScoutCard.CREATE_TABLE);
        db.execSQL(ScoutCardInfoKey.CREATE_TABLE);
        db.execSQL(RobotInfo.CREATE_TABLE);
        db.execSQL(RobotInfoKey.CREATE_TABLE);

        db.execSQL(User.CREATE_TABLE);

        db.execSQL(ChecklistItem.CREATE_TABLE);
        db.execSQL(ChecklistItemResult.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + EventTeamList.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Year.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Event.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Match.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Team.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Robot.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RobotMedia.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScoutCard.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScoutCardInfoKey.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RobotInfo.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RobotInfoKey.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + User.TABLE_NAME);

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChecklistItem.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ChecklistItemResult.TABLE_NAME);

        onCreate(sqLiteDatabase);
    }
}

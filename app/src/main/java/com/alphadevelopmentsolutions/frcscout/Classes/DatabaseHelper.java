package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{

    private static final int DB_VERSION = 2;
    private static final String DB_NAME = "FRCScout.db";


    private final String CREATE_EVENTS_TABLE =
            "CREATE TABLE " + Event.TABLE_NAME + " (" +
                    Event.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Event.COLUMN_NAME_BLUE_ALLIANCE_ID + " TEXT," +
                    Event.COLUMN_NAME_NAME + " TEXT," +
                    Event.COLUMN_NAME_CITY + " TEXT," +
                    Event.COLUMN_NAME_STATEPROVINCE + " TEXT," +
                    Event.COLUMN_NAME_COUNTRY + " TEXT," +
                    Event.COLUMN_NAME_START_DATE + " INTEGER," +
                    Event.COLUMN_NAME_END_DATE + " INTEGER)";

    private final String CREATE_TEAMS_TABLE =
            "CREATE TABLE " + Team.TABLE_NAME + " (" +
                    Team.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Team.COLUMN_NAME_NAME + " TEXT," +
                    Team.COLUMN_NAME_CITY + " TEXT," +
                    Team.COLUMN_NAME_STATEPROVINCE + " TEXT," +
                    Team.COLUMN_NAME_COUNTRY + " TEXT," +
                    Team.COLUMN_NAME_ROOKIE_YEAR + " INTEGER," +
                    Team.COLUMN_NAME_FACEBOOK_URL + " TEXT," +
                    Team.COLUMN_NAME_TWITTER_URL + " TEXT," +
                    Team.COLUMN_NAME_INSTAGRAM_URL + " TEXT," +
                    Team.COLUMN_NAME_YOUTUBE_URL + " TEXT," +
                    Team.COLUMN_NAME_WEBSITE_URL + " TEXT," +
                    Team.COLUMN_NAME_IMAGE_FILE_URI + " TEXT)";

    private final String CREATE_ROBOTS_TABLE =
            "CREATE TABLE " + Robot.TABLE_NAME +" (" +
                    Robot.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Robot.COLUMN_NAME_NAME + " TEXT," +
                    Robot.COLUMN_NAME_TEAM_NUMBER + " INTEGER)";

    private final String CREATE_MATCHES_TABLE =
            "CREATE TABLE " + Match.TABLE_NAME +" (" +
                    Match.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    Match.COLUMN_NAME_DATE + " INTEGER," +
                    Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_ID + " INTEGER," +
                    Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_ID + " INTEGER," +
                    Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_ID + " INTEGER," +
                    Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID + " INTEGER," +
                    Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID + " INTEGER," +
                    Match.COLUMN_NAME_BLUE_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID + " INTEGER," +
                    Match.COLUMN_NAME_BLUE_ALLIANCE_SCORE + " INTEGER," +
                    Match.COLUMN_NAME_RED_ALLIANCE_SCORE + " INTEGER," +
                    Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_ID + " INTEGER," +
                    Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_ID + " INTEGER," +
                    Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_ID + " INTEGER," +
                    Match.COLUMN_NAME_RED_ALLIANCE_TEAM_ONE_SCOUT_CARD_ID + " INTEGER," +
                    Match.COLUMN_NAME_RED_ALLIANCE_TEAM_TWO_SCOUT_CARD_ID + " INTEGER," +
                    Match.COLUMN_NAME_RED_ALLIANCE_TEAM_THREE_SCOUT_CARD_ID + " INTEGER)";

    private final String CREATE_SCOUT_CARDS_TABLE =
            "CREATE TABLE " + ScoutCard.TABLE_NAME +" (" +
                    ScoutCard.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    ScoutCard.COLUMNS_NAME_MATCH_ID + " INTEGER," +
                    ScoutCard.COLUMN_NAME_TEAM_ID + " INTEGER," +
                    ScoutCard.COLUMN_NAME_EVENT_ID + " TEXT," +
                    ScoutCard.COLUMN_NAME_ALLIANCE_COLOR + " TEXT," +
                    ScoutCard.COLUMN_NAME_BLUE_ALLIANCE_FINAL_SCORE + " INTEGER," +
                    ScoutCard.COLUMN_NAME_RED_ALLIANCE_FINAL_SCORE + " INTEGER," +
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_EXIT_HABITAT + " TEXT," +
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_HATCH_PANELS_SECURED + " INTEGER," +
                    ScoutCard.COLUMN_NAME_AUTONOMOUS_CARGO_STORED + " INTEGER," +
                    ScoutCard.COLUMN_NAME_TELEOP_HATCH_PANELS_SECURED + " INTEGER," +
                    ScoutCard.COLUMN_NAME_TELEOP_CARGO_STORED + " INTEGER," +
                    ScoutCard.COLUMN_NAME_TELEOP_ROCKETS_COMPLETED + " INTEGER," +
                    ScoutCard.COLUMN_NAME_END_GAME_RETURNED_TO_HABITAT + " TEXT," +
                    ScoutCard.COLUMN_NAME_NOTES + " TEXT," +
                    ScoutCard.COLUMN_NAME_COMPLETED_BY + " TEXT," +
                    ScoutCard.COLUMN_NAME_COMPLETED_DATE + " INTEGER)";

    private final String CREATE_USERS_TABLE =
            "CREATE TABLE " + User.TABLE_NAME +" (" +
                    User.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    User.COLUMN_NAME_FIRST_NAME + " TEXT," +
                    User.COLUMN_NAME_LAST_NAME + " TEXT)";

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
        db.execSQL(CREATE_MATCHES_TABLE);
        db.execSQL(CREATE_SCOUT_CARDS_TABLE);
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newvVersion)
    {
        if(oldVersion == 1 && newvVersion == 2)
        {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ScoutCard.TABLE_NAME);

            sqLiteDatabase.execSQL(CREATE_SCOUT_CARDS_TABLE);
        }
    }
}

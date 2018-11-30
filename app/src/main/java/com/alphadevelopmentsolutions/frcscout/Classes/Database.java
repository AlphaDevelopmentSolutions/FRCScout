package com.alphadevelopmentsolutions.frcscout.Classes;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class Database
{
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;

    Database(Context context)
    {
        databaseHelper = new DatabaseHelper(context);
    }

    /**
     * Opens the database for usage
     * @return boolean if database was opened successfully
     */
    public boolean open()
    {
        try
        {
            db = databaseHelper.getWritableDatabase();
            return true;
        }
        catch(SQLException e)
        {
            return false;
        }

    }

    /**
     * Closes the database after usage
     * @return boolean if database was closed successfully
     */
    public boolean close()
    {
        try
        {
            databaseHelper.close();
            return true;
        }
        catch(SQLException e)
        {
            return false;
        }

    }
}

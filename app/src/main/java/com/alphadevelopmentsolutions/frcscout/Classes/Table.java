package com.alphadevelopmentsolutions.frcscout.Classes;

import com.alphadevelopmentsolutions.frcscout.Exceptions.MissingFieldException;

public abstract class Table
{

    private static final String TABLE_NAME_VAR = "TABLE_NAME";

    abstract public boolean load(Database database);
    abstract public int save(Database database);
    abstract public boolean delete(Database database);
    abstract public String toString();

    protected Table()
    {
        //attempt to retrieve the table name field, throw exception if not present
        try
        {
            this.getClass().getDeclaredField(TABLE_NAME_VAR);
        }
        catch (NoSuchFieldException e)
        {
            throw new MissingFieldException(e);
        }

    }
}

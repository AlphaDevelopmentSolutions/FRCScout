package com.alphadevelopmentsolutions.frcscout.Exceptions;

public class MissingFieldException extends RuntimeException
{
    private NoSuchFieldException e;

    public MissingFieldException(NoSuchFieldException e)
    {
        this.e = e;
    }

    @Override
    public String toString()
    {
        return e.toString();
    }
}

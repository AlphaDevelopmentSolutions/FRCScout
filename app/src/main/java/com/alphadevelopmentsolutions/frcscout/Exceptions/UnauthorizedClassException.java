package com.alphadevelopmentsolutions.frcscout.Exceptions;

public class UnauthorizedClassException extends RuntimeException
{
    private Class authorizedClass;

    public UnauthorizedClassException(Class authorizedClass)
    {
        this.authorizedClass = authorizedClass;
    }

    @Override
    public String toString()
    {
        return "Only children of " + authorizedClass.getName() + " can call this method.";
    }
}

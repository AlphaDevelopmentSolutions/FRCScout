package com.alphadevelopmentsolutions.frcscout.Exceptions

class MissingFieldException(private val e: NoSuchFieldException) : RuntimeException()
{

    override fun toString(): String
    {
        return e.toString()
    }
}

package com.alphadevelopmentsolutions.frcscout.Exceptions

class UnauthorizedClassException(private val authorizedClass: Class<*>) : RuntimeException()
{

    override fun toString(): String
    {
        return "Only children of " + authorizedClass.name + " can call this method."
    }
}

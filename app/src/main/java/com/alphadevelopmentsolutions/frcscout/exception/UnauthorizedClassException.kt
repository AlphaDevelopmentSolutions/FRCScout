package com.alphadevelopmentsolutions.frcscout.exception

class UnauthorizedClassException(private val authorizedClass: Class<*>) : RuntimeException()
{

    override fun toString(): String
    {
        return "Only children of " + authorizedClass.name + " can call this method."
    }
}

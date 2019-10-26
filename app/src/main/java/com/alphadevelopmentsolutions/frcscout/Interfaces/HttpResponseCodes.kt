package com.alphadevelopmentsolutions.frcscout.Interfaces

interface HttpResponseCodes
{
    companion object
    {
        const val OK = 200
        const val BAD_REQUEST = 400
        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val ERROR = 500
        const val NOT_IMPLEMENTED = 501
    }
}
package com.alphadevelopmentsolutions.frcscout.Interfaces

import android.util.Log
import com.alphadevelopmentsolutions.frcscout.BuildConfig

interface AppLog
{
    companion object
    {
        fun log(title: String, message: String)
        {
            if(BuildConfig.DEBUG)
                Log.d(title, message)
        }
    }
}
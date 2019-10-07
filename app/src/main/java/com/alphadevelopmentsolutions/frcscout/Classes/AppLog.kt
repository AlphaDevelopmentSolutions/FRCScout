package com.alphadevelopmentsolutions.frcscout.Classes

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
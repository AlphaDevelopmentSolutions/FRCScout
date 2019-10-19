package com.alphadevelopmentsolutions.frcscout.Interfaces

import android.util.Log
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.crashlytics.android.Crashlytics

interface AppLog
{
    companion object
    {
        fun log(title: String, message: String)
        {
            if(BuildConfig.DEBUG)
                Log.d(title, message)
        }

        fun error(exception: java.lang.Exception)
        {
            if(BuildConfig.DEBUG)
                exception.stackTrace
            else
                Crashlytics.logException(exception)
        }
    }
}
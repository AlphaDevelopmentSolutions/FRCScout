package com.alphadevelopmentsolutions.frcscout.interfaces

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

        fun error(exception: Exception)
        {
            if(BuildConfig.DEBUG)
                exception.stackTrace
            else
                Crashlytics.logException(exception)
        }

        fun error(throwable: Throwable)
        {
            if(BuildConfig.DEBUG)
                throwable.stackTrace
            else
                Crashlytics.logException(throwable)
        }
    }
}
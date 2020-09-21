package com.alphadevelopmentsolutions.frcscout.interfaces

import android.util.Log
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase

interface AppLog
{
    companion object
    {
        fun log(title: String, message: String)
        {
            if(BuildConfig.DEBUG)
                Log.d(title, message)
            else
                FirebaseCrashlytics.getInstance().log("$title: $message")
        }

        fun error(exception: Exception)
        {
            if(BuildConfig.DEBUG)
                exception.stackTrace
            else
                FirebaseCrashlytics.getInstance().recordException(exception)
        }
    }
}
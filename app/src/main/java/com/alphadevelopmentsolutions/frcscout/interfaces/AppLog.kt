package com.alphadevelopmentsolutions.frcscout.interfaces

import android.util.Log
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase

interface AppLog
{
    companion object
    {
        fun l(title: String, message: String) {
            if(BuildConfig.DEBUG)
                Log.d(title, message)
            else {
                FirebaseCrashlytics.getInstance().log("$title: $message")
                sendReports()
            }
        }

        fun w(title: String, message: String) {
            if(BuildConfig.DEBUG)
                Log.w(title, message)
            else {
                FirebaseCrashlytics.getInstance().log(message)
                sendReports()
            }
        }

        fun e(exception: Exception) {
            if(BuildConfig.DEBUG)
                exception.printStackTrace()
            else {
                FirebaseCrashlytics.getInstance().recordException(exception)
                sendReports()
            }
        }

        fun e(throwable: Throwable) {
            if(BuildConfig.DEBUG)
                throwable.printStackTrace()
            else {
                FirebaseCrashlytics.getInstance().recordException(throwable)
                sendReports()
            }
        }

        /**
         * Sends reports directly to firebase
         */
        private fun sendReports() {
            launchIO {
                FirebaseCrashlytics.getInstance().sendUnsentReports()
            }
        }
    }
}
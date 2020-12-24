package com.alphadevelopmentsolutions.frcscout.interfaces

import android.util.Log
import com.alphadevelopmentsolutions.frcscout.BuildConfig
import com.alphadevelopmentsolutions.frcscout.extensions.launchIO
import com.google.firebase.crashlytics.FirebaseCrashlytics

interface AppLog {
    companion object {

        private const val TAG = "AppLog"

        fun l(title: String, message: String) {
            if (BuildConfig.DEBUG)
                Log.d(title, message)
            else {
                FirebaseCrashlytics.getInstance().log("$title: $message")
                sendReports()
            }
        }

        fun w(title: String, message: String) {
            if (BuildConfig.DEBUG)
                Log.w(title, message)
            else {
                FirebaseCrashlytics.getInstance().log(message)
                sendReports()
            }
        }

        fun e(throwable: Throwable) {
            if (BuildConfig.DEBUG)
                Log.e(TAG, "Exception", throwable)
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
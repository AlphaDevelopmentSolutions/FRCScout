package com.alphadevelopmentsolutions.frcscout

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.os.Handler
import android.os.Looper

class FRCScoutApplication : Application() {

    var activeActivity: Activity? = null

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                    activeActivity = activity
                }

                override fun onActivityStarted(activity: Activity) {
                    activeActivity = activity
                }

                override fun onActivityResumed(activity: Activity) {
                    activeActivity = activity
                }

                override fun onActivityPaused(activity: Activity) {
                    activeActivity = activity
                }

                override fun onActivityStopped(activity: Activity) {
                    activeActivity = null
                }

                override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
                    activeActivity = activity
                }

                override fun onActivityDestroyed(activity: Activity) {
                    activeActivity = null
                }
            }
        )
    }

    companion object {
        private val mHandler: Handler = Handler()

        fun runOnUiThread(runnable: Runnable) {
            if (Thread.currentThread() === Looper.getMainLooper().thread) {
                runnable.run()
            } else {
                mHandler.post(runnable)
            }
        }
    }
}
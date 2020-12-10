package com.alphadevelopmentsolutions.frcscout

import android.app.Application
import android.os.Handler
import android.os.Looper

class FRCScoutApplication : Application() {
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
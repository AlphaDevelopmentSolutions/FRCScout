package com.alphadevelopmentsolutions.frcscout.classes

import android.annotation.SuppressLint
import java.text.SimpleDateFormat

@SuppressLint("SimpleDateFormat")
class DateFormatter private constructor() : SimpleDateFormat() {
    companion object {
        private var INSTANCE: DateFormatter? = null

        fun getInstance(): DateFormatter {
            return INSTANCE ?: let {
                val tempInstance = DateFormatter()

                INSTANCE = tempInstance

                tempInstance
            }
        }
    }
}

package com.alphadevelopmentsolutions.frcscout.classes

import android.content.Context

class Account(
    val authToken: String = ""
) {
    companion object {
        fun getInstance(context: Context) =
            Account()
    }

    fun initialize(context: Context) {

    }
}
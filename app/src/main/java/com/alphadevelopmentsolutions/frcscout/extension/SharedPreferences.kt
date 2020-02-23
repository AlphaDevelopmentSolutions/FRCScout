package com.alphadevelopmentsolutions.frcscout.extension

import android.content.SharedPreferences
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.*

fun SharedPreferences.getUUID(key: String) =
        if (contains(key))
            try {
                UUID.fromString(getString(key, null))
            } catch (e: IllegalArgumentException) {
                null
            }
        else
            null

fun SharedPreferences.putUUID(key: String, uuid: UUID?) {
    if(uuid != null)
        edit().putString(key, uuid.toString()).apply()
    else
        edit().remove(key).apply()
}

fun SharedPreferences.getIntOrNull(key: String) =
        if(contains(key))
            getInt(key, -1)
        else
            null
package com.alphadevelopmentsolutions.frcscout.classes

import android.content.Context
import androidx.room.ColumnInfo
import com.alphadevelopmentsolutions.frcscout.data.models.User
import com.alphadevelopmentsolutions.frcscout.extensions.toUUID
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.annotations.SerializedName

class Account(
    override var firstName: String,
    override var lastName: String,
    override var email: String,
    override var username: String,
    override var description: String? = null,
    override var avatarUri: String? = null,
    val authToken: String
) : User(
    firstName,
    lastName,
    email,
    username,
    description,
    avatarUri
) {
    companion object {

        private var INSTANCE: Account? = null

        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                val tempInstance = KeyStore.getInstance(context).account

                INSTANCE = tempInstance
                tempInstance
            }
    }

    fun initialize(context: Context) {
        getInstance(context)?.let { account ->
            val id = account.id.toUUID().toString()

            FirebaseAnalytics.getInstance(context).setUserProperty("USER_UUID", id)
            FirebaseCrashlytics.getInstance().setUserId(id)
            FirebaseCrashlytics.getInstance().setCustomKey("USER_UUID", id)
        }
    }
}
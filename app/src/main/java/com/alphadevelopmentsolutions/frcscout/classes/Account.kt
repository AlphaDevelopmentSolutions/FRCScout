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
    @SerializedName("id") var id: ByteArray,
    @SerializedName("first_name") var firstName: String,
    @SerializedName("last_name") var lastName: String,
    @SerializedName("email") var email: String,
    @SerializedName("username") var username: String,
    @SerializedName("description") var description: String? = null,
    @SerializedName("avatar_uri") var avatarUri: String? = null,
    @SerializedName("auth_token") val authToken: String
) {
    companion object {

        private var INSTANCE: Account? = null

        fun getInstance(context: Context?) =
            INSTANCE ?: synchronized(this) {
                var tempInstance: Account? = null

                context?.let {
                    tempInstance = KeyStore.getInstance(it).account
                }

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
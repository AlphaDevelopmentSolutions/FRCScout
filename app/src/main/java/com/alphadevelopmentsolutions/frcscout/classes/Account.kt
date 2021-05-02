package com.alphadevelopmentsolutions.frcscout.classes

import android.content.Context
import androidx.room.ColumnInfo
import com.alphadevelopmentsolutions.frcscout.data.models.Role
import com.alphadevelopmentsolutions.frcscout.data.models.TeamAccount
import com.alphadevelopmentsolutions.frcscout.data.models.User
import com.alphadevelopmentsolutions.frcscout.data.models.UserTeamAccount
import com.alphadevelopmentsolutions.frcscout.extensions.toUUID
import com.alphadevelopmentsolutions.frcscout.singletons.KeyStore
import com.google.firebase.FirebaseApp
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
    @SerializedName("auth_token") val authToken: String,
    @SerializedName("user_team_account") var userTeamAccount: UserTeamAccount? = null,
    @SerializedName("role_matrix") var roleMatrix: Role? = null
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

        fun setInstance(account: Account, context: Context) {
            INSTANCE.let {
                INSTANCE = account

                KeyStore.getInstance(context).account = account
            }
        }

        fun initialize(context: Context) {
            getInstance(context)?.let { account ->
                val id = account.id.toUUID().toString()

                FirebaseApp.initializeApp(context)
                FirebaseAnalytics.getInstance(context).setUserProperty("USER_UUID", id)
                FirebaseCrashlytics.getInstance().setUserId(id)
                FirebaseCrashlytics.getInstance().setCustomKey("USER_UUID", id)
            }
        }
    }
}
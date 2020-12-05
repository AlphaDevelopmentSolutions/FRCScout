package com.alphadevelopmentsolutions.frcscout.singletons

import android.content.Context
import android.content.SharedPreferences
import com.alphadevelopmentsolutions.frcscout.classes.Account
import com.alphadevelopmentsolutions.frcscout.data.models.Event
import com.alphadevelopmentsolutions.frcscout.data.models.Year
import com.alphadevelopmentsolutions.frcscout.interfaces.KeyStoreKey
import java.util.*

class KeyStore private constructor(
    context: Context
) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

    /**
     * Removes the preference from the [sharedPreferences]
     * @param key [String] preference key
     */
    fun removePreference(key: String) = sharedPreferences.edit().remove(key).apply()

    /**
     * Rests all default field vals
     */
    fun resetData() = sharedPreferences.edit().clear().apply()

    companion object {
        private var INSTANCE: KeyStore? = null

        private const val NAME = "SHARED_SHARED_PREFERENCES"

        /**
         * Gets the current [KeyStore] [INSTANCE]
         * @param context [Context] used to get the [KeyStore] instance
         * @return [INSTANCE]
         */
        fun getInstance(context: Context): KeyStore {
            return INSTANCE
                ?: synchronized(this) {
                    val tempInstance =
                        KeyStore(context)

                    INSTANCE = tempInstance
                    tempInstance
                }
        }
    }

    /**
     * JSON [String] holding the apps [Account] object
     * @return [Account] object
     */
    var account: Account?
        get() {
            val json =
                sharedPreferences.getString(
                    KeyStoreKey.ACCOUNT_JSON,
                    null
                ) ?: return null

            return GsonInstance.getInstance()
                .fromJson(
                    json,
                    Account::class.java
                )
        }
        set(value) {
            sharedPreferences.edit()
                .putString(
                    KeyStoreKey.ACCOUNT_JSON,
                    GsonInstance.getInstance()
                        .toJson(value)
                )
                .apply()
        }

    /**
     * JSON [String] holding the apps auth token object
     * @return [String] object
     */
    var authToken: String?
        get()  =
            sharedPreferences.getString(
                KeyStoreKey.AUTH_TOKEN,
                null
            )
        set(value) {
            sharedPreferences.edit()
                .putString(
                    KeyStoreKey.AUTH_TOKEN,
                    value
                )
                .apply()
        }

    /**
     * JSON [String] holding the apps [Year] object
     * @return [Year] object
     */
    var selectedYear: Year?
        get() {
            val json =
                sharedPreferences.getString(
                    KeyStoreKey.YEAR_JSON,
                    null
                ) ?: return null

            return GsonInstance.getInstance()
                .fromJson(
                    json,
                    Year::class.java
                )
        }
        set(value) {
            sharedPreferences.edit()
                .putString(
                    KeyStoreKey.YEAR_JSON,
                    GsonInstance.getInstance()
                        .toJson(value)
                )
                .apply()
        }

    /**
     * JSON [String] holding the apps [Year] object
     * @return [Year] object
     */
    var selectedEvent: Event?
        get() {
            val json =
                sharedPreferences.getString(
                    KeyStoreKey.EVENT_JSON,
                    null
                ) ?: return null

            return GsonInstance.getInstance()
                .fromJson(
                    json,
                    Event::class.java
                )
        }
        set(value) {
            sharedPreferences.edit()
                .putString(
                    KeyStoreKey.EVENT_JSON,
                    GsonInstance.getInstance()
                        .toJson(value)
                )
                .apply()
        }

    /**
     * JSON [String] holding the apps [Year] object
     * @return [Year] object
     */
    var lastUpdated: Date
        get() =
            Date(
                sharedPreferences.getLong(
                    KeyStoreKey.LAST_UPDATED,
                    0
                )
            )
        set(value) {
            sharedPreferences.edit()
                .putLong(
                    KeyStoreKey.LAST_UPDATED,
                    value.time
                )
                .apply()
        }
}
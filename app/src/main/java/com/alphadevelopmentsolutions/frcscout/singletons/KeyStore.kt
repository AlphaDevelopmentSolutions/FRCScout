package com.alphadevelopmentsolutions.frcscout.singletons

import android.content.Context
import android.content.SharedPreferences
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
     * JSON [String] holding the apps [Year] object
     * @return [Year] object
     */
    var selectedYear: Year?
        get() {
            val accountJson =
                sharedPreferences.getString(
                    KeyStoreKey.YEAR_JSON,
                    null
                ) ?: return null

            return GsonInstance.getInstance()
                .fromJson(
                    accountJson,
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
    var lastUpdated: Date
        get() = Date(
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
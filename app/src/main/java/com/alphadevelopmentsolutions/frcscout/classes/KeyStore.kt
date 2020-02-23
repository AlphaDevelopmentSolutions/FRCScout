package com.alphadevelopmentsolutions.frcscout.classes

import android.content.SharedPreferences
import android.graphics.Color
import android.preference.PreferenceManager
import com.alphadevelopmentsolutions.frcscout.activity.MainActivity
import com.alphadevelopmentsolutions.frcscout.extension.getIntOrNull
import com.alphadevelopmentsolutions.frcscout.extension.getUUID
import com.alphadevelopmentsolutions.frcscout.extension.putUUID
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import java.util.*

class KeyStore private constructor (val context: MainActivity)
{
    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    companion object
    {
        private var INSTANCE: KeyStore? = null

        fun getInstance(context: MainActivity): KeyStore
        {
            return INSTANCE ?: let {
                val tempInstance = KeyStore(context)
                INSTANCE = tempInstance
                return tempInstance
            }
        }
    }

    /**
     * Sets or adds a preference into the shared prefs
     * @param key [String] pref key
     * @param value [Any] pref value
     */
    private fun setPreference(key: String, value: Any)
    {
        with(sharedPreferences.edit())
        {

            when (value)
            {
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Long -> putLong(key, value)
                is Float -> putFloat(key, value)
            }

            apply()
        }
    }
    
    /**
     * Gets the shared preference value
     * @param key [String] pref key
     * @param defaultValue [Any] default pref value
     */
    private fun getPreference(key: String, defaultValue: Any): Any?
    {
        return with(sharedPreferences)
        {
            when (defaultValue)
            {
                is String -> getString(key, defaultValue)
                is Int -> getInt(key, defaultValue)
                is Boolean -> getBoolean(key, defaultValue)
                is Long -> getLong(key, defaultValue)
                is Float -> getFloat(key, defaultValue)

                else -> defaultValue
            }
        }
    }

    /**
     * Rests all default field vals
     */
    fun resetData()
    {
        with(Constants.SharedPrefKeys)
        {
            setPreference(API_KEY_KEY, "")
            setPreference(API_CORE_USERNAME, "")
            setPreference(API_CORE_PASSWORD, "")

            setPreference(PRIMARY_COLOR_KEY, "")
            setPreference(PRIMARY_COLOR_DARK_KEY, "")
            setPreference(TEAM_ROBOT_MEDIA_DIR_KEY, "")

            setPreference(SELECTED_EVENT_KEY, -1)
            setPreference(SELECTED_YEAR_KEY, Calendar.getInstance().get(Calendar.YEAR))
        }
    }

    var apiKey: String? = null
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.API_KEY_KEY, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.SharedPrefKeys.API_KEY_KEY, value).apply()
            field = value
        }

    var coreUsername: String? = null
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.API_CORE_USERNAME, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.SharedPrefKeys.API_CORE_USERNAME, value).apply()
            field = value
        }

    var corePassword: String? = null
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.API_CORE_PASSWORD, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.SharedPrefKeys.API_CORE_PASSWORD, value).apply()
            field = value
        }

    var teamNumber: Int? = null
        get() = sharedPreferences.getIntOrNull(Constants.SharedPrefKeys.TEAM_NUMBER_KEY)
        set(value) {

            if(value != null)
                sharedPreferences.edit().putInt(Constants.SharedPrefKeys.TEAM_NUMBER_KEY, value).apply()

            else
                sharedPreferences.edit().remove(Constants.SharedPrefKeys.TEAM_NUMBER_KEY).apply()

            field = value
        }

    var teamName: String? = null
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.TEAM_NAME_KEY, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.SharedPrefKeys.TEAM_NAME_KEY, value).apply()
            field = value
        }

    var teamId: UUID? = null
        get() = sharedPreferences.getUUID(Constants.SharedPrefKeys.TEAM_ID_KEY)
        set(value) {
            sharedPreferences.putUUID(Constants.SharedPrefKeys.TEAM_ID_KEY, value)
            field = value
        }

    var appName: String? = null
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.APP_NAME_KEY, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.SharedPrefKeys.APP_NAME_KEY, value).apply()
            field = value
        }

    var primaryColor: Int? = null
        get() {
            sharedPreferences.getString(Constants.SharedPrefKeys.PRIMARY_COLOR_KEY, null)?.let {
                return Color.parseColor("#$it")
            }

            return null
        }
        set(value) {

            if(value != null)
                sharedPreferences.edit().putInt(Constants.SharedPrefKeys.PRIMARY_COLOR_KEY, value).apply()

            else
                sharedPreferences.edit().remove(Constants.SharedPrefKeys.PRIMARY_COLOR_KEY).apply()

            field = value
        }

    var primaryColorDark: Int? = null
        get() {
            sharedPreferences.getString(Constants.SharedPrefKeys.PRIMARY_COLOR_DARK_KEY, null)?.let {
                return Color.parseColor("#$it")
            }

            return null
        }
        set(value) {

            if(value != null)
                sharedPreferences.edit().putInt(Constants.SharedPrefKeys.PRIMARY_COLOR_DARK_KEY, value).apply()

            else
                sharedPreferences.edit().remove(Constants.SharedPrefKeys.PRIMARY_COLOR_DARK_KEY).apply()

            field = value
        }

    var robotMediaDir: String? = null
        get() = sharedPreferences.getString(Constants.SharedPrefKeys.TEAM_ROBOT_MEDIA_DIR_KEY, null)
        set(value) {
            sharedPreferences.edit().putString(Constants.SharedPrefKeys.TEAM_ROBOT_MEDIA_DIR_KEY, value).apply()
            field = value
        }

    var selectedEventId: UUID? = null
        get() = sharedPreferences.getUUID(Constants.SharedPrefKeys.SELECTED_EVENT_KEY)
        set(value) {
            sharedPreferences.putUUID(Constants.SharedPrefKeys.SELECTED_EVENT_KEY, value)
            field = value
        }

    var selectedYearId: UUID? = null
        get() = sharedPreferences.getUUID(Constants.SharedPrefKeys.SELECTED_YEAR_KEY)
        set(value) {
            sharedPreferences.putUUID(Constants.SharedPrefKeys.SELECTED_YEAR_KEY, value)
            field = value
        }

    /**
     * Check all the shared pref settings to validate the app is setup with the required info
     * @return [Boolean] if config is valid
     */
    fun validateApiConfig(): Boolean
    {
        return with(Constants.SharedPrefKeys)
        {
            getPreference(API_KEY_KEY, "") != ""
        }
    }

}
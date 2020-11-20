package com.alphadevelopmentsolutions.frcscout.classes

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.alphadevelopmentsolutions.frcscout.activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.interfaces.Constants
import java.util.*

class KeyStore(val context: MainActivity? = null)
{
    private var sharedPreferences: SharedPreferences? = null

    init
    {
        if(context != null)
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    }

    /**
     * Sets or adds a preference into the shared prefs
     * @param key [String] pref key
     * @param value [Any] pref value
     */
    fun setPreference(key: String, value: Any)
    {
        with(sharedPreferences!!.edit())
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
    fun getPreference(key: String, defaultValue: Any): Any
    {
        return with(sharedPreferences!!)
        {
            when (defaultValue) {
                is String -> getString(key, defaultValue)
                is Int -> getInt(key, defaultValue)
                is Boolean -> getBoolean(key, defaultValue)
                is Long -> getLong(key, defaultValue)
                is Float -> getFloat(key, defaultValue)
                else -> defaultValue
            } as Any
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

            setPreference(DOWNLOAD_EVENTS_KEY, true)
            setPreference(DOWNLOAD_MATCHES_KEY, true)
            setPreference(DOWNLOAD_TEAMS_KEY, true)
            setPreference(DOWNLOAD_CHECKLISTS_KEY, true)
            setPreference(DOWNLOAD_ROBOT_INFO_KEY, true)
            setPreference(DOWNLOAD_SCOUT_CARD_INFO_KEY, true)
            setPreference(DOWNLOAD_ROBOT_MEDIA_KEY, false)

            setPreference(UPLOAD_CHECKLISTS_KEY, true)
            setPreference(UPLOAD_ROBOT_INFO_KEY, true)
            setPreference(UPLOAD_SCOUT_CARD_INFO_KEY, true)
            setPreference(UPLOAD_ROBOT_MEDIA_KEY, false)
        }
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
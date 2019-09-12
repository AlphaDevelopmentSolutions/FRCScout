package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity

class KeyStore(context: MainActivity)
{
    private var sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    /**
     * Sets or adds a preference into the shared prefs
     * @param key [String] pref key
     * @param value [Any] pref value
     */
    fun setPreference(key: String, value: Any)
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
    fun getPreference(key: String, defaultValue: Any): Any
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

}
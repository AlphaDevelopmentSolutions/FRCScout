package com.alphadevelopmentsolutions.frcscout.Interfaces

import android.os.Environment
import com.alphadevelopmentsolutions.frcscout.BuildConfig

interface Constants
{

    interface SharedPrefKeys
    {
        companion object
        {
            //Shared Pref Keys
            val API_KEY_KEY = "API_KEY" //key to access web

            val API_CORE_USERNAME = "API_CORE_USERNAME"
            val API_CORE_PASSWORD = "API_CORE_PASSWORD"

            val TEAM_NUMBER_KEY = "TEAM_NUMBER"
            val TEAM_NAME_KEY = "TEAM_NAME"
            val APP_NAME_KEY = "APP_NAME"

            val PRIMARY_COLOR_KEY = "PRIMARY_COLOR"
            val PRIMARY_COLOR_DARK_KEY = "PRIMARY_COLOR_DARK"

            val SELECTED_EVENT_KEY = "SELECTED_EVENT"
            val SELECTED_YEAR_KEY = "SELECTED_YEAR"
        }
    }

    companion object
    {
        val BASE_FILE_DIRECTORY = Environment.getExternalStorageDirectory().toString() + "/frcscout/"
        val ROBOT_MEDIA_DIRECTORY = BASE_FILE_DIRECTORY + "robot-media/"
        val YEAR_MEDIA_DIRECTORY = BASE_FILE_DIRECTORY + "year-media/"

        val ROBOT_MEDIA_REQUEST_CODE = 5885

        val WEB_URL = BuildConfig.API_URL
        val API_URL = "$WEB_URL/api/api.php"


    }

}

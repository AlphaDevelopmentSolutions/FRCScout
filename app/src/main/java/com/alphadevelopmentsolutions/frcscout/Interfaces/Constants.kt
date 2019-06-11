package com.alphadevelopmentsolutions.frcscout.Interfaces

import android.os.Environment

interface Constants
{

    interface SharedPrefKeys
    {
        companion object
        {
            //Shared Pref Keys
            val API_KEY_KEY = "API_KEY" //key to access web
            val WEB_URL_KEY = "WEB_URL" //web url - http(s)://subdomain.domain.com/
            val API_URL_KEY = "API_URL" //api url - http(s)://subdomain.domain.com/api/api.php

            val TEAM_NUMBER_KEY = "TEAM_NUMBER"
            val TEAM_NAME_KEY = "TEAM_NAME"

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
    }

}

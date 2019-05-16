package com.alphadevelopmentsolutions.frcscout.Interfaces;

import android.os.Environment;

public interface Constants
{
    String BASE_FILE_DIRECTORY = Environment.getExternalStorageDirectory() + "/frcscout/";
    String ROBOT_MEDIA_DIRECTORY = BASE_FILE_DIRECTORY + "robot-media/";
    String YEAR_MEDIA_DIRECTORY = BASE_FILE_DIRECTORY + "year-media/";

    int ROBOT_MEDIA_REQUEST_CODE = 5885;

    interface SharedPrefKeys
    {
        //Shared Pref Keys
        String API_KEY_KEY = "API_KEY"; //key to access web
        String WEB_URL_KEY = "WEB_URL"; //web url - http(s)://subdomain.domain.com/
        String API_URL_KEY = "API_URL"; //api url - http(s)://subdomain.domain.com/api/api.php

        String TEAM_NUMBER_KEY = "TEAM_NUMBER";
        String TEAM_NAME_KEY = "TEAM_NAME";

        String SELECTED_EVENT_KEY = "SELECTED_EVENT";
        String SELECTED_YEAR_KEY = "SELECTED_YEAR";
    }

}

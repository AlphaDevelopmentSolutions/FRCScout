package com.alphadevelopmentsolutions.frcscout.Interfaces;

import android.os.Environment;

public interface Constants
{
    String MEDIA_DIRECTORY = Environment.getExternalStorageDirectory() + "/robot-media/";

    int ROBOT_MEDIA_REQUEST_CODE = 5885;

    interface SharedPrefKeys
    {
        //Shared Pref Keys
        String API_KEY_KEY = "API_KEY"; //key to access web
        String WEB_URL_KEY = "WEB_URL"; //web url - http(s)://subdomain.domain.com/
        String API_URL_KEY = "API_URL"; //api url - http(s)://subdomain.domain.com/api/api.php

        String TEAM_NUMBER_KEY = "TEAM_NUMBER";
        String TEAM_NAME_KEY = "TEAM_NAME";
    }

}

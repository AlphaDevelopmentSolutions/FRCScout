package com.alphadevelopmentsolutions.frcscout.Interfaces;

import android.os.Environment;

public interface Constants
{
    String MEDIA_DIRECTORY = Environment.getExternalStorageDirectory() + "/robot-media/";

    int ROBOT_MEDIA_REQUEST_CODE = 5885;

//    String API_KEY_KEY = "AZv4J7t2JpMe2UzQyQqtcNAwjSrmPTcZJhT5ZXVz";
//    String WEB_URL_KEY = "http://scouting.wiredcats5885.ca/";
//    String API_URL_KEY = WEB_URL + "/api/api.php";

    interface SharedPrefKeys
    {
        //Shared Pref Keys
        String API_KEY_KEY = "API_KEY"; //key to access web
        String WEB_URL_KEY = "WEB_URL"; //web url - http(s)://subdomain.domain.com/
        String API_URL_KEY = "API_URL";//api url - http(s)://subdomain.domain.com/api/api.php
    }

}

package com.alphadevelopmentsolutions.frcscout.Interfaces;

import android.os.Environment;

public interface Constants
{
    String EVENT_ID_PREF = "EventId";

    String MEDIA_DIRECTORY = Environment.getExternalStorageDirectory() + "/robot-media/";

    int ROBOT_MEDIA_REQUEST_CODE = 5885;
}

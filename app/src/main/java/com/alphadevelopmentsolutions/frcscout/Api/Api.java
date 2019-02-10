package com.alphadevelopmentsolutions.frcscout.Api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public abstract class Api
{
    private String URL;
    private String action;

    private HashMap<String, String> postData;

    private boolean success;

    private final String MYSQL_DATE_FORMAT = "yyyy-MM-dd H:mm:ss";

    protected final String API_FIELD_NAME_STATUS = "status";
    protected final String API_FIELD_NAME_RESPONSE = "response";
    protected final String API_FIELD_NAME_STATUS_SUCCESS = "success";
    protected final String API_FIELD_NAME_STATUS_FAILED = "error";

    protected static final String API_PARAM_SESSION_KEY = "key";
    protected static final String API_PARAM_API_VERSION = "apiVersion";
    protected static final String API_PARAM_API_ACTION = "apiAction";

    protected SimpleDateFormat simpleDateFormat;

    Api(String URL, HashMap<String, String> postData)
    {
        this.URL = URL;

        if(postData != null)
            this.postData = postData;
        else
            this.postData = new HashMap<>();

        simpleDateFormat = new SimpleDateFormat(MYSQL_DATE_FORMAT);
    }

    //region Getters

    public String getURL()
    {
        return URL;
    }

    public String getAction()
    {
        return action;
    }

    /**
     * Formats the post data from the hashmap
     * @return formatted post data
     */
    public String getPostData() throws UnsupportedEncodingException
    {
        StringBuilder formattedPostData = new StringBuilder();

        //Specify version of current API
        formattedPostData
                .append("z=z");

        //add each post data to the string builder
        for(Map.Entry<String, String> pair : postData.entrySet())
        {
            //replace all spaced with %20
            String parsedKey = URLEncoder.encode(pair.getKey(), "UTF-8");
            String parsedValue = URLEncoder.encode(pair.getValue(), "UTF-8");

            formattedPostData
                    .append("&")
                    .append(parsedKey)
                    .append("=")
                    .append(parsedValue);
        }

        //return the formatted data
        return formattedPostData.toString();
    }

    public boolean isSuccess()
    {
        return success;
    }

    //endregion

    //region Setters

    public void setSuccess(boolean success)
    {
        this.success = success;
    }

    //endregion

    public abstract boolean execute();
}


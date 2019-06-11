package com.alphadevelopmentsolutions.frcscout.Api

import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.HashMap

abstract class Api internal constructor(
        //region Getters

        val url: String, private val key: String, postData: HashMap<String, String>?)
{
    val action: String? = null

    private var postData: HashMap<String, String>? = null

    //endregion

    //region Setters

    var isSuccess: Boolean = false

    private val MYSQL_DATE_FORMAT = "yyyy-MM-dd H:mm:ss"

    protected val API_FIELD_NAME_STATUS = "Status"
    protected val API_FIELD_NAME_RESPONSE = "Response"
    protected val API_FIELD_NAME_STATUS_SUCCESS = "Success"
    protected val API_FIELD_NAME_STATUS_FAILED = "error"

    protected var simpleDateFormat: SimpleDateFormat

    init
    {

        if (postData != null)
            this.postData = postData
        else
            this.postData = HashMap()

        simpleDateFormat = SimpleDateFormat(MYSQL_DATE_FORMAT)
    }

    /**
     * Formats the post data from the hashmap
     * @return formatted post data
     */
    @Throws(UnsupportedEncodingException::class)
    fun getPostData(): String
    {
        val formattedPostData = StringBuilder()

        //Specify version of current API
        formattedPostData.append("$API_PARAM_SESSION_KEY=").append(key)

        //add each post data to the string builder
        for ((key1, value) in postData!!)
        {
            //replace all spaced with %20
            val parsedKey = URLEncoder.encode(key1, "UTF-8")
            val parsedValue = URLEncoder.encode(value, "UTF-8")

            formattedPostData
                    .append("&")
                    .append(parsedKey)
                    .append("=")
                    .append(parsedValue)
        }

        //return the formatted data
        return formattedPostData.toString()
    }

    //endregion

    abstract fun execute(): Boolean

    companion object
    {

        @JvmStatic
        protected val API_PARAM_SESSION_KEY = "key"

        @JvmStatic
        protected val API_PARAM_API_VERSION = "apiVersion"

        @JvmStatic
        protected val API_PARAM_API_ACTION = "action"
    }
}


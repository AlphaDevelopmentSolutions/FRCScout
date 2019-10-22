package com.alphadevelopmentsolutions.frcscout.Api

import com.alphadevelopmentsolutions.frcscout.Activities.MainActivity
import com.alphadevelopmentsolutions.frcscout.Classes.Image
import com.alphadevelopmentsolutions.frcscout.Interfaces.AppLog
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import com.alphadevelopmentsolutions.frcscout.Interfaces.HttpResponseCodes
import org.json.JSONArray
import org.json.JSONException
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class ApiResponse(private val api: Api)
{
    var responseCode: Int = 401
    lateinit var response: JSONArray

    /**
     * Parses data from the server
     * Must be ran on a new thread
     * @return XML in string format
     * @throws IOException
     */
    @Throws(IOException::class, JSONException::class)
    fun parse(): ApiResponse
    {
        val response = queryApi()

        AppLog.log("API Response", response)

        this.response = JSONArray(response)

        //parse and return the response
        return this
    }

    /**
     * Queries the API specified at a URL
     * @return JSON Response from URL
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun queryApi(): String
    {
        //response from server
        val response = StringBuilder()

        //create the url based on the app URL and specified file
        val url = URL(api.url)

        //create a new connection to the server
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "POST"
        httpURLConnection.doOutput = true
        httpURLConnection.connectTimeout = 5000
        httpURLConnection.readTimeout = 5000

        //gather all post data to submit to the server
        val bufferedWriter = BufferedWriter(OutputStreamWriter(httpURLConnection.outputStream))
        bufferedWriter.write(api.getPostData())
        bufferedWriter.flush()
        bufferedWriter.close()

        //get header code
        responseCode = httpURLConnection.responseCode

        //read response from the server
        val bufferedReader = BufferedReader(InputStreamReader(if(responseCode == HttpResponseCodes.OK) httpURLConnection.inputStream else httpURLConnection.errorStream))

        //for each line in the response, append to the response string
        var line: String?
        do
        {
            line = bufferedReader.readLine()

            if(line != null)
                response.append(line)

        }while(line != null)



        httpURLConnection.disconnect()

        return response.toString()
    }

    /**
     * Downloads an image from the web server
     * @param fileUri path to image online
     * @return save File object
     * @throws IOException
     */
    @Throws(IOException::class)
    fun downloadImage(fileUri: String, directory: String, context: MainActivity): File
    {
        //create the url based on the app URL and specified file
        val url = URL(fileUri)

        //create a new connection to the server
        val httpURLConnection = url.openConnection() as HttpURLConnection

        //Create the folder
        val mediaFolder = Constants.getFileDirectory(context, directory)
        if (!mediaFolder.isDirectory)
            if (!mediaFolder.mkdir())
                throw IOException("Failed to make directory")

        //Create the file
        val mediaFile = File(Image.generateFileUri(mediaFolder.path).absolutePath)
        if (!mediaFile.createNewFile())
            throw IOException("Failed to create file")

        //read the input from the web server
        val inputStream = httpURLConnection.inputStream
        val fileOutputStream = FileOutputStream(mediaFile)

        val buffer = ByteArray(1024)
        var bufferLength: Int = inputStream.read(buffer)

        //start writing the media file
        while(bufferLength > 0)
        {
            if(bufferLength != -1)
                fileOutputStream.write(buffer, 0, bufferLength)

            bufferLength = inputStream.read(buffer)
        }

        //close and disconnect from the web server
        fileOutputStream.close()
        httpURLConnection.disconnect()

        return mediaFile
    }

}


package com.alphadevelopmentsolutions.frcscout.Api

import android.util.Log
import com.alphadevelopmentsolutions.frcscout.Classes.Image
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.net.HttpURLConnection
import java.net.URL


class ApiParser(private val api: Api)
{

    /**
     * Parses data from the server
     * Must be ran on a new thread
     * @return XML in string format
     * @throws IOException
     */
    @Throws(IOException::class, JSONException::class)
    fun parse(): JSONObject
    {
        val response = queryAPI()

        Log.i("API Response", response)

        //parse and return the response
        return JSONObject(response)
    }

    /**
     * Queries the API specified at a URL
     * @return JSON Response from URL
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun queryAPI(): String
    {
        //response from server
        val response = StringBuilder()

        //create the url based on the app URL and specified file
        val url = URL(api.url)

        //create a new connection to the server
        val httpURLConnection = url.openConnection() as HttpURLConnection
        httpURLConnection.requestMethod = "POST"
        httpURLConnection.doOutput = true

        //gather all post data to submit to the server
        val bufferedWriter = BufferedWriter(OutputStreamWriter(httpURLConnection.outputStream))
        bufferedWriter.write(api.getPostData())
        bufferedWriter.flush()
        bufferedWriter.close()

        //read response from the server
        val bufferedReader = BufferedReader(InputStreamReader(httpURLConnection.inputStream))

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
    fun downloadImage(fileUri: String, directory: String): File
    {
        //create the url based on the app URL and specified file
        val url = URL(fileUri)

        //create a new connection to the server
        val httpURLConnection = url.openConnection() as HttpURLConnection

        val baseDir = File(Constants.BASE_FILE_DIRECTORY)
        if (!baseDir.isDirectory)
            if (!baseDir.mkdir())
                throw IOException("Failed to make base file directory")

        //Create the folder
        val mediaFolder = File(directory)
        if (!mediaFolder.isDirectory)
            if (!mediaFolder.mkdir())
                throw IOException("Failed to make directory")


        //Create the file
        val mediaFile = File(Image.generateFileUri(directory).absolutePath)
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


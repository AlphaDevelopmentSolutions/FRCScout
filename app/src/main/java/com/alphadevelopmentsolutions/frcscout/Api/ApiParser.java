package com.alphadevelopmentsolutions.frcscout.Api;

import android.util.Log;

import com.alphadevelopmentsolutions.frcscout.Classes.Image;
import com.alphadevelopmentsolutions.frcscout.Interfaces.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


public class ApiParser
{
    private Api api;

    public ApiParser(Api api)
    {
        this.api = api;
    }

    /**
     * Parses data from the server
     * Must be ran on a new thread
     * @return XML in string format
     * @throws IOException
     */
    public JSONObject parse() throws IOException, JSONException
    {
        String response = queryAPI();

        Log.i("API Response", response);

        //parse and return the response
        return new JSONObject(response);
    }

    /**
     * Queries the API specified at a URL
     * @return JSON Response from URL
     * @throws IOException
     */
    private String queryAPI() throws IOException
    {
        //response from server
        StringBuilder response = new StringBuilder();

        //create the url based on the app URL and specified file
        URL url = new URL(api.getURL());

        //create a new connection to the server
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("POST");
        httpURLConnection.setDoOutput(true);

        //gather all post data to submit to the server
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(httpURLConnection.getOutputStream()));
        bufferedWriter.write(api.getPostData());
        bufferedWriter.flush();
        bufferedWriter.close();

        //read response from the server
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

        //for each line in the response, append to the response string
        for (String line; (line = bufferedReader.readLine()) != null; )
            response.append(line);


        httpURLConnection.disconnect();

        return response.toString();
    }

    /**
     * Downloads an image from the web server
     * @param fileUri path to image online
     * @return save File object
     * @throws IOException
     */
    public File downloadImage(String fileUri, String directory) throws IOException
    {
        //create the url based on the app URL and specified file
        URL url = new URL(fileUri);

        //create a new connection to the server
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

        File baseDir = new File(Constants.BASE_FILE_DIRECTORY);
        if(!baseDir.isDirectory())
            if(!baseDir.mkdir())
                throw new IOException("Failed to make base file directory");

        //Create the folder
        File mediaFolder = new File(directory);
        if(!mediaFolder.isDirectory())
            if(!mediaFolder.mkdir())
                throw new IOException("Failed to make directory");


        //Create the file
        File mediaFile = new File(Image.generateFileUri(directory).getAbsolutePath());
        if (!mediaFile.createNewFile())
            throw new IOException("Failed to create file");

        //read the input from the web server
        InputStream inputStream = httpURLConnection.getInputStream();
        FileOutputStream fileOutputStream = new FileOutputStream(mediaFile);

        byte[] buffer = new byte[1024];
        int bufferLength;

        //start writing the media file
        while((bufferLength = inputStream.read(buffer)) > 0)
            fileOutputStream.write(buffer, 0, bufferLength);


        //close and disconnect from the web server
        fileOutputStream.close();
        httpURLConnection.disconnect();

        return mediaFile;
    }

}


package com.alphadevelopmentsolutions.frcscout.Classes;

import java.io.File;
import java.util.UUID;

public class Image
{
    /**
     * Generates a unique file path for a new file
     * @return file with UUID
     */
    public static File generateFileUri(String directory)
    {
        File mediaFolder = new File(directory);

        File mediaFile;

        //keep generating UUID's until we found one that does not exist
        //should only run once
        do
        {
            mediaFile = new File(mediaFolder.getAbsolutePath() + "/" + UUID.randomUUID().toString() + ".jpeg");
        }while (mediaFile.isFile());

        return mediaFile;
    }
}

package com.alphadevelopmentsolutions.frcscout.classes

import java.io.File
import java.util.*

object Image
{
    /**
     * Generates a unique file path for a new file
     * @return file with UUID
     */
    fun generateFileUri(directory: String): File
    {
        val mediaFolder = File(directory)

        var mediaFile: File

        //keep generating UUID's until we found one that does not exist
        //should only run once
        do
        {
            mediaFile = File(mediaFolder.absolutePath + "/" + UUID.randomUUID().toString() + ".jpeg")
        } while (mediaFile.isFile)

        return mediaFile
    }
}

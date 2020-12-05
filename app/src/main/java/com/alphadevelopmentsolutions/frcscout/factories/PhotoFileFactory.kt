package com.alphadevelopmentsolutions.frcscout.factories

import android.content.Context
import com.alphadevelopmentsolutions.frcscout.interfaces.Constant
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.quality
import java.io.File

object PhotoFileFactory {

    /**
     * Creates a new file to store the photo
     * @param fileName [String] the name of the file to create
     * @param context [Context]
     * @return [File] object or null if: the file already exists or the file cannot be created
     */
    fun createFile(fileName: String, context: Context): File? {
        getFileUri(
            "$fileName${Constant.IMAGE_FILE_EXTENSION}",
            context
        )?.let { path ->

            if (path.isNotBlank()) {
                val file = File(path)

                if (file.exists())
                    return null

                if (!file.createNewFile())
                    return null

                return file
            }
        }

        return null
    }

    /**
     * Gets the photo file
     * @param fileName [String] the name of the file to create
     * @param context [Context]
     * @return [File] object or null if the file does not exist
     */
    fun getFile(fileName: String, context: Context): File? {
        getFileUri(fileName, context)
            ?.let { path ->

                if (path.isNotBlank()) {
                    val file = File(path)

                    if (file.exists())
                        return file
                }
            }

        return null
    }

    fun files(context: Context): MutableList<File> {
        val files = mutableListOf<File>()

        getFileDir(context)?.let { dir ->
            dir.listFiles()?.let { dirFiles ->
                files.addAll(dirFiles)
            }
        }

        return files
    }

    /**
     * Gets the file director for a new file
     * @param context [Context]
     * @return [File] or null if [Context.getExternalFilesDir] fails
     */
    fun getFileDir(context: Context): File? =
        context.getExternalFilesDir(Constant.IMAGE_EXTERNAL_FILES_DIR)

    /**
     * Gets the file URI for the supplied URI string
     * @param uri [String] to create a file uri for
     * @param context [Context]
     * @return [String] object for the logo file URI or null if [getFileDir] fails
     */
    private fun getFileUri(uri: String, context: Context): String? {
        getFileDir(context)?.let { dir ->

            return StringBuilder()
                .append("${dir.path}/")
                .append(uri)
                .toString()
        }

        return null
    }

    /**
     * Compresses the supplied image file
     * @param context [Context]
     * @param imageFile [File] file to compress
     */
    suspend fun compressPhoto(context: Context, imageFile: File) {
        Compressor.compress(context, imageFile) {
            default()
            destination(imageFile)
            quality(50)
        }
    }
}
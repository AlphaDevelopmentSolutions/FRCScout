package com.alphadevelopmentsolutions.frcscout.classes

import com.alphadevelopmentsolutions.frcscout.data.models.PhotoFile

class PhotoFileChunk(
    var chunkSize: Double,
    var photoFiles: MutableList<PhotoFile>
)
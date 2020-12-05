package com.alphadevelopmentsolutions.frcscout.callbacks

import androidx.annotation.FloatRange
import java.io.Serializable

interface OnProgressCallback : Serializable {
    fun onProgressChange(message: String? = null, @FloatRange(from = 0.0, to = 1.0) progress: Float? = null)
}
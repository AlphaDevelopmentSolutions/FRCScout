package com.alphadevelopmentsolutions.frcscout.classes

import com.google.gson.annotations.SerializedName

class SuccessErrorCode(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("error") val error: Int = 400,
    @SerializedName("message") val message: String = ""
)
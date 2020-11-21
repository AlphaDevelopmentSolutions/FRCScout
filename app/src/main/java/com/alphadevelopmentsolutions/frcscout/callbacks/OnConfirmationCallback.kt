package com.alphadevelopmentsolutions.frcscout.callbacks

import java.io.Serializable

interface OnConfirmationCallback : Serializable {
    fun onConfirm()
    fun onCancel()
}
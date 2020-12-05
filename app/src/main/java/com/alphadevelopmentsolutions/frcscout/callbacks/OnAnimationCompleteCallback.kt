package com.alphadevelopmentsolutions.frcscout.callbacks

import java.io.Serializable

interface OnAnimationCompleteCallback : Serializable {
    fun onSuccess()

    fun onFail()

    fun onFinish()
}
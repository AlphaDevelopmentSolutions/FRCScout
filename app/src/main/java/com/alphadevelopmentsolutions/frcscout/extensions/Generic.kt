package com.alphadevelopmentsolutions.frcscout.extensions

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import com.alphadevelopmentsolutions.frcscout.FRCScoutApplication
import kotlinx.coroutines.*

/**
 * Launches a new [CoroutineScope] in the [Dispatchers.IO] dispatcher
 */
fun launchIO(block: suspend CoroutineScope.() -> Unit) =
    GlobalScope.launch(
        Dispatchers.IO,
        CoroutineStart.DEFAULT,
        block
    )

fun launchIO(lifecycleOwner: LifecycleOwner, block: suspend CoroutineScope.() -> Unit) =
    lifecycleOwner.lifecycle.coroutineScope.launch(
        Dispatchers.IO,
        CoroutineStart.DEFAULT,
        block
    )

fun runOnUiThread(block: () -> Unit) =
    FRCScoutApplication.runOnUiThread(block)
package com.alphadevelopmentsolutions.frcscout.extensions

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
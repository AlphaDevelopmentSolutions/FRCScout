package com.alphadevelopmentsolutions.frcscout.exceptions

class PreconditionFailedException(currentApiVersion: Int) : Exception("API out of date. Current API version: $currentApiVersion")

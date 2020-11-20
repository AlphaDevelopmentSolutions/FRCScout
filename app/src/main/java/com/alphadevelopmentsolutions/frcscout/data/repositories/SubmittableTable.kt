package com.alphadevelopmentsolutions.frcscout.data.repositories

import com.alphadevelopmentsolutions.frcscout.data.models.Table

interface SubmittableTable<T : Table> {
    fun getAllRaw(isDraft: Boolean?): List<T>
}
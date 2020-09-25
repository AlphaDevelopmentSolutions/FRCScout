package com.alphadevelopmentsolutions.frcscout.repository

import com.alphadevelopmentsolutions.frcscout.table.Table

interface SubmittableTable<T : Table> {
    fun getAllRaw(isDraft: Boolean?): List<T>
}
package com.alphadevelopmentsolutions.frcscout.data.models

interface StaticTable<T: Table> {
    fun create(): T

    private fun create(table: T): T {
        table.isDraft = true
        return table
    }
}
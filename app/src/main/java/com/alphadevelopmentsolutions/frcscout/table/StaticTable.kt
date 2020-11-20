package com.alphadevelopmentsolutions.frcscout.table

interface StaticTable<T: Table> {
    fun create(): T

    private fun create(table: T): T {
        table.isDraft = true
        return table
    }
}
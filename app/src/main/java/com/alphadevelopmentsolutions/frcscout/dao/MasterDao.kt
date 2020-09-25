package com.alphadevelopmentsolutions.frcscout.dao

import androidx.room.*

@Dao
abstract class MasterDao<Table> {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(obj: Table): Long

    @Update
    abstract suspend fun update(obj: Table): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insertAll(objs: List<Table>): List<Long>

    @Update
    abstract suspend fun updateAll(obj: List<Table>)

    @Delete
    abstract suspend fun delete(obj: Table)

    @Transaction
    open suspend fun upsert(obj: Table): Long {
        insert(obj).let { id ->
            if (id == (-1).toLong()) {
                return update(obj).toLong()
            }

            return id
        }
    }

    @Transaction
    open suspend fun upsertAll(objs: List<Table>) {
        val updateList = mutableListOf<Table>()

        insertAll(objs).let { insertResults ->
            insertResults.forEachIndexed { index, result ->
                if (result == (-1).toLong())
                    updateList.add(objs[index])
            }

            if (updateList.isNotEmpty()) {
                updateAll(updateList.toList())
            }
        }
    }
}
package com.alphadevelopmentsolutions.frcscout.Classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alphadevelopmentsolutions.frcscout.Classes.Tables.*
import com.alphadevelopmentsolutions.frcscout.Dao.*

@Database(
        entities =
        [
                ChecklistItem::class,
                ChecklistItemResult::class,
                Event::class,
                EventTeamList::class,
                Match::class,
                RobotInfo::class,
                RobotInfoKey::class,
                RobotMedia::class,
                ScoutCardInfo::class,
                ScoutCardInfoKey::class,
                Team::class,
                User::class,
                Year::class
        ],
        version = 1)
abstract class RDatabase : RoomDatabase() {

    abstract fun checklistItemDao(): ChecklistItemDao
    abstract fun checklistItemResultDao(): ChecklistItemResultDao
    abstract fun eventDao(): EventDao
    abstract fun eventTeamListDao(): EventTeamListDao
    abstract fun matchDao(): MatchDao
    abstract fun robotInfoDao(): RobotInfoDao
    abstract fun robotInfoKeyDao(): RobotInfoKeyDao
    abstract fun robotMediaDao(): RobotMediaDao
    abstract fun scoutCardInfoDao(): ScoutCardInfoDao
    abstract fun scoutCardInfoKeyDao(): ScoutCardInfoKeyDao
    abstract fun teamDao(): TeamDao
    abstract fun userDao(): UserDao
    abstract fun yearDao(): YearDao

    companion object {
        private var instance: RDatabase? = null

        /**
         * Gets a new or existing instance of [RDatabase]
         */
        fun getInstance(
                context: Context
        ): RDatabase {

            // If the instance is not already created,
            // create a new instance and return it
            return instance
                    ?: synchronized(this) {
                        val tempInstance = Room.databaseBuilder(
                                context.applicationContext,
                                RDatabase::class.java,
                                "FRCScout.db"
                        ).build()

                        instance = tempInstance
                        tempInstance
                    }
        }
    }
}
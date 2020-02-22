package com.alphadevelopmentsolutions.frcscout.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alphadevelopmentsolutions.frcscout.classes.table.account.*
import com.alphadevelopmentsolutions.frcscout.classes.table.core.*
import com.alphadevelopmentsolutions.frcscout.converter.DataTypeConverter
import com.alphadevelopmentsolutions.frcscout.converter.DateConverter
import com.alphadevelopmentsolutions.frcscout.converter.MatchTypeConverter
import com.alphadevelopmentsolutions.frcscout.converter.UUIDConverter
import com.alphadevelopmentsolutions.frcscout.dao.*

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
@TypeConverters(
        value = [
            UUIDConverter::class,
            DateConverter::class,
            MatchTypeConverter::class,
            DataTypeConverter::class
        ]
)
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
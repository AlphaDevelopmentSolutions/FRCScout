package com.alphadevelopmentsolutions.frcscout.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alphadevelopmentsolutions.frcscout.data.dao.MatchDao
import com.alphadevelopmentsolutions.frcscout.data.models.*

@Database(
    entities = [
        ChecklistItem::class,
        ChecklistItemResult::class,
        DataType::class,
        Event::class,
        EventTeamList::class,
        Match::class,
        MatchType::class,
        RobotInfo::class,
        RobotInfoKey::class,
        RobotMedia::class,
        Role::class,
        ScoutCardInfo::class,
        ScoutCardInfoKey::class,
        ScoutCardInfoKeyState::class,
        Team::class,
        TeamAccount::class,
        User::class,
        UserRole::class,
        UserTeamAccount::class,
        Year::class
    ],
    views = [

    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [

    ]
)
abstract class RDatabase : RoomDatabase() {

    abstract fun getMatchDao(): MatchDao

    companion object {
        private var instance: RDatabase? = null

        private const val DATABASE_NAME = "frcscout.db"

        fun getInstance(context: Context): RDatabase {
            return instance ?: synchronized(this) {
                val tempInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        RDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                instance = tempInstance
                tempInstance
            }
        }
    }
}
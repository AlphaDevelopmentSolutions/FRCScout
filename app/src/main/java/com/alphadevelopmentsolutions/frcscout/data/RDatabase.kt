package com.alphadevelopmentsolutions.frcscout.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alphadevelopmentsolutions.frcscout.converters.ChecklistStatusConverter
import com.alphadevelopmentsolutions.frcscout.converters.DateConverter
import com.alphadevelopmentsolutions.frcscout.converters.ItemStateConverter
import com.alphadevelopmentsolutions.frcscout.data.dao.*
import com.alphadevelopmentsolutions.frcscout.data.models.*
import com.alphadevelopmentsolutions.frcscout.enums.ChecklistStatus

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
        DateConverter::class,
        ItemStateConverter::class,
        ChecklistStatusConverter::class
    ]
)
abstract class RDatabase : RoomDatabase() {

    abstract fun getChecklistItemDao(): ChecklistItemDao
    abstract fun getChecklistItemResultDao(): ChecklistItemResultDao
    abstract fun getDataTypeDao(): DataTypeDao
    abstract fun getEventDao(): EventDao
    abstract fun getEventTeamListDao(): EventTeamListDao
    abstract fun getMatchDao(): MatchDao
    abstract fun getMatchTypeDao(): MatchTypeDao
    abstract fun getRobotInfoKeyDao(): RobotInfoKeyDao
    abstract fun getRobotInfoDao(): RobotInfoDao
    abstract fun getRobotMediaDao(): RobotMediaDao
    abstract fun getRoleDao(): RoleDao
    abstract fun geScoutCardInfoKeyDao(): ScoutCardInfoKeyDao
    abstract fun getScoutCardInfoKeyStateDao(): ScoutCardInfoKeyStateDao
    abstract fun getScoutCardInfoDao(): ScoutCardInfoDao
    abstract fun getTeamAccountDao(): TeamAccountDao
    abstract fun getTeamDao(): TeamDao
    abstract fun getUserDao(): UserDao
    abstract fun getUserRoleDao(): UserRoleDao
    abstract fun getUserTeamAccountDao(): UserTeamAccountDao
    abstract fun getYearDao(): YearDao

    companion object {
        private var INSTANCE: RDatabase? = null

        private const val DATABASE_NAME = "frcscout.db"

        fun getInstance(context: Context): RDatabase {
            return INSTANCE ?: synchronized(this) {
                val tempInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        RDatabase::class.java,
                        DATABASE_NAME
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                INSTANCE = tempInstance
                tempInstance
            }
        }

        fun destroyInstance() {
            INSTANCE?.close()
            INSTANCE = null
        }
    }
}
package com.alphadevelopmentsolutions.frcscout.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alphadevelopmentsolutions.frcscout.converters.ChecklistStatusConverter
import com.alphadevelopmentsolutions.frcscout.converters.DateConverter
import com.alphadevelopmentsolutions.frcscout.converters.ItemStateConverter
import com.alphadevelopmentsolutions.frcscout.data.dao.*
import com.alphadevelopmentsolutions.frcscout.data.models.*
import com.alphadevelopmentsolutions.frcscout.data.repositories.LogDeleteRepository
import com.alphadevelopmentsolutions.frcscout.interfaces.TableName
import com.alphadevelopmentsolutions.frcscout.interfaces.Trigger

@Database(
    entities = [
        ChecklistItem::class,
        ChecklistItemResult::class,
        DataType::class,
        Event::class,
        EventTeamList::class,
        LogDelete::class,
        LogDraft::class,
        Match::class,
        MatchType::class,
        PhotoFile::class,
        RobotInfo::class,
        RobotInfoKey::class,
        RobotInfoKeyState::class,
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
    abstract fun getLogDeleteDao(): LogDeleteDao
    abstract fun getLogDraftDao(): LogDraftDao
    abstract fun getMatchDao(): MatchDao
    abstract fun getMatchTypeDao(): MatchTypeDao
    abstract fun getPhotoFileDao(): PhotoFileDao
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
                        .addCallback(
                            object : Callback() {
                                override fun onCreate(db: SupportSQLiteDatabase) {
                                    super.onCreate(db)

                                    // Create a trigger for inserting photos into the database
                                    db.execSQL(
                                        """
                                            CREATE TRIGGER ${TableName.ROBOT_MEDIA}_local_file_insert
                                            AFTER INSERT ON ${TableName.ROBOT_MEDIA}
                                            WHEN (SELECT log_draft FROM ${TableName.LOG_DRAFT} LIMIT 1) = 1 AND NEW.id NOT IN (SELECT photo_id FROM ${TableName.PHOTO_FILE})
                                            BEGIN
                                                INSERT OR IGNORE INTO ${TableName.PHOTO_FILE} (`photo_id`, `file_name`, `file_uri`)
                                                VALUES (NEW.id, NEW.uri, NEW.local_file_uri);
                                            END;
                                        """.trimIndent()
                                    )

                                    /**
                                     * Use this section when adding a new table to the database if the [Table]'s instance
                                     * of [MasterRepository] extends [SubmittableTable]
                                     */
                                    with(TableName) {
                                        Trigger.getTriggers(CHECKLIST_ITEM_RESULT).forEach { db.execSQL(it) }
                                        Trigger.getTriggers(ROBOT_INFO).forEach { db.execSQL(it) }
                                        Trigger.getTriggers(ROBOT_MEDIA).forEach { db.execSQL(it) }
                                        Trigger.getTriggers(SCOUT_CARD_INFO).forEach { db.execSQL(it) }
                                    }
                                }
                            }
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

    /**
     * Clears all data from the database
     * @param logDelete [Boolean] whether you should log the deletion of all objects
     * @param includePhotoFiles [Boolean] whether you should include photo files stored on the phone in the deletion
     */
    suspend fun clearDatabase(logDelete: Boolean, includePhotoFiles: Boolean) {

        val logDeleteRepo = LogDeleteRepository(getLogDeleteDao())

        if (!logDelete)
            logDeleteRepo.disable()

        if (includePhotoFiles)
            getPhotoFileDao().deleteAll()

        if (!logDelete)
            logDeleteRepo.enable()
    }
}
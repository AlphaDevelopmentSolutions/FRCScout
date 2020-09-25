package com.alphadevelopmentsolutions.frcscout.classes

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.alphadevelopmentsolutions.frcscout.dao.MatchDao

@Database(
    entities = [

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

        private const val DATABASE_NAME = ""

        fun getInstance(context: Context): RDatabase {
            return instance ?: synchronized(this) {
                val tempInstance =
                    Room.databaseBuilder(
                        context.applicationContext,
                        RDatabase::class.java,
                        DATABASE_NAME
                    )
                        .addCallback(object : Callback() {
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                            }
                        })
                        .fallbackToDestructiveMigration()
                        .build()

                instance = tempInstance
                tempInstance
            }
        }
    }
}
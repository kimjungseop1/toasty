package com.syncrown.arpang.db.push_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [PushMessageEntity::class], version = 1, exportSchema = false)
abstract class PushMessageDatabase: RoomDatabase() {
    abstract fun pushMessageDao(): PushMessageDao

    companion object {
        @Volatile
        private var INSTANCE: PushMessageDatabase? = null

        val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                //쿼리 작성
            }
        }

        fun getDatabase(context: Context): PushMessageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PushMessageDatabase::class.java,
                    "push_message_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
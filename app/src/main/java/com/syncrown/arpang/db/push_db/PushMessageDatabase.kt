package com.syncrown.arpang.db.push_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PushMessageEntity::class], version = 1, exportSchema = false)
abstract class PushMessageDatabase: RoomDatabase() {
    abstract fun pushMessageDao(): PushMessageDao

    companion object {
        @Volatile
        private var INSTANCE: PushMessageDatabase? = null

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
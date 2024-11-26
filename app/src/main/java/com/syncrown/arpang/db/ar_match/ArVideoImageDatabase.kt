package com.syncrown.arpang.db.ar_match

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ArVideoImageEntity::class], version = 1, exportSchema = false)
abstract class ArVideoImageDatabase: RoomDatabase() {
    abstract fun arVideoImageDao(): ArVideoImageDao

    companion object {
        @Volatile
        private var INSTANCE: ArVideoImageDatabase? = null

        fun getDatabase(context: Context): ArVideoImageDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ArVideoImageDatabase::class.java,
                    "video_image_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
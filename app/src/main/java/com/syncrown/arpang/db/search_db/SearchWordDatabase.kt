package com.syncrown.arpang.db.search_db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [SearchWordEntity::class], version = 1, exportSchema = false)
abstract class SearchWordDatabase: RoomDatabase() {
    abstract fun searchWordDao(): SearchWordDao

    companion object {
        @Volatile
        private var INSTANCE: SearchWordDatabase? = null

        val MIGRATION_1_2 = object : Migration(1,2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                //쿼리 작성
            }
        }

        fun getDatabase(context: Context): SearchWordDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SearchWordDatabase::class.java,
                    "search_word_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}